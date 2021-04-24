import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:pending_check_service/pending_check_service.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
    initPlatformState();
    PendingCheckService.startPendingCheckService(
      time: 10,
      token: "4M4o0yoCJQc7cgRwcVlK2m8kdFwOfQbhXU7Ctt5FsLa4wAUbiz2sNzEOrfmR",
      cardId: "asd",
      orderId: "750",
      orderUrl: "https://daniapp.net/api/orders/",
      updateUrl: "https://daniapp.net/api/update_order",
      title: "dsfsdf",
      description: "sdffsdsdf",
      cancelTitle: "dfsdfsdfaasd",
      cancelDescription: "Sdfffadasd"
    );
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await PendingCheckService.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Text('Running on: $_platformVersion\n'),
        ),
      ),
    );
  }
}
