#import "JitsiMeetSdk.h"
#import <JitsiMeetSDK/JitsiMeetUserInfo.h>
#import <JitsiMeetSDK/JitsiMeetConferenceOptions.h>
#import <React/RCTLog.h>

@interface JitsiMeetSdk ()

@property (nonatomic, strong) NSBundle *assetBundle;

@end


@implementation JitsiMeetSdk{
    
    ConferenceController *viewConference;
    bool hasListeners;
    
}



RCT_EXPORT_MODULE()


RCT_EXPORT_METHOD(startCall:(NSString *)urlString userInfo:(NSDictionary *)userInfo options:(NSDictionary *)options)
{
  
  
  [[NSNotificationCenter defaultCenter] removeObserver:self name:@"SendNotificationEvents" object:nil];
  [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(receiveNotification:) name:@"SendNotificationEvents" object:nil];
  
    JitsiMeetUserInfo * _userInfo = [[JitsiMeetUserInfo alloc] init];
    if (userInfo != NULL) {
      if (userInfo[@"displayName"] != NULL) {
        _userInfo.displayName = userInfo[@"displayName"];
      }
      if (userInfo[@"email"] != NULL) {
        _userInfo.email = userInfo[@"email"];
      }
      if (userInfo[@"avatar"] != NULL) {
        NSURL *url = [NSURL URLWithString:[userInfo[@"avatar"] stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]]];
        _userInfo.avatar = url;
      }
    }
  
    dispatch_sync(dispatch_get_main_queue(), ^{
      
      RCTLogInfo(@"Load Video URL %@", urlString);

    self.assetBundle = [NSBundle bundleForClass:[self class]];
    NSString *bundlePath = [self.assetBundle pathForResource:@"Conference" ofType:@"bundle"];
    if (bundlePath) {
        self.assetBundle = [NSBundle bundleWithPath:bundlePath];
    }
      
      UIStoryboard *sb = [UIStoryboard storyboardWithName:@"Conference" bundle:self.assetBundle];
      viewConference = [sb instantiateViewControllerWithIdentifier:@"ConferenceController"];
        
        
      viewConference.room = urlString;
      viewConference.userInfo = _userInfo;
      viewConference.options = options;


        

      UIViewController *rootVC = UIApplication.sharedApplication.delegate.window.rootViewController;

      while (rootVC.presentedViewController != nil) {
        rootVC = rootVC.presentedViewController;
      }
      
      [viewConference setModalPresentationStyle: UIModalPresentationFullScreen];

      [rootVC presentViewController:viewConference animated:FALSE completion:nil];

    });
}


RCT_EXPORT_METHOD(endCall)
{
    dispatch_sync(dispatch_get_main_queue(), ^{
        [viewConference.jitsiMeetView hangUp];
      RCTLogInfo(@"++ endCall = ");

    });
}

RCT_EXPORT_METHOD(retrieveParticipantsInfo:(RCTResponseSenderBlock)callback)
{
  
    dispatch_sync(dispatch_get_main_queue(), ^{
        [viewConference.jitsiMeetView retrieveParticipantsInfo:^(NSArray * _Nullable arrayInfo) {
            
         //   RCTLogInfo(@"retrieveParticipantsInfo %@",arrayInfo);

         // RCTLogInfo(@"++ retrieveParticipantsInfo = %@",arrayInfo);
            
            callback(@[[NSNull null], arrayInfo]);

        }];
    });
}

- (NSDictionary *)constantsToExport
{
 return @{
   @"CONST_JS_CONFERENCE_JOINED_EVENT_NAME": @"CONFERENCE_JOINED_EVENT_NAME" ,
   @"CONST_JS_CONFERENCE_WILL_JOIN_EVENT_NAME": @"CONFERENCE_WILL_JOIN_EVENT_NAME" ,
   @"CONST_JS_CONFERENCE_TERMINATED_EVENT_NAME": @"CONFERENCE_TERMINATED_EVENT_NAME" ,
   @"CONST_JS_PARTICIPANT_LEFT_EVENT_NAME": @"PARTICIPANT_LEFT_EVENT_NAME" ,
   @"CONST_JS_PARTICIPANT_JOINED_EVENT_NAME": @"PARTICIPANT_JOINED_EVENT_NAME" ,
   @"CONST_JS_RETRIEVE_PARTICIPANT_INFO_EVENT_NAME": @"RETRIEVE_PARTICIPANT_INFO_EVENT_NAME",
 };
}


- (NSArray<NSString *> *)supportedEvents {
    return @[@"onSessionConnect",
             @"CONFERENCE_JOINED_EVENT_NAME",
             @"CONFERENCE_WILL_JOIN_EVENT_NAME",
             @"CONFERENCE_TERMINATED_EVENT_NAME",
             @"PARTICIPANT_LEFT_EVENT_NAME",
             @"PARTICIPANT_JOINED_EVENT_NAME",
             @"RETRIEVE_PARTICIPANT_INFO_EVENT_NAME",
    ];
}


// Will be called when this module's first listener is added.
-(void)startObserving {
    hasListeners = YES;
    // Set up any upstream listeners or background tasks as necessary
}

// Will be called when this module's last listener is removed, or on dealloc.
-(void)stopObserving {
    hasListeners = NO;
    // Remove upstream listeners, stop unnecessary background tasks
}



- (void)sendEvent:(NSString *)eventName
             body:(NSDictionary *)body
{
  
  
  if (hasListeners) {
    // Only send events if anyone is listening
    [self sendEventWithName:eventName body:@{@"data": body}];

  }
}


- (void) receiveNotification:(NSNotification *) notification
{
    [self sendEvent:notification.object[@"type"] body:notification.object[@"data"]];
}

@end
