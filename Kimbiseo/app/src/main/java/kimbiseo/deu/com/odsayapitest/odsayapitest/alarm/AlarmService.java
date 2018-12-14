package kimbiseo.deu.com.odsayapitest.odsayapitest.alarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import kimbiseo.deu.com.odsayapitest.R;
import kimbiseo.deu.com.odsayapitest.odsayapitest.mainPackage.MainPage;


public class AlarmService extends Service
{
    MediaPlayer mediaPlayer;
    String alarmSound;
    int startId;
    boolean isRunning;

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        //알림창
        if (Build.VERSION.SDK_INT >= 26)
        {
            String CHANNEL_ID = "default";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();

            startForeground(1, notification);
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        String getState = intent.getExtras().getString("state");

        assert getState != null;

        // 전달받은 알람 정보 체크
        switch (getState)
        {
            case "alarm on":
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                break;

            // 소리값 체크. 알람 실행 X
            case "clock":
                alarmSound = "clock";
                startId = -1;
                break;
            case "army":
                alarmSound = "army";
                startId = -1;
                break;
            case "nature":
                alarmSound = "nature";
                startId = -1;
                break;
            default:
                alarmSound = "default";
                startId = -1;
                break;
        }

        // 알람 정지 중 알람 시작 시
        if(!this.isRunning && startId == 1)
        {
            // 기본 알람 소리
            if(alarmSound == null)
                alarmSound = "default";

            // 알람 소리 설정
            switch(alarmSound)
            {
                case "clock":
                    mediaPlayer = MediaPlayer.create(this, R.raw.alarm1);
                    break;
                case "army":
                    mediaPlayer = MediaPlayer.create(this, R.raw.wakeup);
                    break;
                case "nature":
                    mediaPlayer = MediaPlayer.create(this, R.raw.flute);
                    break;
                default:
                    mediaPlayer = MediaPlayer.create(this, R.raw.alarm1);
                    break;
            }

            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            Toast.makeText(MainPage.fragmentContext, "알람 시작", Toast.LENGTH_LONG).show();

            //알람페이지 이동
            Intent page_intent = new Intent(getApplicationContext(), AlarmPage.class);
            page_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(page_intent);

            this.isRunning = true;
            this.startId = 0;
        }

        // 알람 재생 중 알람 종료 시
        else if(this.isRunning && startId == 0)
        {
            Toast.makeText(MainPage.fragmentContext, "알람 종료", Toast.LENGTH_LONG).show();

            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();

            this.isRunning = false;
            this.startId = 0;
        }

        // 알람 정지 중 알람 종료 시
        else if(!this.isRunning && startId == 0)
        {

            this.isRunning = false;
            this.startId = 0;

        }

        // 알람 재생 중 알람 시작 시
        else if(this.isRunning && startId == 1)
        {

            this.isRunning = true;
            this.startId = 1;
        }

        return START_REDELIVER_INTENT;

    }



    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Toast.makeText(MainPage.fragmentContext, "서비스 종료", Toast.LENGTH_LONG).show();
        Log.d("onDestory() 실행", "서비스 파괴");

    }
}
