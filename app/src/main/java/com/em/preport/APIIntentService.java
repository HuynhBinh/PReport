package com.em.preport;

import android.app.IntentService;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import greendao.PLocation;
import greendao.SharePrefs;

/**
 * Created by USER on 4/22/2015.
 */
public class APIIntentService extends IntentService
{


    public APIIntentService()
    {
        super(APIIntentService.class.getName());


    }


    @Override
    protected void onHandleIntent(Intent intent)
    {
        String action = intent.getAction();

        // if command load -> load data
        if (action.equals(Const.ACTION_LOAD_ALL_PLOC))
        {
            if (StaticFunction.isNetworkAvailable(APIIntentService.this))
            {
                boolean isSuccess = loadAllPLocation();
                if (isSuccess)
                {
                    broadCastLoadNewCardFinish(Const.EXTRA_RESULT_OK);

                } else
                {
                    broadCastLoadNewCardFinish(Const.EXTRA_RESULT_FAIL);
                }

            } else
            {
                broadCastLoadNewCardFinish(Const.EXTRA_RESULT_NO_INTERNET);
            }


        } else if (action.equals(Const.ACTION_REPORT_PLOC))
        {
            reportPLocation();

        } else if (action.equals(Const.ACTION_SEND_GCM_REG_ID_TO_SERVER))
        {

        }

    }


    private void reportPLocation()
    {
        List<PLocation> listPLocThatNotYetUpload = LocationController.getPLocationThatIsNotSyncYet(this);

        API api = new API();

        if (listPLocThatNotYetUpload != null)
        {
            if (listPLocThatNotYetUpload.size() > 0)
            {
                for (PLocation pLoc : listPLocThatNotYetUpload)
                {
                    JSONObject jsonObject;
                    String result = api.reportPLocation(pLoc);
                    try
                    {
                        jsonObject = new JSONObject(result);
                        String strMessage = jsonObject.optString("message");
                        if (strMessage.equalsIgnoreCase("1"))
                        {
                            long svId = jsonObject.optLong("id");
                            int count = jsonObject.optInt("count");
                            String updateTime = jsonObject.optString("update_time");
                            pLoc.setSvId(svId);
                            pLoc.setIsReported(true);
                            pLoc.setCount(count);
                            pLoc.setTimeStamp(updateTime);
                            LocationController.updatePLocation(APIIntentService.this, pLoc);
                        }

                    } catch (Exception ex)
                    {

                    }


                }
            }
        }
    }


    private boolean loadAllPLocation()
    {
        boolean isSuccess = false;
        API api = new API();
        String result = api.getAllPLocation();
        JSONArray jsonResponse;

        List<PLocation> listPLocations = new ArrayList<PLocation>();

        try
        {
            jsonResponse = new JSONArray(result);

            for (int i = 0; i < jsonResponse.length(); i++)
            {
                JSONObject jsonPLoc = jsonResponse.getJSONObject(i);

                long svID = jsonPLoc.optLong("id");
                double lat = jsonPLoc.optDouble("lat");
                double log = jsonPLoc.optDouble("long");
                int pType = jsonPLoc.optInt("report_type");
                int type = jsonPLoc.optInt("type");
                int count = jsonPLoc.optInt("count");
                String updateTime = jsonPLoc.optString("update_time");
                String notes = jsonPLoc.optString("note");

                PLocation pLoc = new PLocation();
                pLoc.setSvId(svID);
                pLoc.setLatitude(lat);
                pLoc.setLongitude(log);
                pLoc.setCSGTType(pType + "");
                pLoc.setType(type + "");
                pLoc.setCount(count);
                pLoc.setTimeStamp(updateTime);
                pLoc.setNote(notes);
                pLoc.setIsEnter(false);

                listPLocations.add(pLoc);

                //LocationController.insertPLocation(this, pLoc);

            }

            LocationController.insertPLocationInBulk(APIIntentService.this, listPLocations);

            isSuccess = true;

        } catch (Exception ex)
        {
            isSuccess = false;
        }

        if (isSuccess)
        {

            SharePrefs sharePrefs = new SharePrefs();
            sharePrefs.setKey("FirstUseApp");
            sharePrefs.setValue("false");
            LocationController.insertOrUpdateSharePrefs(APIIntentService.this, sharePrefs);
        }

        return isSuccess;

    }

    private void broadCastLoadNewCardFinish(String result)
    {

        Intent i = new Intent();
        i.setAction(Const.RECEIVER_LOAD_ALL_PLOC_FINISH);
        i.putExtra(Const.EXTRA_RESULT, result);
        sendBroadcast(i);
    }
}
