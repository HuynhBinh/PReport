package com.em.preport;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.List;

import greendao.MyLocation;
import greendao.PLocation;
import greendao.SharePrefs;


public class MainActivity extends ActionBarActivity
{

    Button btnReport;

    private BroadcastReceiver activityReceiver = new BroadcastReceiver()
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getAction().equalsIgnoreCase(Const.RECEIVER_LOAD_ALL_PLOC_FINISH))
            {
                String result = intent.getStringExtra(Const.EXTRA_RESULT);
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        //
        if (activityReceiver != null)
        {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Const.RECEIVER_LOAD_ALL_PLOC_FINISH);
            registerReceiver(activityReceiver, intentFilter);
        }


        SharePrefs sharePrefs = LocationController.getSharePrefs(this, "FirstUseApp");
        if (sharePrefs == null)
        {
            Intent intent = new Intent(Const.ACTION_LOAD_ALL_PLOC, null, this, APIIntentService.class);
            startService(intent);
        }


        // use this to start and trigger a service
        Intent i = new Intent(this, MyLocationService.class);
        i.addCategory(MyLocationService.TAG);
        this.startService(i);


        PlayServicesHelper playServicesHelper = new PlayServicesHelper(MainActivity.this);


    }

    @Override
    protected void onDestroy()
    {
        if (activityReceiver != null)
        {
            this.unregisterReceiver(activityReceiver);
        }
        super.onDestroy();
    }

    private void initView()
    {
        btnReport = (Button) findViewById(R.id.btnReport);
    }

    public void onBtnMapClick(View view)
    {
        Intent in = new Intent(this, MapActivity.class);
        startActivity(in);
    }

    public void onBtnReportClick(View view)
    {

        SharePrefs sharePrefs = LocationController.getSharePrefs(MainActivity.this, "btnReportClickTime");
        long previousTime = 0;
        try
        {
            previousTime = Long.parseLong(sharePrefs.getValue());

        } catch (Exception ex)
        {

        }

        if (((System.currentTimeMillis() - previousTime) / 1000) < 30)
        {
            Toast.makeText(MainActivity.this, "You can only report after 30s", Toast.LENGTH_LONG).show();
            return;
        }


        final MyLocation mLoc = LocationController.getMyLocationById(MainActivity.this, Const.MY_LOCATION_ID);


        if (mLoc != null)
        {

            // show pop up ask user select type and notes
            // custom dialog
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.popup_report);

            final EditText edtNotes = (EditText) dialog.findViewById(R.id.edtNotes);
            final RadioButton rdoOneTime = (RadioButton) dialog.findViewById(R.id.rdoOneTime);
            final RadioButton rdoManyTime = (RadioButton) dialog.findViewById(R.id.rdoManyTime);
            Button btnConfirmReport = (Button) dialog.findViewById(R.id.btnConfirmReport);

            btnConfirmReport.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    boolean isReportLocationIn50Met = false;
                    String strNote = edtNotes.getText().toString().trim();
                    boolean isOneTime = rdoOneTime.isChecked();
                    boolean isManyTime = rdoManyTime.isChecked();

                    String strReportType = "0";
                    if (isOneTime)
                    {
                        strReportType = "1";
                    }

                    if (isManyTime)
                    {
                        strReportType = "2";
                    }


                    List<PLocation> listPLocation = LocationController.getAllPLocation(MainActivity.this);
                    //Location loc = LocationIntentService.mCurrentLocation;

                    for (PLocation pLoc : listPLocation)
                    {
                        float result[] = new float[3];
                        Location.distanceBetween(mLoc.getLatitude(), mLoc.getLongitude(), pLoc.getLatitude(), pLoc.getLongitude(), result);
                        if (result[0] <= Const.MIN_DISTANCE_IN_METTER)
                        {
                            isReportLocationIn50Met = true;
                            pLoc.setIsReported(false);
                            pLoc.setNote(strNote);
                            pLoc.setCSGTType(strReportType);
                            pLoc.setIsEnter(false);
                            LocationController.updatePLocation(MainActivity.this, pLoc);

                            Toast.makeText(MainActivity.this, "Near other", Toast.LENGTH_LONG).show();
                            break;
                        }
                    }


                    if (isReportLocationIn50Met == false)
                    {

                        PLocation pLoc = new PLocation();
                        pLoc.setLatitude(mLoc.getLatitude());
                        pLoc.setLongitude(mLoc.getLongitude());
                        pLoc.setCSGTType(strReportType);
                        pLoc.setType("0");
                        pLoc.setIsReported(false);
                        pLoc.setNote(strNote);
                        pLoc.setIsEnter(false);

                        LocationController.insertPLocation(MainActivity.this, pLoc);

                        Toast.makeText(MainActivity.this, "Thanks for your report!", Toast.LENGTH_LONG).show();
                    }

                    //
                    Intent intent = new Intent(Const.ACTION_REPORT_PLOC, null, MainActivity.this, APIIntentService.class);
                    startService(intent);

                    //disable button
                    SharePrefs sharePrefs1 = new SharePrefs();
                    sharePrefs1.setKey("btnReportClickTime");
                    sharePrefs1.setValue(System.currentTimeMillis() + "");

                    LocationController.insertOrUpdateSharePrefs(MainActivity.this, sharePrefs1);

                    dialog.dismiss();
                }
            });

            dialog.show();
            //
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
