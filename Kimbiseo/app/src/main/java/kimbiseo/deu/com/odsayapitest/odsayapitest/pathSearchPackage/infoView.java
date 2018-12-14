package kimbiseo.deu.com.odsayapitest.odsayapitest.pathSearchPackage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import kimbiseo.deu.com.odsayapitest.R;

public class infoView extends AppCompatActivity {
    TextView laneTextView[]=new TextView[21];
    TableRow row[]=new TableRow[21];
    TableRow down[]=new TableRow[21];
    ImageView ico[]=new ImageView[21];
    int onflag=0;
    int Listsize=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_view);
        ArrayList<String> SubPathList=(ArrayList<String>)getIntent().getSerializableExtra("SubPathList");
        for(int i=0;i<21;i++){
            int k = getResources().getIdentifier("Lane"+i, "id", getPackageName()); // 노선 텍스트뷰 가져오기
            int t = getResources().getIdentifier("TableRow"+i, "id", getPackageName()); // 정류장 텍스트뷰
            int d = getResources().getIdentifier("center"+i, "id", getPackageName()); // 화살표 텍스트뷰
            int c = getResources().getIdentifier("iconview"+i, "id", getPackageName()); // 텍스트 앞 아이콘
            laneTextView[i]=findViewById(k);
            row[i]=findViewById(t);
            down[i]=findViewById(d);
            ico[i]=findViewById(c);
            down[i].setVisibility(View.GONE);
            row[i].setVisibility(View.GONE);
        }
        for(int j=0;j<SubPathList.size();j++){
            String temp=SubPathList.get(j);
            if (temp.contains("ᚔ")) {
                laneTextView[j].setText(temp.substring(0, temp.length() - 1));
                row[j].setVisibility(View.VISIBLE);
                ico[j].setImageResource(R.drawable.ic_twotone_directions_24px);
            }
            else if(temp.contains("정류장")){ // 버스일때 경로
                laneTextView[j].setText(temp);
                row[j].setVisibility(View.VISIBLE);
                ico[j].setImageResource(R.drawable.ic_twotone_directions_bus_24px);
            }
            else if(temp.contains("도보")){// 도보일때
                laneTextView[j].setText(temp);
                row[j].setVisibility(View.VISIBLE);
                ico[j].setImageResource(R.drawable.ic_twotone_directions_run_24px);

            }
            else{
                laneTextView[j].setText(temp);
                row[j].setVisibility(View.VISIBLE);
            }
            if(j<SubPathList.size()-1){
                down[j].setVisibility(View.VISIBLE);
            }
        }
    }
}
