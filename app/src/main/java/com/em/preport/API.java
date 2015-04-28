package com.em.preport;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.util.ArrayList;
import java.util.List;

import greendao.PLocation;

public class API
{

    private HttpClient client = new DefaultHttpClient();
    private HttpPost post = new HttpPost("http://www.freeappsforyou.info/csgt/apicall.php");
    HttpDelete delete = new HttpDelete("");
    HttpPut put = new HttpPut("");
    HttpParams httpParams = client.getParams();

    private int TIME_OUT = 30 * 1000;
    //HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
    //HttpConnectionParams.setSoTimeout(httpParams, TIME_OUT);

    public String getAllPLocation()
    {
        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("action", "get_all_report"));
        try
        {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            post.setEntity(entity);
            ResponseHandler<String> handler = new BasicResponseHandler();
            content = client.execute(post, handler);
            Log.d("message", content);
        } catch (Exception e)
        {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String reportPLocation(PLocation pLocation)
    {

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("action", "insert_report"));
        params.add(new BasicNameValuePair("lat", pLocation.getLatitude() + ""));
        params.add(new BasicNameValuePair("long", pLocation.getLongitude() + ""));
        params.add(new BasicNameValuePair("report_type", pLocation.getCSGTType()));
        params.add(new BasicNameValuePair("type", pLocation.getType()));
        params.add(new BasicNameValuePair("note", pLocation.getNote()));

        try
        {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            post.setEntity(entity);
            ResponseHandler<String> handler = new BasicResponseHandler();
            content = client.execute(post, handler);
            Log.d("message", content);
        } catch (Exception e)
        {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }


    public String uploadGCMRegisterIdToServer(String regId)
    {
        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("action", "insert_notification"));
        params.add(new BasicNameValuePair("register_id", regId));

        try
        {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            post.setEntity(entity);
            ResponseHandler<String> handler = new BasicResponseHandler();
            content = client.execute(post, handler);
            Log.d("message", content);
        } catch (Exception e)
        {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }


}
