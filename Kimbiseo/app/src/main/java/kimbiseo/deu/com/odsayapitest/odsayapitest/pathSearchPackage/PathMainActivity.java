package kimbiseo.deu.com.odsayapitest.odsayapitest.pathSearchPackage;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.odsay.odsayandroidsdk.API;
import com.odsay.odsayandroidsdk.ODsayData;
import com.odsay.odsayandroidsdk.ODsayService;
import com.odsay.odsayandroidsdk.OnResultCallbackListener;

import android.app.AlarmManager;
import android.content.Context;
import android.location.LocationManager;
import android.provider.ContactsContract;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import kimbiseo.deu.com.odsayapitest.R;

import static android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED;
import static android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED;
import static android.support.design.widget.BottomSheetBehavior.STATE_HIDDEN;

public class PathMainActivity extends FragmentActivity {
    FloatingActionButton Fab;
    LinearLayout buttonGrid;
    Button AllPathBtn;
    Button selectPathBtn;
    Button BusPathBtn;
    Button SubwayPathBtn;
    TableLayout AllPathTable;
    TableLayout SubwayTable;
    SubwayResultFragment swfrag;
    BusResultFragment bsfrag;
    PathResultFragment prfrag;
    BusSubwayFragment bsswfrag;
    BottomSheetBehavior bottomSheetBehavior;
    ODsayService odsayService;

    AlarmManager alarm_manager;
    String startPointx=DataSingleton.INSTANCE.startPointx;
    String startPointy=DataSingleton.INSTANCE.startPointy;
    String stopPointx=DataSingleton.INSTANCE.stopPointx;
    String stopPointy=DataSingleton.INSTANCE.stopPointy;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInit(this);

        setContentView(R.layout.activity_main);


