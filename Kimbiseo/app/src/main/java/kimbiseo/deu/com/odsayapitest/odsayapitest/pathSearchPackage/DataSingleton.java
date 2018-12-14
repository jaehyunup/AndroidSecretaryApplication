package kimbiseo.deu.com.odsayapitest.odsayapitest.pathSearchPackage;

import com.nhn.android.maps.maplib.NGeoPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public enum DataSingleton {
    /*클래스 전체에 동기화된 데이터를 이용하기위한 Singleton 패턴 enum*/
    INSTANCE;
    ArrayList<String> SearchInfoData = new ArrayList<>(); // 검색 정보  저장
    ArrayList<JSONObject> BigPathTotal=new ArrayList<>();// 검색결과 큰경로 전체결과
    ArrayList<JSONObject> BigPathBus= new ArrayList<>(); // 검색결과의 큰경로 저장(버스일때)
    ArrayList<JSONObject> BigPathSubway= new ArrayList<>(); // 검색결과의 큰경로 저장 (지하철일때)
    ArrayList<JSONObject> BigPathBusSubway= new ArrayList<>(); // 검색결과의 큰경로 저장 (버스+지하철 일때)

    ArrayList<NGeoPoint> PathMapObj= new ArrayList<NGeoPoint>(); // 각 경로별 mapObj 저장.


    String startPointx="129.0578444";
    String startPointy="35.1578157";
    String stopPointx="129.0319021";
    String stopPointy="35.1429679";

    public Double getstopPointx(){
        return Double.parseDouble(stopPointx);
    }

    public Double getstopPointy(){
        return Double.parseDouble(stopPointy);
    }

    public void setStopPointx(String stopPointx) {
        this.stopPointx = stopPointx;
    }

    public void setStartPointx(String startPointx) {
        this.startPointx = startPointx;
    }

    public void setStartPointy(String startPointy) {
        this.startPointy = startPointy;
    }

    public void setStopPointy(String stopPointy) {
        this.stopPointy = stopPointy;
    }


    public void setInfoData(String[] s){ //들어온 크기만큼 검색정보 데이터를 저장
        // 각인덱스별 정보
        // 도시간 직통 탐색 유무 (다른 도시로 길찾기할때)
        // 결과구분 , 0=도시내,1=도시간 환승,2=도시간 직통
        // 검색된 길중 지하철로만 갈수 있는길 개수
        // 검색된 길중 버스로만 갈수 있는 길 개수
        // 검색된 길중 버스+지하철로 갈수 있는 길 개수
        // 출,도착지간 직선거리
        for(int i=0;i<s.length;i++){
            SearchInfoData.add(s[i]);
        }
    }

    public void setSearchbigPathArray(JSONArray patharray) throws JSONException { // 첫검색의 큰경로 저장
        // 큰 경로검색결과 인덱스 저장
        for (int i = 0; i < patharray.length(); i++) {
            JSONObject indexObject=patharray.getJSONObject(i);
            String pathType = indexObject.getString("pathType");
            BigPathTotal.add(indexObject); // 전체경로에는 모두 저장
            if (pathType.equals("1")) { //이동타입이 지하철인 오브젝트만 저장
                BigPathSubway.add(indexObject);
            }
            else if (pathType.equals("2")) { // 이동타입이 버스인 오브젝트만 저장
                BigPathBus.add(indexObject);
            }
            else if (pathType.equals("3")) { // 이동타입이 버스+ 지하철인 오브젝트만 저장
                BigPathBusSubway.add(indexObject);
            }
        }
    }

    public ArrayList<JSONArray> getSubPathList(ArrayList<JSONObject> PathArray){ // 경로 전체결과를 각 오브젝트별로 테이블화 하는 메서드
                                                    // 인스턴스의 인덱스는 경로검색의 해당 인덱스 오브젝트를 가르킨다
                                                    // big path 가 인자로 들어간다고 생각
        ArrayList<JSONArray> SubPathList=new ArrayList<JSONArray>();
        JSONObject Path;
        JSONArray SubPath;
        for(int i=0;i<PathArray.size();i++){
            try {
                Path=PathArray.get(i);
                SubPath=Path.getJSONArray("subPath");
                SubPathList.add(SubPath);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return SubPathList;
    }

    public void setPoint(NGeoPoint pathPoint){
        PathMapObj.add(pathPoint);
    }
    public void initPoint(){
        PathMapObj=new ArrayList<NGeoPoint>();
    }


}


