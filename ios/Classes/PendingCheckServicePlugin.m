#import "PendingCheckServicePlugin.h"
#if __has_include(<pending_check_service/pending_check_service-Swift.h>)
#import <pending_check_service/pending_check_service-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "pending_check_service-Swift.h"
#endif

@implementation PendingCheckServicePlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftPendingCheckServicePlugin registerWithRegistrar:registrar];
}
@end
