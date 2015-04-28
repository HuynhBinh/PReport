package com.em.preport;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import greendao.MyLocation;

/**
 * Created by USER on 4/18/2015.
 */
public class MyLocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener//, LocationListener
{

    protected static final String TAG = "com.em.loc.tag.emsbilo.ser.sticky";

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;

    /**
     * Represents a geographical location.
     */
    //public static Location mCurrentLocation;

    /**
     * Time when the location was updated represented as a String.
     */
    //protected String mLastUpdateTime;

    public int currentRequestTime = 30;

    PowerManager.WakeLock wakeLock = null;

    //public static List<PLocation> listPLocation = new ArrayList<PLocation>();
    //public static List<PLocation> listFiredLocation = new ArrayList<PLocation>();


    private Intent mIntentService;
    private PendingIntent mPendingIntent;


    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        buildGoogleApiClient(Const.UPDATE_INTERVAL_IN_MILLISECONDS);
        mGoogleApiClient.connect();
        //StaticFunction.sendNotification(MyLocationService.this, "onCreate", "connect", 100000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {

        mIntentService = new Intent(this, LocationIntentService.class);
        mPendingIntent = PendingIntent.getService(this, 1001, mIntentService, PendingIntent.FLAG_CANCEL_CURRENT);

        if (mGoogleApiClient == null)
        {
            buildGoogleApiClient(Const.UPDATE_INTERVAL_IN_MILLISECONDS);
            mGoogleApiClient.connect();
        }

        return START_STICKY;
    }


    @Override
    public void onDestroy()
    {
        //StaticFunction.sendNotification(MyLocationService.this, "onDestroy", "disconnect", 123000);

        /*if (mGoogleApiClient.isConnected())
        {
            stopLocationUpdates();
            mGoogleApiClient.disconnect();
        }*/

        /*if (wakeLock != null)
        {
            wakeLock.release();
        }*/

        super.onDestroy();
    }

    private void initWakeLock()
    {
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        if (wakeLock == null)
        {
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "locserwakelocktag1");
        }

        if (!wakeLock.isHeld())
        {
            wakeLock.acquire();
        }
    }

    protected synchronized void buildGoogleApiClient(long requestTimeInterval)
    {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        createLocationRequest(requestTimeInterval);
    }

    protected void createLocationRequest(long requestTimeInterval)
    {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(requestTimeInterval);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(requestTimeInterval);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates()
    {
        // use location listener
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        // use pending intent
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, mPendingIntent);
    }

    protected void stopLocationUpdates()
    {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.

        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }


    @Override
    public void onConnected(Bundle connectionHint)
    {
        Log.i(TAG, "Connected to GoogleApiClient");

        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in the Bundle and check for it in onCreate(). We
        // do not request it again unless the user specifically requests location updates by pressing
        // the Start Updates button.
        //
        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        if (location != null)
        {
            MyLocation myLoc = new MyLocation();
            myLoc.setId(Const.MY_LOCATION_ID);
            myLoc.setLatitude(location.getLatitude());
            myLoc.setLongitude(location.getLongitude());
            LocationController.insertOrUpdateMyLocation(MyLocationService.this, myLoc);
        }

        startLocationUpdates();
    }

    /*@Override
    public void onLocationChanged(Location location)
    {
       *//* listPLocation = LocationController.getAllPLocation(MyLocationService.this);


        mCurrentLocation = location;

        float result[] = new float[3];

        for (int i = 0; i < listPLocation.size(); i++)
        {
            PLocation pLocation = listPLocation.get(i);

            Location.distanceBetween(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), pLocation.getLatitude(), pLocation.getLongitude(), result);

            // enter location
            if (result[0] <= Const.DISTANCE_NOTIFY_IN_METTER)
            {
                if (!listFiredLocation.contains(pLocation))
                {
                    sendNotification("Enter " + pLocation.getNote(), "kc " + result[0], i);
                    listFiredLocation.add(pLocation);
                }
            } else // exit location
            {
                if (listFiredLocation.contains(pLocation))
                {
                    sendNotification("Exit " + pLocation.getNote(), "kc " + result[0], i + 1000);
                    listFiredLocation.remove(pLocation);
                }
            }
        }*//*

    }*/

    @Override
    public void onConnectionSuspended(int cause)
    {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result)
    {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


}
