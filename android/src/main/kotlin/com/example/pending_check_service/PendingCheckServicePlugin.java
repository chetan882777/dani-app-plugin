package com.example.pending_check_service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;
import com.example.pending_check_service.PendingCheckService.*;

import static com.example.pending_check_service.PendingCheckService.INTENT_CANCEL_DESCRIPTION;
import static com.example.pending_check_service.PendingCheckService.INTENT_CANCEL_TITLE;
import static com.example.pending_check_service.PendingCheckService.INTENT_CART_ID;
import static com.example.pending_check_service.PendingCheckService.INTENT_DESCRIPTION;
import static com.example.pending_check_service.PendingCheckService.INTENT_ORDER_ID;
import static com.example.pending_check_service.PendingCheckService.INTENT_ORDER_URL;
import static com.example.pending_check_service.PendingCheckService.INTENT_TIME;
import static com.example.pending_check_service.PendingCheckService.INTENT_TITLE;
import static com.example.pending_check_service.PendingCheckService.INTENT_TOKEN;
import static com.example.pending_check_service.PendingCheckService.INTENT_URL;

public class PendingCheckServicePlugin extends Activity implements MethodChannel.MethodCallHandler {
    private static final String TAG = "PendingCheckServicePlug";
    private Context mContext;
    private MethodChannel mChannel;
    
    public static void registerWith(PluginRegistry.Registrar registrar) {
        MethodChannel channel = new MethodChannel(registrar.messenger(), "pending_check_service");
        channel.setMethodCallHandler(new PendingCheckServicePlugin(registrar.context(), channel));
    }
    
    public PendingCheckServicePlugin(Context context, MethodChannel channel) {
        super();
        mContext = context;
        mChannel = channel;
    }

    @Override
    public void onMethodCall(@NonNull @NotNull MethodCall call, @NonNull @NotNull MethodChannel.Result result) {
        Log.d(TAG, "onMethodCall: CALLED");
        if (call.method.equals("getPlatformVersion")) {
            result.success("Android ${android.os.Build.VERSION.RELEASE}");
        } else if (call.method.equals("startPendingCheckService")) {
            List arguments = (List) call.arguments;
            Log.d(TAG, "onMethodCall:  == aergs $arguments");
            int time = (int) arguments.get(0);
            String cartId = (String) arguments.get(1);
            String token = (String) arguments.get(2);
            String title = (String) arguments.get(3);
            String description = (String) arguments.get(4);
            String cancelTitle = (String) arguments.get(5);
            String cancelDescription = (String) arguments.get(6);
            String url = (String) arguments.get(7);
            String orderUrl = (String) arguments.get(8);
            String orderId = (String) arguments.get(9);
            Intent intent = new Intent(mContext, PendingCheckService.class);

            intent.putExtra(INTENT_TIME, time);
            intent.putExtra(INTENT_CART_ID, cartId);
            intent.putExtra(INTENT_TOKEN, token);
            intent.putExtra(INTENT_TITLE, title);
            intent.putExtra(INTENT_DESCRIPTION, description);
            intent.putExtra(INTENT_CANCEL_TITLE, cancelTitle);
            intent.putExtra(INTENT_CANCEL_DESCRIPTION, cancelDescription);
            intent.putExtra(INTENT_URL, url);
            intent.putExtra(INTENT_ORDER_URL, orderUrl);
            intent.putExtra(INTENT_ORDER_ID, orderId);

            mContext.startService(intent);
        } else {
            result.notImplemented();
        }
    }
}
