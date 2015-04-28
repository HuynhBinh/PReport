package com.em.preport;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

/**
 * Created by USER on 4/24/2015.
 */
public class BootCompleteReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(final Context context, Intent intent)
    {
        Handler han = new Handler();
        han.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                // use this to start and trigger a service
                Intent i = new Intent(context, MyLocationService.class);
                i.addCategory(MyLocationService.TAG);
                context.startService(i);
                Toast.makeText(context, "BootCompleteReceiver : MyLocationService", Toast.LENGTH_LONG).show();
            }
        }, 7000);

    }
}
