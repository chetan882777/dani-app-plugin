import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';

class PendingCheckService {
  static const MethodChannel _channel =
      const MethodChannel('pending_check_service');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static void startPendingCheckService(
      {int time,
      String cardId,
      String orderId,
      String token,
      String orderUrl,
      String cartUrl,
      String title,
      String description,
      String cancelTitle,
      String cancelDescription}) {
    _channel.invokeMethod("startPendingCheckService", [
      time, // time
      cardId, // cart id
      token, // token
      title, //title
      description, //description
      cancelTitle, //cancel title
      cancelDescription, // cancel description
      cartUrl, // cancel url
      orderUrl, // order status url
      orderId // order id
    ]);
  }
}
