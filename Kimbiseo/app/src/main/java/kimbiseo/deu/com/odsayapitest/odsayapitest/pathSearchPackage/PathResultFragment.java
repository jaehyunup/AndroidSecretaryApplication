package kimbiseo.deu.com.odsayapitest.odsayapitest.pathSearchPackage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kimbiseo.deu.com.odsayapitest.R;

import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

public class PathResultFragment extends Fragment{
// 모든 길 찾기 프라그먼트
        TableLayout tableLayout;
        TableRow tablerow;
        LinearLayout FcchLinear;
        LinearLayout ndchLinear;
        LinearLayout rootLinear;
        ArrayList<ArrayList<String>> onoffList=new ArrayList<ArrayList<String>>();
            // 각 어레이 리스트 인덱스는 각 정보 보기 버튼, 내부 어레이 리스트는 차례대로 승차,하차 정류장 순.
            ArrayList<String> temponoffList=new ArrayList<String>();
         int whatbutton=-1;
         int count=-1;
        public PathResultFragment() {
            // Required empty public constructor
        }


        public interface PathResultFragmentListener {
            void onClicked(View v);
            // onCliked , 콜백으로서 엑티비티에서 구현한다. 이벤트는 프라그먼트에서 터트림
        }




    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.pathresult_fragment,container,false);
            tableLayout=(TableLayout)layout.findViewById(R.id.pathresultTable); // 테이블 레이아웃


            try {
                MakePathinfoTable();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return layout;

        }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    public int Dptopix(int value){ // Dp를 픽셀로 변환반화
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,value,getResources().getDisplayMetrics());
    }

    private void MakeTableRow(String totalTime,String totalDistance,String payment,ArrayList<String> temponoffList){
        onoffList.add(temponoffList); // onoffList에 현재 추가할 경로 정보 보기 버튼에 들어갈 경로 승하차 정보를 담는다.

        tablerow =new TableRow(getActivity()); // 테이블 로우 파라미터 설정
        TableLayout.LayoutParams TableRowParams=new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT,1);
        tablerow.setLayoutParams(TableRowParams);
        tablerow.setBackgroundResource(R.drawable.radius_listbg);
        tablerow.setPadding(Dptopix(16),Dptopix(16),Dptopix(16),Dptopix(16));

        rootLinear=new LinearLayout(getActivity()); // 루트 리니어 파라미터 설정
        TableRow.LayoutParams rootLinearParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,1);
        rootLinear.setOrientation(VERTICAL);
        rootLinear.setLayoutParams(rootLinearParams);


        FcchLinear=new LinearLayout(getActivity()); // 첫번째 자식리니어 파라미터 설정
        LinearLayout.LayoutParams FcchLinearParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,1);
        FcchLinear.setOrientation(HORIZONTAL);
        FcchLinearParams.bottomMargin=Dptopix(8);
        FcchLinear.setLayoutParams(FcchLinearParams);


        ndchLinear=new LinearLayout(getActivity()); // 두번째 자식리니어 파라미터 설정
        LinearLayout.LayoutParams ndchLinearParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                Dptopix(40),1);
        ndchLinear.setOrientation(LinearLayout.HORIZONTAL);
        ndchLinear.setLayoutParams(ndchLinearParams);



        // 소유시간 텍스트 뷰 객체 생성 및 파라미터 설정
        TextView time = new TextView(getActivity());
        time.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "nanumgothicbold.ttf"));
        time.setText(totalTime+"분 소요");
        time.setTextColor(Color.parseColor("#018786"));
        int timeTextDp=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10,getResources().getDisplayMetrics());
        time.setTextSize(timeTextDp);
        LinearLayout.LayoutParams timeParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        FcchLinear.addView(time,timeParams);


        // 이동거리
        TextView distance = new TextView(getActivity());
        distance.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "nanumgothic.ttf"));
        int disPadDP=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,6,getResources().getDisplayMetrics());
        distance.setPadding(disPadDP, 0, 0, 0);
        distance.setText("("+totalDistance+"m)");
        distance.setTextSize(20);
        LinearLayout.LayoutParams distanceParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        // 이동거리 텍스트 뷰객체 생성
        FcchLinear.addView(distance,distanceParams);



        // 요금
        TextView SemiPath = new TextView(getActivity());
        SemiPath.setText(payment+"원");
        SemiPath.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "nanumgothicbold.ttf"));
        SemiPath.setTextSize(Dptopix(8));

        int SemiPathTextWidth=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,100,getResources().getDisplayMetrics());
        int SemiPathTextHeight=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,30,getResources().getDisplayMetrics());
        LinearLayout.LayoutParams SemiPathParams = new LinearLayout.LayoutParams(SemiPathTextWidth,SemiPathTextHeight,3);
        SemiPathParams.leftMargin=4;
        SemiPath.setLayoutParams(SemiPathParams);
        SemiPath.setGravity(Gravity.BOTTOM);
        ndchLinear.addView(SemiPath,SemiPathParams);

        // 버튼 두개
        Button pathinfobutton = new Button(getActivity());
        // pathinfobutton.setId(R.id.pathinfobutton);
        pathinfobutton.setBackgroundResource(R.drawable.radius_bluebutton);
        pathinfobutton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sharp_navigate_next_24px,0);
        pathinfobutton.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "nanumgothicbold.ttf"));
        pathinfobutton.setText("경로 정보");
        pathinfobutton.setTextColor(Color.parseColor("#FFFFFF"));
        pathinfobutton.setTextSize(14);
        // 경로 정보 버튼 객체 생성
        int infobtnwidth=Dptopix(70);
        int infobtnheight=Dptopix(26);

        LinearLayout.LayoutParams pathinfoParams = new LinearLayout.LayoutParams(infobtnwidth,infobtnheight,1);
        pathinfobutton.setLayoutParams(pathinfoParams);
        ndchLinear.addView(pathinfobutton,pathinfoParams);
        // 경로 정보버튼 파라미터 설정 및 두번째 최하위 자식 리니어 레이아웃에 추가

        Button searchPath = new Button(getActivity());
        //searchgetActivity()Path.setId(R.id.searchgetActivity()Path);
        searchPath.setBackgroundResource(R.drawable.radius_greenbutton);
        searchPath.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_sharp_navigate_next_24px,0);
        searchPath.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "nanumgothicbold.ttf"));
        searchPath.setText("경로 보기");
        searchPath.setTextColor(Color.parseColor("#FFFFFF"));
        searchPath.setTextSize(14);
        // 경로 안내 시작 버튼 객체 생성

        LinearLayout.LayoutParams sbuttonParams = new LinearLayout.LayoutParams(infobtnwidth,infobtnheight,1);

        sbuttonParams.leftMargin = Dptopix(4);
        searchPath.setLayoutParams(sbuttonParams);
        ndchLinear.addView(searchPath,sbuttonParams);

        rootLinear.addView(FcchLinear,FcchLinearParams);
        rootLinear.addView(ndchLinear,ndchLinearParams);
        tablerow.addView(rootLinear,rootLinearParams);
        tableLayout.addView(tablerow,TableRowParams);

        /* 경로 정보 상세보기(텍스트로 보기) 버튼 이벤트 리스너
        * 1. 이또한 동적으로 생성
        * 2. onoffList 는 각 버튼의 이벤트에 들어갈 어레이 리스트 인덱스를 담고있고
        *    whatbutton은 현재 클릭된 버튼에게 부여한 번호이다.
        * 3. ex) onoffList.get(2) = 3번째 버튼의 이벤트 클릭이며, 그 승하차 리스트를 반환한다.*/

        pathinfobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatbutton=count;

                Intent intent=new Intent(getActivity(),infoView.class);
                for(int i=0;i<onoffList.get(whatbutton).size();i++) {
                   intent.putExtra("SubPathList",onoffList.get(whatbutton));

                }
                startActivity(intent);
            }
        });

        /* 경로 보기 ( 맵으로 보기 ) 버튼 이번트 리스너
         * 1. whatButton(버튼 번호) 에 따라서 , 각 버튼이 가지는 정보값의 mapObj를 맵 프라그먼트에 뿌려주어야한다.
           2. 액티비티에 이벤트를 전달해야하기때문에 안에서는 기존 클릭리스너 사용하고, 클릭되었을때 buttonClicked 메서드
              실행해서 인터페이스를 구현한 엑티비티에서 이벤트가 발동되도록 구현. */
       searchPath.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

           }
       });
    }

    private void MakePathinfoTable() throws JSONException {
        for (int rowcount = 0; rowcount < DataSingleton.INSTANCE.BigPathTotal.size(); rowcount++) {
            ArrayList<String> temponoffList=new ArrayList<String>();

            JSONObject PathObject=DataSingleton.INSTANCE.BigPathTotal.get(rowcount);
            // 데이터 클래스에 있는 이동타입이 버스와 지하철이 섞인 경로가 들어있는 JSONObject를 받음.
            // 데이터 클래스에 존재하는 BigPathBus 라는 어레이리스트의 인덱스에 각 경로가 들어있다.
            // 그러므로 for문을 이용하여 모든 경로에 대한 데이터를 수집시작.

            JSONObject infoObject=PathObject.getJSONObject("info");
            // 경로 오브젝트에는 info라는 오브젝트가 존재하고, 그안에 해당 경로에 관한 정보가 담겨있다

            String totalTime=infoObject.getString("totalTime"); // 경로에 걸리는 시간 정보
            String totalDistance=infoObject.getString("totalDistance"); // 경로의 총 이동거리
            String firstStartStation=infoObject.getString("firstStartStation"); // 시작 첫 정류장
            String lastEndStation=infoObject.getString("lastEndStation"); // 목적 정류장
            String payment=infoObject.getString("payment"); // 요금
            String mapObj=infoObject.getString("mapObj"); // 맵오브젝트 ( 네이버 맵에 뿌려줄 좌표값의 보간점 )
            JSONArray subPath=PathObject.getJSONArray("subPath");

            for (int i = 0; i < subPath.length(); i++) {// SubPath 개수만큼.
                //서브 패스의 인덱스 개수만큼, 서브패스 각인덱스에는 Jsonarray가 있다.
                JSONObject SubPathObject=subPath.getJSONObject(i);

                if (SubPathObject.getString("trafficType").equals("1")) { // 지하철 일때
                    JSONArray laneArray=SubPathObject.getJSONArray("lane");// 노선 들어있는 어레이
                    JSONObject stopList=SubPathObject.getJSONObject("passStopList"); // 정류장 들어있는 오브젝트
                    JSONArray stationList=stopList.getJSONArray("stations"); // 거쳐야할 정류장 리스트어레이.

                    /*하위의 JsonArray[lane] 에 타야할 지하철 번호가 나옴
                     * 하위의 JsonObject passStopList -> JsonArray[Jsonobject] stations ->에 해당 이동수단의 정류장  */
                    String startname=SubPathObject.getString("startName")+"역-> "; // 승차역 이름
                    String laneway=SubPathObject.getString("way")+"방면\n";
                    String lanename=laneArray.getJSONObject(0).getString("name")+"승차"; // 노선이름(부산 2호선)

                    String lanewayname=startname+laneway+lanename;
                    temponoffList.add(lanewayname);
                    String stationString="";
                    for(int v=0;v<stationList.length();v++){ // 정류장 리스트 뽑기
                        stationString=stationString+stationList.getJSONObject(v).getString("stationName")
                                .toString()+"ᚔ"; // 정류장 번호
                    }
                    temponoffList.add(stationString);

                    String endname=SubPathObject.getString("endName")+" 하차"; // 하차역 이름
                    temponoffList.add(endname);
                } else if (SubPathObject.getString("trafficType").equals("2")) { // 버스 일때
                    JSONArray laneArray=SubPathObject.getJSONArray("lane");// 노선 들어있는 어레이
                    JSONObject stopList=SubPathObject.getJSONObject("passStopList"); // 정류장 들어있는 오브젝트
                    JSONArray stationList=stopList.getJSONArray("stations"); // 거쳐야할 정류장 리스트어레이.

                    String startname=SubPathObject.getString("startName")+" 정류장->"; // 승차역 이름
                    String lanename=laneArray.getJSONObject(0).getString("busNo")+" 승차"; // 노선이름(부산 2호선)
                    String startnamelane=startname+lanename; // 정류장 및 승차 합치기.
                    temponoffList.add(startnamelane);

                    String stationString="";
                    for(int v=0;v<stationList.length();v++){ // 정류장 리스트 뽑기
                        stationString=stationString+stationList.getJSONObject(v).getString("stationName")
                                .toString()+"ᚔ"; // 정류장 번호
                    }
                    temponoffList.add(stationString);

                    String endname=SubPathObject.getString("endName")+" 하차"; // 하차역 이름
                    temponoffList.add(endname);

                } else if (SubPathObject.getString("trafficType").equals("3")) { // 도보 일때
                    String footdistance=SubPathObject.getString("distance")+"미터 도보 이동";
                    temponoffList.add(footdistance);

                }
            }
            // onoffList 의 승하차 정보를 차례대로 뽑는다
            count+=1; // 카운트를 증가.
            MakeTableRow(totalTime,totalDistance,payment,temponoffList);
        }
    }


}


