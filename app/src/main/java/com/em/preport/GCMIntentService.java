package com.em.preport;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import greendao.PLocation;

public class GCMIntentService extends IntentService
{
    private static final String TAG = GCMIntentService.class.getSimpleName();

    Handler han;

    public static final String INSERT = "insert";
    public static final String UPDATE = "update";
    public static final String DELETE = "delete";

    @Override
    public void onCreate()
    {
        super.onCreate();
        han = new Handler();
    }

    public GCMIntentService()
    {

        super(Const.GCM_INTENT_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {

        Log.i(TAG, "new push");

        Bundle extras = intent.getExtras();
        GoogleCloudMessaging googleCloudMessaging = GoogleCloudMessaging.getInstance(this);

        String messageType = googleCloudMessaging.getMessageType(intent);

        if (!extras.isEmpty())
        {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType))
            {
                processNotification(Const.GCM_SEND_ERROR, extras);
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType))
            {
                processNotification(Const.GCM_DELETED_MESSAGE, extras);
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType))
            {
                // Post notification of received message.
                processNotification(Const.GCM_RECEIVED, extras);
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }


    private void processNotification(String type, final Bundle extras)
    {
        String title = extras.getString("title");
        long svId = Long.parseLong(extras.getString("id"));
        double lat = Double.parseDouble(extras.getString("lat"));
        double log = Double.parseDouble(extras.getString("long"));
        String rType = extras.getString("report_type");
        String typee = extras.getString("type");
        int count = Integer.parseInt(extras.getString("count"));
        String updateTime = extras.getString("update_time");
        String note = extras.getString("note");

        PLocation pLoc = new PLocation();
        pLoc.setSvId(svId);
        pLoc.setLatitude(lat);
        pLoc.setLongitude(log);
        pLoc.setCSGTType(rType);
        pLoc.setType(typee);
        pLoc.setCount(count);
        pLoc.setTimeStamp(updateTime);
        pLoc.setNote(note);


        if (title.equalsIgnoreCase(INSERT))
        {

            LocationController.updatePLocationWithSVID(GCMIntentService.this, pLoc);


        } else if (title.equalsIgnoreCase(UPDATE))
        {

            LocationController.updatePLocationWithSVID(GCMIntentService.this, pLoc);

        } else if (title.equalsIgnoreCase(DELETE))
        {

        } else
        {

        }

        han.post(new Runnable()
        {
            @Override
            public void run()
            {
                Toast.makeText(getApplicationContext(), extras.toString(), Toast.LENGTH_LONG).show();
            }
        });


    }


}
