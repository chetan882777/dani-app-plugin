
import 'dart:async';

import 'package:flutter/services.dart';

class PendingCheckService {
  static const MethodChannel _channel =
      const MethodChannel('pending_check_service');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static void startPendingCheckService() {
    _channel.invokeMethod("startPendingCheckService",
        [
          10, // time
          "1234", // cart id
          "dJxG3dOJQWPWQz0uVubwCrTB6c4UjFZ9aD7hMtYSlol8SPs2RP3mtj3KVibA", // token
          "Title" , //title
          "Description" , //description
          "Order Canceled", //cancel title
          "Restaurant not accepted your order", // cancel description
          "https://daniapp.net/api/carts/",  // cancel url
          "https://daniapp.net/api/orders/", // order status url
          "704" // order id
        ]
    );
  }
}
