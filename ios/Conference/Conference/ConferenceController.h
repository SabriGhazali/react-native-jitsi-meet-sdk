
#import <UIKit/UIKit.h>

@import JitsiMeetSDK;

@interface ConferenceController : UIViewController<JitsiMeetViewDelegate>

@property (nonatomic, weak) NSString *room;

@property (nonatomic, weak) NSDictionary *options;

@property (nonatomic, weak) JitsiMeetUserInfo *userInfo ;

@property (nonatomic, weak) JitsiMeetView *jitsiMeetView ;


@end
