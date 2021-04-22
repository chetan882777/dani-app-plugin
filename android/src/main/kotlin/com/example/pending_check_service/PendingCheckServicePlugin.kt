package com.example.pending_check_service

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

/** PendingCheckServicePlugin */
private const val TAG = "PendingCheckServicePlug"
public const val INTENT_TIME = "INTENT_TIME"
public const val INTENT_CART_ID = "INTENT_CART_ID"
public const val INTENT_TOKEN = "INTENT_TOKEN"
public const val INTENT_TITLE = "INTENT_TITLE"
public const val INTENT_DESCRIPTION = "INTENT_DESCRIPTION"
public const val INTENT_CANCEL_TITLE = "INTENT_CANCEL_TITLE"
public const val INTENT_CANCEL_DESCRIPTION = "INTENT_CANCEL_DESCRIPTION"
public const val INTENT_URL = "INTENT_URL"
public const val INTENT_ORDER_URL = "INTENT_ORDER_URL"
public const val INTENT_ORDER_ID = "INTENT_ID"
class PendingCheckServicePlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity


  private lateinit var channel : MethodChannel

  private lateinit var mContext: Context;

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "pending_check_service")
    channel.setMethodCallHandler(this)
    mContext = flutterPluginBinding.applicationContext;
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "getPlatformVersion") {
      result.success("Android ${android.os.Build.VERSION.RELEASE}")
    } else if (call.method == "startPendingCheckService") {
      val arguments = call.arguments as List<*>;
      val time : Int = arguments[0] as Int
      val cartId : String = arguments[1] as String
      val token : String = arguments[2] as String
      val title : String = arguments[3] as String
      val description : String = arguments[4] as String
      val cancelTitle : String = arguments[5] as String
      val cancelDescription : String = arguments[6] as String
      val url : String = arguments[7] as String
      val orderUrl = arguments[8] as String
      val orderId = arguments[9] as String
      val intent = Intent(mContext, PendingCheckService::class.java)

      intent.putExtra(INTENT_TIME, time)
      intent.putExtra(INTENT_CART_ID, cartId)
      intent.putExtra(INTENT_TOKEN, token)
      intent.putExtra(INTENT_TITLE, title)
      intent.putExtra(INTENT_DESCRIPTION, description)
      intent.putExtra(INTENT_CANCEL_TITLE, cancelTitle)
      intent.putExtra(INTENT_CANCEL_DESCRIPTION, cancelDescription)
      intent.putExtra(INTENT_URL, url)
      intent.putExtra(INTENT_ORDER_URL, orderUrl)
      intent.putExtra(INTENT_ORDER_ID, orderId)

      mContext.startService(intent)
    } else {
      result.notImplemented()
    }
  }

  private fun startServiceToCheck() {
    Log.d(TAG, "startServiceToCheck: ==CALLED")
    mContext.startService(Intent(mContext, PendingCheckService::class.java))
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}
