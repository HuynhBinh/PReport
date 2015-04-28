package com.em.preport;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

/**
 * Created by USER on 4/22/2015.
 */
public class StaticFunction
{
    public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static void sendNotification(Context ctx, String notificationDetails, String sub, int notifyId, int iconResource)
    {
        // Create an explicit content Intent that starts the main Activity.
        Intent notificationIntent = new Intent(ctx.getApplicationContext(), MapActivity.class);

        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(MainActivity.class);

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx);

        // Define the notification settings.
        builder.setSmallIcon(iconResource)
                // In a real app, you may want to use a library like Volley
                // to decode the Bitmap.
                //.setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_launcher)).
                .setContentTitle(notificationDetails).setContentText(sub).setContentIntent(notificationPendingIntent);

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        builder.setVibrate(new long[]{0, 10000, 500, 10000, 500, 10000});


        // Get an instance of the Notification manager
        NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        // Issue the notification
        mNotificationManager.notify(notifyId, builder.build());
    }
}
