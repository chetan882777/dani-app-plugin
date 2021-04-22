import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:pending_check_service/pending_check_service.dart';

void main() {
  const MethodChannel channel = MethodChannel('pending_check_service');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await PendingCheckService.platformVersion, '42');
  });
}
