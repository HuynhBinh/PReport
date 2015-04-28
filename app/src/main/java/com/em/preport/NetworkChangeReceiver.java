package com.em.preport;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

/**
 * Created by USER on 4/24/2015.
 */
public class NetworkChangeReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(final Context context, Intent intent)
    {
        if (isOnline(context))
        {
            Handler han = new Handler();
            han.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    Intent intentsv = new Intent(Const.ACTION_REPORT_PLOC, null, context, APIIntentService.class);
                    context.startService(intentsv);
                }
            }, 5000);

        }

    }

    public boolean isOnline(Context context)
    {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in air plan mode it will be null
        return (netInfo != null && netInfo.isConnected());

    }
}
