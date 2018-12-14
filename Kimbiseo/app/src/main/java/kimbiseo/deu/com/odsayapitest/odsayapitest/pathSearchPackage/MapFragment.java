package kimbiseo.deu.com.odsayapitest.odsayapitest.pathSearchPackage;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.maps.overlay.NMapPathData;
import com.nhn.android.maps.overlay.NMapPathLineStyle;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.overlay.NMapPathDataOverlay;
import com.nhn.android.mapviewer.overlay.NMapResourceProvider;

import java.util.ArrayList;

import kimbiseo.deu.com.odsayapitest.R;

public class MapFragment extends Fragment {
    private NMapController mMapController;
    private NMapResourceProvider mMapViewerResourceProvider1;
    private NMapResourceProvider mMapViewerResourceProvider2;
    private NMapContext mMapContext;
    private NMapOverlayManager mMapOverlayManager1;
    private NMapOverlayManager mMapOverlayManager2;

    private NMapPOIdata poiData;
    private NMapPOIdataOverlay poidataoverlay;
    private NMapPOIdataOverlay poidataoverlay2;

    private NMapView mapView;
    private NMapLocationManager mMapLocationManager;
    private NMapCompassManager mMapCompassManager;
    private NMapMyLocationOverlay mMyLocationOverlay;
    private NGeoPoint MyLocation=new NGeoPoint(Double.parseDouble(DataSingleton.INSTANCE.startPointx),
            Double.parseDouble(DataSingleton.INSTANCE.startPointy));

    private NGeoPoint StopLocation=new NGeoPoint(Double.parseDouble(DataSingleton.INSTANCE.stopPointx),
            Double.parseDouble(DataSingleton.INSTANCE.stopPointy));
    private int markerid;

