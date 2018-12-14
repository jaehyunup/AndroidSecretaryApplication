package kimbiseo.deu.com.odsayapitest.odsayapitest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import kimbiseo.deu.com.odsayapitest.R;

public class GpsService extends Service {
    int startId;
    boolean isRunning;
    private DBHelper dbHelper = new DBHelper(GpsService.this, "gps_data", null, 1);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        dbHelper.readDB();
        dbHelper.createGPS();
        dbHelper.addGPS();

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "default";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("알람시작")
                    .setContentText("제발되어라제발.")
                    .setSmallIcon(R.mipmap.ic_launcher)

                    .build();

            startForeground(1, notification);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //return super.onStartCommand(intent, flags, startId);
        String getState = intent.getExtras().getString("state");

        Intent newIntent = new Intent(GpsService.this, insert_table.class);
        startActivity(newIntent);

        /*
        assert getState != null;
        switch (getState) {
            case "gps on":
                this.startId = 1;
                Intent newIntent = new Intent(GpsService.this, insert_table.class);
                startActivity(newIntent);
                break;
            case "gps off":
                this.startId = 0;
                Intent newIntent2 = new Intent(GpsService.this, insert_table2.class);
                startActivity(newIntent2);
                break;
            default:
                this.startId = 0;
                break;
        }
*/
        /*
        // 서비스 진행중 X , 알람음 시작 클릭
        if (!this.isRunning && startId == 1) {


            Intent newIntent = new Intent(GpsService.this, insert_table.class);
            startActivity(newIntent);
            this.isRunning = true;
            this.startId = 0;
        }

        // 서비스 진행중 O , 알람음 종료 버튼 클릭
        else if (this.isRunning && startId == 0) {
            Intent newIntent = new Intent(GpsService.this, insert_table2.class);
            startActivity(newIntent);
            this.isRunning = false;
            this.startId = 0;
        }

        // 서비스 진행중 X , 알람음 종료 버튼 클릭
        else if (!this.isRunning && startId == 0) {

            this.isRunning = false;
            this.startId = 0;

        }

        // 알람음 재생 O , 알람음 시작 버튼 클릭
        else if (this.isRunning && startId == 1) {

            this.isRunning = true;
            this.startId = 1;
        } else {
        }*/
        return START_NOT_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d("onDestory() 실행", "서비스 파괴");
    }

}
