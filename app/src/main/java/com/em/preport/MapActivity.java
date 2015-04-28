package com.em.preport;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import greendao.MyLocation;
import greendao.PLocation;

/**
 * Created by USER on 4/22/2015.
 */
public class MapActivity extends Activity implements OnMapReadyCallback
{
    public List<PLocation> listPLocation = new ArrayList<PLocation>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map)
    {

        final MyLocation mLoc = LocationController.getMyLocationById(MapActivity.this, Const.MY_LOCATION_ID);

        if (mLoc == null)
        {
            return;
        }

        LatLng currentPos = new LatLng(mLoc.getLatitude(), mLoc.getLongitude());

        map.setMyLocationEnabled(true);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPos, 14), 1000, null);//.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPos, 14));
        //map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.emo_im_cool)).anchor(0.5f, 0.5f).title("me").position(currentPos));

        listPLocation = LocationController.getAllPLocation(MapActivity.this);

        for (int i = 0; i < listPLocation.size(); i++)
        {
            PLocation pLocation = listPLocation.get(i);
            map.addMarker(new MarkerOptions()//.icon(BitmapDescriptorFactory..defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title(pLocation.getId() + "").snippet(pLocation.getNote()).position(new LatLng(pLocation.getLatitude(), pLocation.getLongitude())));
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.police)).title(pLocation.getId() + "").snippet(pLocation.getNote()).position(new LatLng(pLocation.getLatitude(), pLocation.getLongitude())));

        }

    }

    private void loadPLocation()
    {

    }

}

