#import "ConferenceController.h"

#import <JitsiMeetSDK/JitsiMeetUserInfo.h>
#import <JitsiMeetSDK/JitsiMeetConferenceOptions.h>
#import <React/RCTLog.h>


@interface ConferenceController ()

@end

@implementation ConferenceController


- (void)viewDidLoad {
    [super viewDidLoad];
    
    
    if (self.room == nil) {
      //  NSLog(@"Room is nul! ");
        
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            [self dismissViewControllerAnimated:FALSE completion:nil];
        });
        
        return;
    }
    
    
    self.jitsiMeetView = (JitsiMeetView * ) self.view;
    self.jitsiMeetView.delegate = self;
    
    // Join the room.
    JitsiMeetConferenceOptions *options
    = [JitsiMeetConferenceOptions fromBuilder:^(JitsiMeetConferenceOptionsBuilder *builder) {
        builder.room = self.room;
        builder.userInfo = self.userInfo;
        [builder setFeatureFlag:@"pip.enabled" withBoolean:NO];
        
      //  NSLog(@"Conference options %@ ", self.options);
        
        if(self.options[@"token"] != NULL)
            builder.token = self.options[@"token"];
        if(self.options[@"subject"] != NULL)
            builder.subject = self.options[@"subject"];
        if(self.options[@"videoMuted"] != NULL)
            builder.videoMuted = [[self.options objectForKey:@"videoMuted"] boolValue];
        if(self.options[@"audioOnly"] != NULL)
            builder.audioOnly = [[self.options objectForKey:@"audioOnly"] boolValue];
        if(self.options[@"audioMuted"] != NULL)
            builder.audioMuted = [[self.options objectForKey:@"audioMuted"] boolValue];     
    }];
    
    
    [self.jitsiMeetView join:options];
    
    
}


- (void)conferenceWillJoin:(NSDictionary *)data {
   // NSLog(@"About to join conference %@", self.room);
    // [self sendEvent:@"CONFERENCE_WILL_JOIN_EVENT_NAME" body:data];
    
    NSDictionary *obj = @{@"type": @"CONFERENCE_WILL_JOIN_EVENT_NAME" , @"data": data};
    
    [[NSNotificationCenter defaultCenter] postNotificationName:@"SendNotificationEvents" object:obj];
    
}

- (void)conferenceJoined:(NSDictionary *)data {
  //  NSLog(@"Conference %@ joined", self.room);
    NSDictionary *obj = @{@"type": @"CONFERENCE_JOINED_EVENT_NAME" , @"data": data};
    
    [[NSNotificationCenter defaultCenter]
     postNotificationName:@"SendNotificationEvents"
     object:obj];
}

- (void)conferenceTerminated:(NSDictionary *)data {
  //  NSLog(@"Conference %@ terminated", self.room);
    NSDictionary *obj = @{@"type": @"CONFERENCE_TERMINATED_EVENT_NAME" , @"data": data};
    
    [[NSNotificationCenter defaultCenter]
     postNotificationName:@"SendNotificationEvents"
     object:obj];
    [self dismissViewControllerAnimated:FALSE completion:nil];
    
}

- (void)participantLeft:(NSDictionary *)data {
   // NSLog(@"participantLeft %@",data);
    NSDictionary *obj = @{@"type": @"PARTICIPANT_LEFT_EVENT_NAME" , @"data": data};
    
    [[NSNotificationCenter defaultCenter]
     postNotificationName:@"SendNotificationEvents"
     object:obj];
}

- (void)participantJoined:(NSDictionary *)data {
   // NSLog(@"onParticipantJoined %@",data);
    
    NSDictionary *obj = @{@"type": @"PARTICIPANT_JOINED_EVENT_NAME" , @"data": data};
    
    [[NSNotificationCenter defaultCenter]
     postNotificationName:@"SendNotificationEvents"
     object:obj];
    
}



- (void)viewDidDisappear:(BOOL)animated{
    [self.jitsiMeetView hangUp];
   // RCTLogInfo(@"++ hangUp ++");
}


- (void)viewWillDisappear:(BOOL )animated {
    [self.jitsiMeetView hangUp];
   // RCTLogInfo(@"++ hangUp ++");
    
}

@end
