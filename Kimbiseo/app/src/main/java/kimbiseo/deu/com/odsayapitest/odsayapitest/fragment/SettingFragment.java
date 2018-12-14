package kimbiseo.deu.com.odsayapitest.odsayapitest.fragment;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.odsay.odsayandroidsdk.API;
import com.odsay.odsayandroidsdk.ODsayData;
import com.odsay.odsayandroidsdk.ODsayService;
import com.odsay.odsayandroidsdk.OnResultCallbackListener;

import com.odsay.odsayandroidsdk.ODsayService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import kimbiseo.deu.com.odsayapitest.R;
import kimbiseo.deu.com.odsayapitest.odsayapitest.GpsInfo;
import kimbiseo.deu.com.odsayapitest.odsayapitest.alarm.AlarmReceiver;
import kimbiseo.deu.com.odsayapitest.odsayapitest.insert_table;
import kimbiseo.deu.com.odsayapitest.odsayapitest.insert_table2;
import kimbiseo.deu.com.odsayapitest.odsayapitest.mainPackage.MainPage;
import kimbiseo.deu.com.odsayapitest.odsayapitest.pathSearchPackage.DataSingleton;

import static android.content.Context.MODE_PRIVATE;
import static android.view.Gravity.BOTTOM;


public class SettingFragment extends Fragment
{
    Button btnChangeHome;
    Button btnChangeTT;
    Button btnChangeInfo;
    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = false;
    private GpsInfo gps;
    Button titleAlarm;
    LinearLayout onoffLayout;
    CheckBox checkBoxAutoAlarm;
    TextView alarmInfo;
    double latitude ;
    double longitude;

    Button titleAlarmSound;
    LinearLayout soundLayout;
    RadioGroup radioGroup;
    RadioButton rbSound1;
    RadioButton rbSound2;
    RadioButton rbSound3;

    int[] firstClassTimeOfWeek;
    int firstClassTimeOfDay;
    int nextDay;
    int readyTime;
    int moveTime;

    String alarmInfoText;
    String alarmSound;
    ODsayService odsayService;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        // 레이아웃 설정
        ConstraintLayout settingLayout = (ConstraintLayout)inflater.inflate(R.layout.fragment_setting, container,false);
        final ConstraintLayout layout = (ConstraintLayout)settingLayout.findViewById(R.id.settingLayout);

        titleAlarm = (Button)layout.findViewById(R.id.titleAlarm);
        onoffLayout = (LinearLayout)layout.findViewById(R.id.onoffLayout);
        checkBoxAutoAlarm = (CheckBox)layout.findViewById(R.id.checkBoxAutoAlarm);
        alarmInfo = (TextView)layout.findViewById(R.id.alarmInfo);
        alarmInfo.setText(alarmInfoText);

        titleAlarmSound = (Button)layout.findViewById(R.id.titleAlarmSound);
        soundLayout = (LinearLayout)layout.findViewById(R.id.soundLayout);
        radioGroup = (RadioGroup)layout.findViewById(R.id.radioGroup);
        rbSound1 = (RadioButton)layout.findViewById(R.id.rbSound1);
        rbSound2 = (RadioButton)layout.findViewById(R.id.rbSound2);
        rbSound3 = (RadioButton)layout.findViewById(R.id.rbSound3);

        btnChangeHome = (Button) layout.findViewById(R.id.btnChangeHome);
        btnChangeTT = (Button) layout.findViewById(R.id.btnChangeTT);
        btnChangeInfo = (Button) layout.findViewById(R.id.btnChangeInfo);