        /* 맵 프라그먼트를 mapfragmentLayout에 호출시키는 부분*/
        MapFragment mapfragment = new MapFragment();
        mapfragment.setArguments(new Bundle());
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.mapfragmentLayout, mapfragment);
        fragmentTransaction.commit();
        /* 맵 프라그먼트에 네이버 맵 객체 부여 및 커밋 끝*/

        /* 각 타입별 경로 테이블을 생성하는 각 프라그 먼트 객체의 생성 및 각 프라그먼트의 OnCreateView 메서드 부르기*/

        /* 프라그먼트 객체 생성 끝*/



        /* 위젯 관련 처리 */
        buttonGrid = (LinearLayout) findViewById(R.id.ButtonGrid);
        Fab = (FloatingActionButton) findViewById(R.id.fab);
        bottomSheetBehavior = BottomSheetBehavior.from(buttonGrid);
        AllPathBtn = (Button) findViewById(R.id.allpath);
        BusPathBtn = (Button) findViewById(R.id.BusOnly);
        SubwayPathBtn = (Button) findViewById(R.id.SubwayOnly);
        selectPathBtn =(Button)findViewById(R.id.selectpath);
        AllPathTable = (TableLayout) findViewById(R.id.pathresultTable);// 모든 타입의 경로검색결과가 저장될 테이블


        /* ODsay 서비스 초기화된계, 객체 생성 및 서버와의 연결 제한,데이터 획득 제한 시간을 설정한다 */
        odsayService = ODsayService.init(this, "aacboAGWZuvOGtobeow0qGNHkakZHU14IuMLtbk6yeQ");
        ///‘ODsayService’ 객체 생성.
        odsayService.setConnectionTimeout(5000);        //서버 연결 제한 시간 설정.
        odsayService.setReadTimeout(5000);        //데이터 획득 제한 시간 설정
        odsayService.requestSearchPubTransPath(startPointx, startPointy,stopPointx
                ,stopPointy, "0", "0", "0", onResultCallbackListener);
        // odsayService api 싱글턴 객체를 통해 대중교통길찾기 API에 값을 전달하고 매개인자로 참고된 콜백함수에 결과를 반환한다.

        Fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (bottomSheetBehavior.getState()) {
                    case STATE_HIDDEN:// 바텀시트가 숨겨진 상태일때
                        bottomSheetBehavior.setState(STATE_COLLAPSED);// 바텀시트를 보통상태로 한다.
                        break;
                    case STATE_COLLAPSED:
                        bottomSheetBehavior.setState(STATE_EXPANDED); // 바텀시트를 확장한다
                        break;
                    case STATE_EXPANDED:
                        bottomSheetBehavior.setState(STATE_COLLAPSED); // 바텀시트를 보통상태로 한다.
                }
            }
        });


        SubwayPathBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(STATE_EXPANDED);

                callFragment(1);
            }
        });
        BusPathBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(STATE_EXPANDED);

                callFragment(2);

            }
        });
        selectPathBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(STATE_EXPANDED);

                callFragment(3);
            }
        });
        AllPathBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(STATE_EXPANDED);

                callFragment(4);
            }
        });

    }

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
                    GetSearchInfo(rootobject); //
                    DataSingleton.INSTANCE.setSearchbigPathArray(PathJson);
                    // 검색결과의 큰경로 data를 저장
                    ArrayList<JSONArray> SubPathArray = DataSingleton.INSTANCE.getSubPathList(DataSingleton.INSTANCE.BigPathBus);
                    // SubPath 의 어레이를 반환하는 메서드
                    LocationManager lm=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
                    // 위치관리자 객체
                    //GetResultPath(SubPathArray,PathTable);//PathTable에 각 경로검색 결과를 저장하는 메서드
                    callLaneLoad(); // 맵 보간점 api 호출 메서드.

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (api == API.LOAD_LANE) { // 좌표 보간점 API 반환시
                try {
                    makePathOverlayData(odsayData.getJson().getJSONObject("result"));
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

    /* 길찾기 검색 첫실행시 공통되는 부가정보들 msg 배열의 0~8까지. */
    private void GetSearchInfo(JSONObject rootobject) throws JSONException {
        String[] msg = new String[6];
        msg[0] = rootobject.getString("outTrafficCheck"); // 도시간 직통 탐색 유무 (다른 도시로 길찾기할때)
        msg[1] = rootobject.getString("searchType"); // 결과구분 , 0=도시내,1=도시간 환승,2=도시간 직통
        msg[2] = rootobject.getString("subwayCount"); // 검색된 길중 지하철로만 갈수 있는길 개수 0
        msg[3] = rootobject.getString("busCount"); // 검색된 길중 버스로만 갈수 있는 길 개수 10
        msg[4] = rootobject.getString("subwayBusCount"); // 검색된 길중 버스+지하철로 갈수 있는 길 개수 3
        msg[5] = rootobject.getString("pointDistance"); // 출,도착지간 직선거리
        DataSingleton.INSTANCE.setInfoData(msg);
    }



    private TableLayout getTable(TableLayout layout, ArrayList<TableRow> rowarray) {
        return layout;
    }

    private void LayoutInit(Context context) { // 어플리케이션 초기화 메서드
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    private void callFragment(int frament_no) {// 맵 아래나오는 바텀시트바에 들어가는 프라그먼트 변경 메서드.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (frament_no) {
            case 1:
                //지하철
                swfrag = new SubwayResultFragment();
                transaction.replace(R.id.PathFragment, swfrag);
                transaction.commit();
                break;
            case 2:
                //버스
                bsfrag=new BusResultFragment();
                transaction.replace(R.id.PathFragment, bsfrag);
                transaction.commit();
                break;
            case 3:
                // '모든 경로 결과 프라그먼트 ' 호출
                prfrag=new PathResultFragment();
                transaction.replace(R.id.PathFragment, prfrag); // Path 프라그먼트에 뒤으 파라미터로 들어가는놈 으로 변경.
                transaction.commit();
                break;
            case 4:
                // 버스+지하철
                bsswfrag=new BusSubwayFragment();
                transaction.replace(R.id.PathFragment, bsswfrag); // Path 프라그먼트에 뒤으 파라미터로 들어가는놈 으로 변경.
                transaction.commit();
                break;
        }
    }

    private void callLaneLoad(){
        JSONObject layPath=DataSingleton.INSTANCE.BigPathTotal.get(1); // 경로 첫번째오브젝트.
        try {
            JSONObject info = layPath.getJSONObject("info");
            String mapObj=info.getString("mapObj");
            String parsedmapObj="0:0@"+mapObj;
            odsayService.requestLoadLane(parsedmapObj,onResultCallbackListener);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void makePathOverlayData(JSONObject rootObject){
        try {
            JSONArray laneArray=rootObject.getJSONArray("lane");
            for(int a=0;a<laneArray.length();a++){
                JSONObject lanearray=laneArray.getJSONObject(a); //
                JSONArray sectionArray=lanearray.getJSONArray("section");
                JSONArray graphArray=sectionArray.getJSONObject(0).getJSONArray("graphPos");
                for(int e=0;e<graphArray.length();e++){
                    NGeoPoint overlayPoint=new NGeoPoint(Double.parseDouble(graphArray.getJSONObject(e).getString("x")),
                            Double.parseDouble(graphArray.getJSONObject(e).getString("y")));
                    DataSingleton.INSTANCE.setPoint(overlayPoint);
                    System.out.print(e+":");
                }
            }

        }

        catch (JSONException e) {
            e.printStackTrace();
        }
    }

}


