package kimbiseo.deu.com.odsayapitest.odsayapitest.mainPackage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import kimbiseo.deu.com.odsayapitest.R;

public class Start extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(), 3000);
    }

    private class splashhandler implements Runnable
    {
        public void run()
        {
            //여기서 처음실행여부판단 해야하나
            // 첫실행이면 시간표 입력 페이지로




            // 첫 실행 아닐때 바로 메인페이지 가도록
            startActivity(new Intent(getApplication(), MainPage.class)); //로딩이 끝난 후, 메인 페이지로 이동
            Start.this.finish(); // 로딩페이지 Activity stack에서 제거
        }
    }

    @Override
    public void onBackPressed()
    {
        //초반 플래시 화면에서 넘어갈때 뒤로가기 버튼 못누르게 함
    }

}
