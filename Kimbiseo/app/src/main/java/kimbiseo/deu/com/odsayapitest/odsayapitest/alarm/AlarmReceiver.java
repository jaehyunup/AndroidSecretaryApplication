package kimbiseo.deu.com.odsayapitest.odsayapitest.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import kimbiseo.deu.com.odsayapitest.odsayapitest.mainPackage.MainPage;

public class AlarmReceiver extends BroadcastReceiver
{
    Context context;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        this.context = context;

        String state = intent.getExtras().getString("state");

        System.out.println("받은 인텐트 메세지 : " + state);

        Intent service_intent = new Intent(context, AlarmService.class);

        service_intent.putExtra("state", state);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            this.context.startForegroundService(service_intent);
        }
        else
        {
            this.context.startService(service_intent);
        }
    }
}