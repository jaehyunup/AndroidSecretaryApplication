package kimbiseo.deu.com.odsayapitest.odsayapitest.alarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import kimbiseo.deu.com.odsayapitest.R;
import kimbiseo.deu.com.odsayapitest.odsayapitest.fragment.SettingFragment;
import kimbiseo.deu.com.odsayapitest.odsayapitest.mainPackage.MainPage;

public class AlarmPage extends Activity
{
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_page);

        this.context = this;

        final Intent intent = new Intent(this.context, AlarmReceiver.class);

        Button btnStop = (Button) findViewById(R.id.btnStop);

        // 알람 중지 버튼 클릭 시
        btnStop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                offAndSetAlarm(intent);
            }
        });
    }

    public void offAndSetAlarm(Intent intent)
    {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        pendingIntent = PendingIntent.getBroadcast(AlarmPage.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        intent.putExtra("state", "alarm off");

        sendBroadcast(intent);

        // 다음 알람 설정
        ((MainPage)MainPage.fragmentContext).setNextAlarm();

        finish();
    }


    // 뒤로가기 버튼 터치 시
    @Override
    public void onBackPressed()
    {


        super.onBackPressed();
    }
}