        // 알람 탭 클릭 시
        titleAlarm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(onoffLayout.getVisibility() == layout.GONE)
                {
                    onoffLayout.setVisibility(layout.VISIBLE);
                }
                else
                {
                    onoffLayout.setVisibility(layout.GONE);
                }
            }
        });


        // 자동 알람 기능 체크 박스 클릭 시
        checkBoxAutoAlarm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // 알람 설정
                if(checkBoxAutoAlarm.isChecked())
                {
                    // 체크 시 알람 등록
                    alarmInfoText = ((MainPage)MainPage.fragmentContext).setAlarm(getAlarmSetting());
                    alarmInfo.setText(alarmInfoText);
                }
                else
                {
                    // 체크 해제시 알람 해제
                    ((MainPage)MainPage.fragmentContext).releaseAlarm();
                    alarmInfoText = "";
                    alarmInfo.setText(alarmInfoText);
                }

            }
        });

        // 알람 소리 탭 클릭 시
        titleAlarmSound.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(soundLayout.getVisibility() == layout.GONE)
                {
                    soundLayout.setVisibility(layout.VISIBLE);
                }
                else
                {
                    soundLayout.setVisibility(layout.GONE);
                }
            }
        });


        // 설정되는 알람 값 식별
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                Intent intent = new Intent(MainPage.fragmentContext, AlarmReceiver.class);

                switch(checkedId)
                {
                    case R.id.rbSound1:
                        alarmSound = "clock";
                        break;
                    case R.id.rbSound2:
                        alarmSound = "army";
                        break;
                    case R.id.rbSound3:
                        alarmSound = "nature";
                        break;
                    default:
                        alarmSound = "default";
                        break;
                }

                // 현재 선택된 소리값 전달
                intent.putExtra("state", alarmSound);
                ((MainPage)MainPage.fragmentContext).sendBroadcast(intent);
            }
        });

        //거주지 변경버튼 이벤트
        btnChangeHome.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                for (int i = 0; i < 2; i++) {
                    if (!isPermission) {
                        callPermission();
                    }

                    gps = new GpsInfo(SettingFragment.this.getActivity().getApplicationContext());

                    if (gps.isGetLocation()) {
                        gps.getLocation();
                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();

                        DataSingleton.INSTANCE.setStartPointx(String.valueOf(longitude));
                        DataSingleton.INSTANCE.setStartPointy(String.valueOf(latitude));



                        SQLiteDatabase db2 = SettingFragment.this.getActivity().openOrCreateDatabase("gps_data", MODE_PRIVATE, null);
                        ContentValues cv = new ContentValues();
                        cv.put("LATITUDE", latitude);
                        cv.put("LONGITUDE", longitude);
                        if (i == 1) {
                            db2.update("GPS_TABLE", cv, "LATITUDE<1000 OR LATITUDE IS NULL", null);
                            db2.execSQL("UPDATE GPS_TABLE SET LATITUDE = " + latitude + " , LONGITUDE = " + longitude);
                            Toast.makeText(SettingFragment.this.getActivity().getApplicationContext(), "거주지가 변경되었습니다.\nlon:" + longitude + " lat:" + latitude, Toast.LENGTH_LONG).show();
                            getpathTime();
                        }




                    } else {
                        gps.showSettingsAlert();
                    }
                }
            }
        });
        //거주지 변경 버튼 이벤트 끝


        //시간표 변경 이벤트
        btnChangeTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SettingFragment.this.getContext());

                // AlertDialog 셋팅
                alertDialogBuilder
                        .setMessage("시간표 정보가 삭제됩니다.\n계속 하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("진행",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        //시간표 정보 삭제
                                        SettingFragment.this.getContext().deleteDatabase("/data/data/kimbiseo.deu.com.odsayapitest/databases/time_table");
                                        Intent intent = new Intent(SettingFragment.this.getContext(), insert_table.class);
                                        startActivity(intent);
                                    }
                                })
                        .setNegativeButton("취소",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        // 다이얼로그를 취소한다
                                        dialog.cancel();
                                    }
                                });

                // 다이얼로그 생성
                AlertDialog alertDialog = alertDialogBuilder.create();

                // 다이얼로그 보여주기
                alertDialog.show();
            }
        });
        //시간표 변경 이벤트 끝
        btnChangeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SettingFragment.this.getContext());

                // AlertDialog 셋팅
                alertDialogBuilder
                        .setMessage("학교 정보 및 준비시간 정보가 삭제됩니다.\n계속 하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("진행",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        //시간표 정보 삭제
                                        SettingFragment.this.getContext().deleteDatabase("/data/data/kimbiseo.deu.com.odsayapitest/databases/user_data");
                                        Intent intent = new Intent(SettingFragment.this.getContext(), insert_table2.class);
                                        startActivity(intent);
                                    }
                                })
                        .setNegativeButton("취소",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        // 다이얼로그를 취소한다
                                        dialog.cancel();
                                    }
                                });

                // 다이얼로그 생성
                AlertDialog alertDialog = alertDialogBuilder.create();

                // 다이얼로그 보여주기
                alertDialog.show();
            }
        });

        //사용자 정보 변경 이벤트


        return layout;
    }


    // 알람을 설정할 시간 계산
    public Calendar getAlarmSetting()
    {
        Calendar calendar = Calendar.getInstance();

        // 주중의 첫번째 수업 시간들을 저장할 배열
        firstClassTimeOfWeek = new int[5];

        // 초기화
        for(int i=0; i<5; i++)
            firstClassTimeOfWeek[i] = 0;

        SQLiteDatabase db = this.getActivity().openOrCreateDatabase("time_table", MODE_PRIVATE, null);
        Cursor c = db.rawQuery("SELECT DAY, TIME FROM TIME_TABLE GROUP BY DAY, TIME ORDER BY TIME", null);

        while(c.moveToNext())
        {
            String day = c.getString(c.getColumnIndex("DAY"));
            int time = Integer.parseInt(c.getString(c.getColumnIndex("TIME")));

            switch (day)
            {
                case "mon":
                    if (firstClassTimeOfWeek[0] == 0)
                        firstClassTimeOfWeek[0] = time + 8;
                    break;
                case "tue":
                    if (firstClassTimeOfWeek[1] == 0)
                        firstClassTimeOfWeek[1] = time + 8;
                    break;
                case "wed":
                    if (firstClassTimeOfWeek[2] == 0)
                        firstClassTimeOfWeek[2] = time + 8;
                    break;
                case "thu":
                    if (firstClassTimeOfWeek[3] == 0)
                        firstClassTimeOfWeek[3] = time + 8;
                    break;
                case "fri":
                    if (firstClassTimeOfWeek[4] == 0)
                        firstClassTimeOfWeek[4] = time + 8;
                    break;
                default:
                    break;
            }

        }

        c.close();

        // 내일 요일을 설정
        nextDay = calendar.get(Calendar.DAY_OF_WEEK);
        nextDay = nextDay + 1;
        if(nextDay == 8)
            nextDay = 1;

        // 내일 요일에 해당하는 첫번째 수업 시간을 구함
        switch (nextDay)
        {
            case 1:
                //일요일
                break;
            case 2:
                //월요일
                firstClassTimeOfDay = firstClassTimeOfWeek[0];
                break;
            case 3:
                //화요일
                firstClassTimeOfDay = firstClassTimeOfWeek[1];
                break;
            case 4:
                //수요일
                firstClassTimeOfDay = firstClassTimeOfWeek[2];
                break;
            case 5:
                //목요일
                firstClassTimeOfDay = firstClassTimeOfWeek[3];
                break;
            case 6:
                //금요일
                firstClassTimeOfDay = firstClassTimeOfWeek[4];
                break;
            case 7:
                //토요일
                break;
        }


        // 사용자의 평소 준비 시간을 가져옴
        db = this.getActivity().openOrCreateDatabase("user_data", MODE_PRIVATE, null);
        Cursor r = db.rawQuery("SELECT READY_TIME FROM DATA_TABLE", null);

        while(r.moveToNext())
            readyTime = Integer.parseInt(r.getString(r.getColumnIndex("READY_TIME")));

        r.close();


        // 목적지 까지 가는데 필요한 경로 소요 시간을 가져옴

        // TEST
        // 내일 첫 수업 시작 시간
        //firstClassTimeOfDay = 10;

        // 위의 설정값들로 알람 시간 설정
        calendar.add(Calendar.DATE, 1);     // 하루 더함
        calendar.set(Calendar.HOUR_OF_DAY, firstClassTimeOfDay - (((readyTime + moveTime) / 60) + 1));
        calendar.set(Calendar.MINUTE, 60 - (readyTime + moveTime) % 60);
        calendar.set(Calendar.SECOND, 0);

        if(firstClassTimeOfDay == 0)
        {
            // 내일 수업이 없을 경우
            return null;
        }
        else
        {
            System.out.println("설정된 알람 시간 : " + calendar.getTime());
        }

        return calendar;
    }

    public CheckBox getCheckBoxAutoAlarm()
    {
        return checkBoxAutoAlarm;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_ACCESS_FINE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            isAccessFineLocation = true;
        } else if (requestCode == PERMISSIONS_ACCESS_COARSE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            isAccessCoarseLocation = true;
        }

        if (isAccessFineLocation && isAccessCoarseLocation) {
            isPermission = true;
        }
    }

    private void callPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && this.getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_ACCESS_FINE_LOCATION);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && this.getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_ACCESS_COARSE_LOCATION);
        } else {
            isPermission = true;
        }
    }


    private void getpathTime(){

        /* ODsay 서비스 초기화된계, 객체 생성 및 서버와의 연결 제한,데이터 획득 제한 시간을 설정한다 */
        odsayService = ODsayService.init(getActivity(), "aacboAGWZuvOGtobeow0qGNHkakZHU14IuMLtbk6yeQ");
        ///‘ODsayService’ 객체 생성.
        odsayService.setConnectionTimeout(5000);        //서버 연결 제한 시간 설정.
        odsayService.setReadTimeout(5000);        //데이터 획득 제한 시간 설정

        // odsayService api 싱글턴 객체를 통해 대중교통길찾기 API에 값을 전달하고 매개인자로 참고된 콜백함수에 결과를 반환한다.


        OnResultCallbackListener onResultCallbackListener = new OnResultCallbackListener() { //OdysayApi 결과 콜백메서드
            @Override
            public void onSuccess(ODsayData odsayData, API api) {
                // 호출된 api가 대중교통 길찾기 api 일때
                if (api == API.SEARCH_PUB_TRANS_PATH) { // 길찾기 선택시
                    try {
                        /* 루트 오브젝트 객체 생성 */
                        JSONObject rootobject = odsayData.getJson().getJSONObject("result");
                        /* 대중교통 길찾기 결과 오브젝트(실질적인 길 데이터) 어레이 */
                        JSONArray PathJson = rootobject.getJSONArray("path");
                        /* 길찾기 부가정보 rootobject 에서 받아오기 */
                        JSONObject PathObject=PathJson.getJSONObject(0);


                            JSONObject infoObject=PathObject.getJSONObject("info");
                            // 경로 오브젝트에는 info라는 오브젝트가 존재하고, 그안에 해당 경로에 관한 정보가 담겨있다

                            String totalTime=infoObject.getString("totalTime"); // 경로에 걸리는 시간 정보
                            moveTime=Integer.parseInt(totalTime);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onError(int code, String message, API api) {
                Log.i("[대중교통 길찾기]", "경로 검색에 실패하였습니다.");
            }
        };

        odsayService.requestSearchPubTransPath( String.valueOf(longitude) , String.valueOf(latitude), String.valueOf(DataSingleton.INSTANCE.getstopPointx())
                ,String.valueOf(DataSingleton.INSTANCE.getstopPointy()), "0", "0", "0", onResultCallbackListener);
    }

}