package com.example.pending_check_service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

import static android.content.ContentValues.TAG;

public class PendingCheckService extends Service {


    public static final String INTENT_TIME = "INTENT_TIME";
    public static final String INTENT_CART_ID = "INTENT_CART_ID";
    public static final String INTENT_TOKEN = "INTENT_TOKEN";
    public static final String INTENT_TITLE = "INTENT_TITLE";
    public static final String INTENT_DESCRIPTION = "INTENT_DESCRIPTION";
    public static final String INTENT_CANCEL_TITLE = "INTENT_CANCEL_TITLE";
    public static final String INTENT_CANCEL_DESCRIPTION = "INTENT_CANCEL_DESCRIPTION";
    public static final String INTENT_URL = "INTENT_URL";
    public static final String INTENT_ORDER_URL = "INTENT_ORDER_URL";
    public static final String INTENT_ORDER_ID = "INTENT_ID";

    private PluginNotificationManager notificationManager;
    private int i = 0;
    private int time;
    private String cardId;
    private String token;
    private String title;
    private String description;
    private String cancelTitle;
    private String cancelDescription;
    private String url;
    private String orderUrl;
    private String orderId;

    private int count = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        time = intent.getIntExtra(INTENT_TIME, 60);
        cardId = intent.getStringExtra(INTENT_CART_ID);
        token = intent.getStringExtra(INTENT_TOKEN);
        title = intent.getStringExtra(INTENT_TITLE);
        description = intent.getStringExtra(INTENT_DESCRIPTION);
        cancelTitle = intent.getStringExtra(INTENT_CANCEL_TITLE);
        cancelDescription = intent.getStringExtra(INTENT_CANCEL_DESCRIPTION);
        url = intent.getStringExtra(INTENT_URL);
        orderUrl = intent.getStringExtra(INTENT_ORDER_URL);
        orderId = intent.getStringExtra(INTENT_ORDER_ID);


        notificationManager = new PluginNotificationManager(this);
        notificationManager.buildNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, PluginNotificationManager.CHANNEL_ID);
        builder.setSmallIcon(R.drawable.icon)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        startForeground(1, builder.build());
        checkForOrderStatus();
        return START_STICKY;
    }

    private void checkForOrderStatus() {
        final Handler handler = new Handler(Looper.getMainLooper());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showOrderRejectedNotification();
            }
        }, time * 1000);
    }

    private void showOrderRejectedNotification() {

        final Context context = this;
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                HttpsURLConnection urlConnection = null;
                try {
                    urlConnection = (HttpsURLConnection) new URL(orderUrl + orderId + "?api_token="+token+"&with=user;foodOrders;foodOrders.food;orderStatus;deliveryAddress").openConnection();

                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    Scanner s = new Scanner(in).useDelimiter("\\A");
                    String result = s.hasNext() ? s.next() : "";
                    Log.d(TAG, "showOrderRejectedNotification: ============= " + result);
                    JSONObject jsonObject = new JSONObject(result);
                    int status = jsonObject.getJSONObject("data").getInt("order_status_id");

                    if(status == 1) {
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, PluginNotificationManager.CHANNEL_ID);
                        builder.setSmallIcon(R.drawable.icon)
                                .setContentTitle(cancelTitle)
                                .setContentText(cancelDescription)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                        notificationManager.getNotificationManager().notify(2, builder.build());

                        urlConnection = (HttpsURLConnection) new URL(url + cardId + "?api_token=" + token).openConnection();
                        urlConnection.setDoOutput(true);
                        urlConnection.setRequestProperty("content-type", "application/json");
                        urlConnection.setRequestMethod("DELETE");
                        InputStream in2 = new BufferedInputStream(urlConnection.getInputStream());
                        Log.d(TAG, "showOrderRejectedNotification: ============= " + in2.read());
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
        });


        stopForeground(true);
        stopSelf();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