    private static final String CLIENT_ID = "Vu5S2v0PiQrxi9MpG4Oh";// 애플리케이션 클라이언트 아이디 값
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_fragment, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapContext =  new NMapContext(super.getActivity());
        mMapContext.onCreate();
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) { // 맵을 생성하고 뷰를 실질적으로 등록한다
        super.onActivityCreated(savedInstanceState);
        // location manager
        // create map view
            /*
        // create parent view to rotate map view
        mMapContainerView = new MapContainerView(getActivity());
        mMapContainerView.addView(mMapView);

        // set the activity content to the parent view
        setContentView(mMapContainerView);

        */

        mapView = (NMapView)getView().findViewById(R.id.mapView); // 맵뷰객체
        mapView.setClientId(CLIENT_ID);// 클라이언트 아이디 설정
        mMapContext.setupMapView(mapView); //맵을 부른 context에 맵뷰를 set
        mapView.setScalingFactor(3.0f,true); //네이버 지도 배율 및 HD 맵 설정
        mapView.displayZoomControls(true);
        mapView.setClickable(true);
        mapView.setEnabled(true);
        mapView.setBuiltInZoomControls(true,null);
        mMapController = mapView.getMapController();
        mMapController.setMapCenter(MyLocation);

        mMapViewerResourceProvider1 =new NMapResourceProvider(getActivity()) {
            // 프로바이더에 이미지 등록.
            @Override
            protected int findResourceIdForMarker(int i, boolean b) {

                return R.drawable.ic_map_start;
            }

            @Override
            protected Drawable getDrawableForMarker(int i, boolean b, NMapOverlayItem nMapOverlayItem) {
                return null;
            }
            @Override
            public Drawable getCalloutBackground(NMapOverlayItem nMapOverlayItem) {
                return null;
            }
            @Override
            public String getCalloutRightButtonText(NMapOverlayItem nMapOverlayItem) {
                return null;
            }
            @Override
            public Drawable[] getCalloutRightButton(NMapOverlayItem nMapOverlayItem) {
                return new Drawable[0];
            }
            @Override
            public Drawable[] getCalloutRightAccessory(NMapOverlayItem nMapOverlayItem) {
                return new Drawable[0];
            }
            @Override
            public int[] getCalloutTextColors(NMapOverlayItem nMapOverlayItem) {
                return new int[0];
            }
            @Override
            public Drawable[] getLocationDot() {
                return new Drawable[0];
            }
            @Override
            public Drawable getDirectionArrow() {
                return null;
            }
            @Override
            public int getParentLayoutIdForOverlappedListView() {
                return 0;
            }
            @Override
            public int getOverlappedListViewId() {
                return 0;
            }
            @Override
            public int getLayoutIdForOverlappedListView() {
                return 0;
            }
            @Override
            public void setOverlappedListViewLayout(ListView listView, int i, int i1, int i2) {

            }
            @Override
            public int getListItemLayoutIdForOverlappedListView() {
                return 0;
            }
            @Override
            public int getListItemTextViewId() {
                return 0;
            }
            @Override
            public int getListItemTailTextViewId() {
                return 0;
            }
            @Override
            public int getListItemImageViewId() {
                return 0;
            }
            @Override
            public int getListItemDividerId() {
                return 0;
            }
            @Override
            public void setOverlappedItemResource(NMapPOIitem nMapPOIitem, ImageView imageView) {

            }
        };

        mMapViewerResourceProvider2 =new NMapResourceProvider(getActivity()) {
            // 프로바이더에 이미지 등록.
            @Override
            protected int findResourceIdForMarker(int i, boolean b) {

                        return R.drawable.ic_map_arrive;

            }

            @Override
            protected Drawable getDrawableForMarker(int i, boolean b, NMapOverlayItem nMapOverlayItem) {
                return null;
            }
            @Override
            public Drawable getCalloutBackground(NMapOverlayItem nMapOverlayItem) {
                return null;
            }
            @Override
            public String getCalloutRightButtonText(NMapOverlayItem nMapOverlayItem) {
                return null;
            }
            @Override
            public Drawable[] getCalloutRightButton(NMapOverlayItem nMapOverlayItem) {
                return new Drawable[0];
            }
            @Override
            public Drawable[] getCalloutRightAccessory(NMapOverlayItem nMapOverlayItem) {
                return new Drawable[0];
            }
            @Override
            public int[] getCalloutTextColors(NMapOverlayItem nMapOverlayItem) {
                return new int[0];
            }
            @Override
            public Drawable[] getLocationDot() {
                return new Drawable[0];
            }
            @Override
            public Drawable getDirectionArrow() {
                return null;
            }
            @Override
            public int getParentLayoutIdForOverlappedListView() {
                return 0;
            }
            @Override
            public int getOverlappedListViewId() {
                return 0;
            }
            @Override
            public int getLayoutIdForOverlappedListView() {
                return 0;
            }
            @Override
            public void setOverlappedListViewLayout(ListView listView, int i, int i1, int i2) {

            }
            @Override
            public int getListItemLayoutIdForOverlappedListView() {
                return 0;
            }
            @Override
            public int getListItemTextViewId() {
                return 0;
            }
            @Override
            public int getListItemTailTextViewId() {
                return 0;
            }
            @Override
            public int getListItemImageViewId() {
                return 0;
            }
            @Override
            public int getListItemDividerId() {
                return 0;
            }
            @Override
            public void setOverlappedItemResource(NMapPOIitem nMapPOIitem, ImageView imageView) {

            }
        };

        mMapOverlayManager1=new NMapOverlayManager(getActivity(),mapView, mMapViewerResourceProvider1);
        mMapOverlayManager2=new NMapOverlayManager(getActivity(),mapView, mMapViewerResourceProvider2);


/*
        mMapLocationManager = new NMapLocationManager(getActivity());
        mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);
        // compass manager
        mMapCompassManager = new NMapCompassManager(getActivity());
        // create my location overlay

        mMyLocationOverlay = mMapOverlayManager.createMyLocationOverlay(mMapLocationManager, mMapCompassManager);

*/
        new Handler().postDelayed(new Runnable() {
            // 맵이 생성되고 나서 현재 자기위치 찍기.
            // NaverMap API 상 마커를 동적으로 찍을때 맵 객체 생성과의 딜레이 문제 때문에 핸들러를 얻어서 1초뒤에 찍는것으로.
            @Override
            public void run() {
                /*mMapOverlayManager.addOverlay(mMyLocationOverlay);
                mMyLocationOverlay.setCompassHeadingVisible(true);
                mMapCompassManager.enableCompass();
                mapView.setAutoRotateEnabled(true, false);
                mapView.requestLayout();
                mapView.postInvalidate();*/
                setPathMarker(MyLocation,StopLocation);
                setPathOverlay(MyLocation,DataSingleton.INSTANCE.stopPointx,DataSingleton.INSTANCE.stopPointy);


            }
        }, 3000);

        // close OnCreate()

        //오버레이 매니저 객체 생성

    }


    /* MyLocation Listener */
    private final NMapLocationManager.OnLocationChangeListener onMyLocationChangeListener = new NMapLocationManager.OnLocationChangeListener() {
        @Override
        public boolean onLocationChanged(NMapLocationManager locationManager, NGeoPoint myLocation) {
            if (mMapController != null) {
                mMapController.animateTo(myLocation);
            }

            return true;
        }

        @Override
        public void onLocationUpdateTimeout(NMapLocationManager nMapLocationManager) {

        }

        @Override
        public void onLocationUnavailableArea(NMapLocationManager nMapLocationManager, NGeoPoint nGeoPoint) {

        }
    };



    public void ClearMarker(){ // 맵 마커 제거
        mMapOverlayManager1.clearOverlays();
        mMapOverlayManager2.clearOverlays();

    }
    public void setPathMarker(NGeoPoint start,NGeoPoint stop){
        double startx =start.longitude;
        double starty =start.latitude;
        double stopx =stop.longitude;
        double stopy =stop.longitude;

        // set POI data
        NMapPOIdata poiData = new NMapPOIdata(1, mMapViewerResourceProvider1);
        poiData.beginPOIdata(1);
        poiData.addPOIitem(startx, starty, "출발",R.drawable.ic_map_start,0);
        poiData.endPOIdata();

        NMapPOIdata poiData2 = new NMapPOIdata(1, mMapViewerResourceProvider2);
        poiData.beginPOIdata(1);
        poiData.addPOIitem(stopx, stopy, "도착",R.drawable.ic_map_arrive,0);
        poiData.endPOIdata();

        // create POI data overlay
        poidataoverlay = mMapOverlayManager1.createPOIdataOverlay(poiData,null);
        poidataoverlay2 = mMapOverlayManager2.createPOIdataOverlay(poiData2,null);

        poidataoverlay.showAllPOIdata(0);
        poidataoverlay2.showAllPOIdata(0);
    }


    public void setPathOverlay(NGeoPoint myLocation,String stopx,String stopy){
        // 출발지와 도착지 마커 오버레이 객체 생성및 등록 메서드
        // 출, 도착지 Xy를 찍는다
        // set path data points
        ArrayList<NGeoPoint> mapObj=DataSingleton.INSTANCE.PathMapObj; // 데이터싱글턴에서 해당 값 다받아오기
        NMapPathData pathData = new NMapPathData(mapObj.size()+1); // 출발,도착 좌표는 따로찍는다.따라서 +2
        pathData.initPathData();

        pathData.addPathPoint(myLocation.longitude,myLocation.latitude, NMapPathLineStyle.TYPE_DASH);
        for(int i=0;i<mapObj.size();i++){ // 대중교통 경로 오버레이 추가
            NGeoPoint temp =mapObj.get(i);
            pathData.addPathPoint(temp.longitude,temp.latitude, NMapPathLineStyle.TYPE_SOLID);
        }
        pathData.addPathPoint(StopLocation.longitude,StopLocation.latitude, NMapPathLineStyle.TYPE_DASH);

        pathData.endPathData();


        /*
        NMapPathData pathData = new NMapPathData(5);
        pathData.initPathData();

        pathData.addPathPoint(129.0578444, 35.157815, NMapPathLineStyle.TYPE_DASH);
        pathData.addPathPoint(129.05693112891606, 35.157825227286146, NMapPathLineStyle.TYPE_SOLID);
        pathData.addPathPoint(129.05273836281796, 35.15782572653872,NMapPathLineStyle.TYPE_SOLID);
        pathData.addPathPoint(129.05125609264658, 35.1577845314924, NMapPathLineStyle.TYPE_SOLID);
        pathData.addPathPoint(129.05018877088168, 35.15757746627191, NMapPathLineStyle.TYPE_DASH);
        pathData.endPathData();

        /*pathData.addPathPoint(127.107458, 37.365608, 0);
        pathData.addPathPoint(127.107232, 37.365608, 0);
        pathData.addPathPoint(127.106904, 37.365624, 0);
        pathData.addPathPoint(127.105933, 37.365621, NMapPathLineStyle.TYPE_DASH); // 점선
        pathData.addPathPoint(127.105929, 37.366378, 0);
        pathData.addPathPoint(127.106279, 37.366380, 0);*/
        //pathData.endPathData();*/

        NMapPathDataOverlay pathDataOverlay = mMapOverlayManager1.createPathDataOverlay(pathData);

        pathDataOverlay.showAllPathData(0);

    }
    @Override
    public void onStart(){
        super.onStart();
        mMapContext.onStart();
    }
    @Override
    public void onResume() {
        super.onResume();
        mMapContext.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        mMapContext.onPause();
    }
    @Override
    public void onStop() {
        mMapContext.onStop();
        super.onStop();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    @Override
    public void onDestroy() {
        mMapContext.onDestroy();
        super.onDestroy();
    }

    private void restoreInstanceState() {

        mMapController.setMapCenter(MyLocation);

    }

}
