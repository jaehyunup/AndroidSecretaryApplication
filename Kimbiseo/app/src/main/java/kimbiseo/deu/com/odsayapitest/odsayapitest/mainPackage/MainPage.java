package kimbiseo.deu.com.odsayapitest.odsayapitest.mainPackage;


import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import kimbiseo.deu.com.odsayapitest.odsayapitest.DBHelper;
import kimbiseo.deu.com.odsayapitest.odsayapitest.GpsInfo;
import kimbiseo.deu.com.odsayapitest.odsayapitest.alarm.AlarmReceiver;
import kimbiseo.deu.com.odsayapitest.odsayapitest.fragment.CommunityFragment;
import kimbiseo.deu.com.odsayapitest.odsayapitest.fragment.SettingFragment;
import kimbiseo.deu.com.odsayapitest.odsayapitest.fragment.TimeTableFragment;
import kimbiseo.deu.com.odsayapitest.R;
import kimbiseo.deu.com.odsayapitest.odsayapitest.insert_table;
import kimbiseo.deu.com.odsayapitest.odsayapitest.insert_table2;
import kimbiseo.deu.com.odsayapitest.odsayapitest.pathSearchPackage.PathMainActivity;

import java.util.ArrayList;
import java.util.Calendar;


//implement the interface OnNavigationItemSelectedListener in your activity class
public class MainPage extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener
    {
    Fragment timeTableFragment, routeFragment, communityFragment, settingFragment;

    Intent alarmIntent;
    Context context;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    TextView alarm;
    public static Context fragmentContext;


    // 영훈이 부분
    private TextView[] mTextView = new TextView[40];
    public SharedPreferences prefs;

    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = false;
    private GpsInfo gps;

    private DBHelper dbHelper;
    private SQLiteDatabase gdb;
    public boolean check_test = false;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        prefs = getSharedPreferences("Pref", MODE_PRIVATE); // Pref 불러오기

        gps = new GpsInfo(getApplicationContext());

        if(!prefs.getBoolean("isFirstRun", true)) {
            timeTableFragment = new TimeTableFragment();
            communityFragment = new CommunityFragment();
            settingFragment = new SettingFragment();
        }
        this.context = this;
        alarmIntent = new Intent(this.context, AlarmReceiver.class);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        final Calendar calendar = Calendar.getInstance();;
        final Intent alarmIntent = new Intent(this.context, AlarmReceiver.class);
        fragmentContext = this;

        // GPS 권한
        callPermission();


        // 첫 실행 여부 판단
        checkFirstRun();



        // 알람 설정
        //setAlarm(calendar, alarmIntent);



        // 첫 로딩시 생성되는 화면
        loadFragment(timeTableFragment);

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
    }

    // 영훈이 부분

    // 첫 실행 여부 판단
    public boolean checkFirstRun()
    {
        boolean isFirstRun = prefs.getBoolean("isFirstRun",true);

        if(isFirstRun)
        {
            // 만약 첫 시작이면  시간표 설정 페이지로 이동
            Intent newIntent = new Intent(MainPage.this, insert_table.class);
            startActivityForResult(newIntent, 0);
            return true;
        }
        else
        {
            //selectDB();
            return false;

        }
    }


    // 실행여부 판단 (왔다갔다)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode){
            case 0:     //시간표입력 실패
                Intent newIntent = new Intent(MainPage.this, insert_table.class);
                startActivityForResult(newIntent, 0);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);

                break;
            case 1:     //시간표입력 성공
                newIntent = new Intent(MainPage.this, insert_table2.class);
                startActivityForResult(newIntent, 0);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                break;
            case 2:     //정보입력 성공
                prefs.edit().putBoolean("isFirstRun",false).apply();
                dbHelper = new DBHelper(MainPage.this, "gps_data", null, 1);
                dbHelper.readDB();
                dbHelper.createGPS();
                dbHelper.addGPS();
                dbHelper = new DBHelper(MainPage.this, "memo_data", null, 1);
                dbHelper.readDB();
                dbHelper.createMemo();

                newIntent = new Intent(MainPage.this, MainPage.class);
                startActivity(newIntent);
                finish();
                break;
        }
    }

    // GPS 권한
    private void callPermission()
    {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_ACCESS_FINE_LOCATION);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_ACCESS_COARSE_LOCATION);
        } else {
            isPermission = true;
        }
    }


    // 승제 부분

        // 알람 설정
        public String setAlarm(Calendar calendar)
        {
            String alarmInfo;

            if(calendar == null)
            {
                alarmInfo = "내일은 수업이 없습니다!";
                SettingFragment settingFragment = (SettingFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                // 체크해제
                settingFragment.getCheckBoxAutoAlarm().setChecked(false);
            }
            else
            {
                alarmIntent.putExtra("state", "alarm on");

                // 알람 메내저 설정
                alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                pendingIntent = PendingIntent.getBroadcast
                        (
                                context,
                                0,
                                alarmIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );

                alarmManager.setExact
                        (
                                AlarmManager.RTC_WAKEUP,
                                calendar.getTimeInMillis(),
                                pendingIntent
                        );

                alarmInfo = "설정된 알람 시간 : " + calendar.getTime();


                    Toast.makeText(context, "알람이 설정되었습니다" , Toast.LENGTH_LONG).show();
                }

            // 테스트옹 10초 알람 등록
/*
                alarmManager.setExact
                        (
                                AlarmManager.RTC_WAKEUP,
                                System.currentTimeMillis() + 10*1000,
                                pendingIntent
                        );

                alarmInfo = "설정된 알람 시간 : 10초 후";

*/
            // 전달 받은 알람 시간값으로 알람 등록
            return alarmInfo;
        }

        // 설정된 알람 해제
        public void releaseAlarm()
        {
            if(pendingIntent != null)
            {
                alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmIntent = new Intent(context, AlarmReceiver.class);

                pendingIntent = PendingIntent.getBroadcast(
                        context.getApplicationContext(),
                        0,
                        alarmIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT);

                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();
                alarmManager = null;
                pendingIntent = null;

                Toast.makeText(context, "알람이 해제되었습니다", Toast.LENGTH_LONG).show();
            }
        }

        // 알람 페이지에서 알람 종료 시 다음 알람을 설정
        public void setNextAlarm()
        {
            SettingFragment settingFragment = (SettingFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            setAlarm(settingFragment.getAlarmSetting());
        }

    // 네이게이션 메뉴 클릭에 따라 페이지 이동
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        Fragment fragment = null;

        switch (item.getItemId())
        {
            case R.id.navigation_time_table:
                fragment = timeTableFragment;
                return loadFragment(fragment);

            case R.id.navigation_route:
                //fragment = routeFragment;
                startActivity(new Intent(getApplication(), PathMainActivity.class));
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);


                break;

            case R.id.navigation_community:
                fragment = communityFragment;
                return loadFragment(fragment);


            case R.id.navigation_setting:
                fragment = settingFragment;
                return loadFragment(fragment);
        }

        return false;
    }



    private boolean loadFragment(Fragment fragment)
    {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }



}
