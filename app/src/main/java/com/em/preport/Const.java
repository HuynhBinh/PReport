package com.em.preport;

/**
 * Created by USER on 4/18/2015.
 */
public class Const
{
    public static final long MY_LOCATION_ID = 1;

    public static final int DISTANCE_NOTIFY_IN_METTER = 500;
    public static final int MIN_DISTANCE_IN_METTER = 50;

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 30000;


    public static final String ACTION_LOAD_ALL_PLOC = "ACTION_LOAD_ALL_PLOC";
    public static final String ACTION_REPORT_PLOC = "ACTION_REPORT_PLOC";
    public static final String ACTION_SEND_GCM_REG_ID_TO_SERVER = "ACTION_SEND_GCM_REG_ID_TO_SERVER";

    public static final String RECEIVER_LOAD_ALL_PLOC_FINISH = "RECEIVER_LOAD_ALL_PLOC_FINISH";
    public static final String RECEIVER_REPORT_PLOC_FINISH = "RECEIVER_REPORT_PLOC_FINISH";
    public static final String RECEIVER_SEND_GCM_REG_ID_TO_SERVER_FINISH = "RECEIVER_SEND_GCM_REG_ID_TO_SERVER_FINISH";

    public static final String EXTRA_RESULT = "EXTRA_RESULT";
    public static final String EXTRA_RESULT_OK = "EXTRA_RESULT_OK";
    public static final String EXTRA_RESULT_FAIL = "EXTRA_RESULT_FAIL";
    public static final String EXTRA_RESULT_NO_INTERNET = "EXTRA_RESULT_NO_INTERNET";

    // In GCM, the Sender ID is a project ID that you acquire from the API
    public static final String PROJECT_NUMBER = "381428347477";

    public static final String GMAP_KEY = "AIzaSyBWoSIPn5dv0UO7cZv9wQU1YjFp9IcQsEc";

    public static final String EXTRA_MESSAGE = "message";

    public static final String GCM_NOTIFICATION = "GCM Notification";

    public static final String GCM_DELETED_MESSAGE = "Deleted messages on server: ";

    public static final String GCM_INTENT_SERVICE = "GcmIntentService";

    public static final String GCM_SEND_ERROR = "Send error: ";

    public static final String GCM_RECEIVED = "Received: ";
    // end GCM setup

}
