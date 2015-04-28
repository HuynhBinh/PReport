package com.em.preport;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderApi;

import java.util.ArrayList;
import java.util.List;

import greendao.MyLocation;
import greendao.PLocation;


/**
 * Created by USER on 4/24/2015.
 */
public class LocationIntentService extends IntentService
{

    //public static Location mCurrentLocation;

    public static List<PLocation> listPLocation = new ArrayList<PLocation>();

    //public static List<PLocation> listFiredLocation = new ArrayList<PLocation>();

    public LocationIntentService()
    {
        super(LocationIntentService.class.getName());

    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return super.onBind(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        Location location = intent.getParcelableExtra(FusedLocationProviderApi.KEY_LOCATION_CHANGED);
        if (location != null)
        {
            Log.i("TAG", "onHandleIntent " + location.getLatitude() + "," + location.getLongitude());

            listPLocation = LocationController.getAllPLocation(LocationIntentService.this);


            MyLocation myLoc = new MyLocation();
            myLoc.setId(Const.MY_LOCATION_ID);
            myLoc.setLatitude(location.getLatitude());
            myLoc.setLongitude(location.getLongitude());
            LocationController.insertOrUpdateMyLocation(LocationIntentService.this, myLoc);


            float result[] = new float[3];

            for (int i = 0; i < listPLocation.size(); i++)
            {
                PLocation pLocation = listPLocation.get(i);

                Location.distanceBetween(location.getLatitude(), location.getLongitude(), pLocation.getLatitude(), pLocation.getLongitude(), result);

                // enter location
                if (result[0] <= Const.DISTANCE_NOTIFY_IN_METTER)
                {
                    if (pLocation.getIsEnter() == false)
                    {
                        StaticFunction.sendNotification(LocationIntentService.this, pLocation.getNote(), "kc " + result[0], i, R.drawable.enter);
                        pLocation.setIsEnter(true);
                        LocationController.updatePLocation(LocationIntentService.this, pLocation);
                        //listFiredLocation.add(pLocation);
                    }
                } else // exit location
                {
                    if (pLocation.getIsEnter() == true)
                    {
                        StaticFunction.sendNotification(LocationIntentService.this, pLocation.getNote(), "kc " + result[0], i + 1000, R.drawable.exit);
                        pLocation.setIsEnter(false);
                        LocationController.updatePLocation(LocationIntentService.this, pLocation);
                    }
                }
            }

        }

    }
}
