# react-native-jitsi-meet-sdk

React native wrapper for Jitsi Meet SDK Library
This Library implements the Jitsi SDK with a native activity on the Android side and a viewController for iOS part, which give you a better experience and less problems.

## Installation

```sh
npm install react-native-jitsi-meet-sdk
```

## Android Configuration:(MinSdk: 23)

Add this to your `android/build.gradle` :

```sh

allprojects {
    repositories {
        mavenCentral()
        mavenLocal()
        maven {
            url("$rootDir/../node_modules/react-native/android")
        }
        maven {
            // Android JSC is installed from npm
            url("$rootDir/../node_modules/jsc-android/dist")
        }
        maven {
            url "https://github.com/jitsi/jitsi-maven-repository/raw/master/releases" // <---- Add this line
        }
        google()
        maven { url 'https://www.jitpack.io' }
        jcenter()

    }
}

```

Add `tools:replace="android:allowBackup"` to your `AndroidManifest.xml` :

```sh

  <application
      ...
      tools:replace="android:allowBackup"  // <---- Add this line
      >  

```


## iOS Configuration:(Min Target: 12)

Add this to your `Info.plist` :

```sh
<key>NSCameraUsageDescription</key>
<string>Camera permission description</string>
<key>NSMicrophoneUsageDescription</key>
<string>Microphone permission description</string>
```

## Usage

Start meeting :

```js
import { startCall, endCall } from 'react-native-jitsi-meet-sdk';

// ...

  // Start the call with url and user

startCall("https://meet.jit.si/react-native-jitsi-meet-sdk", {
      name: "name",
      email: "test@mail.com",
      avatar: "https://avatar.png"
    })


  // Call this to end the call programmatically 
endCall()

```

Implement listeners :

```js
import JitsiMeetSdk from 'react-native-jitsi-meet-sdk';

// ...

  useEffect(() => {

    const subscription_will_join = eventManagerEmitter.addListener(
      JitsiMeetSdk.CONST_JS_CONFERENCE_WILL_JOIN_EVENT_NAME,
      (e) => {
        console.log("CONFERENCE_WILL_JOIN_EVENT_NAME");
      }
    );

    const subscription_terminated = eventManagerEmitter.addListener(
      JitsiMeetSdk.CONST_JS_CONFERENCE_TERMINATED_EVENT_NAME,
      (e) => {
        console.log("CONFERENCE_TERMINATED_EVENT_NAME");
      }
    );

    const subscription_joined = eventManagerEmitter.addListener(
      JitsiMeetSdk.CONST_JS_CONFERENCE_JOINED_EVENT_NAME,
      (e) => {
        console.log("CONFERENCE_JOINED_EVENT_NAME");
      }
    );

    const subscription_participant_left = eventManagerEmitter.addListener(
      JitsiMeetSdk.CONST_JS_PARTICIPANT_LEFT_EVENT_NAME,
      (e) => {
        console.log("PARTICIPANT_LEFT_EVENT_NAME", e);
      }
    );

    const subscription_participant_joined = eventManagerEmitter.addListener(
      JitsiMeetSdk.CONST_JS_PARTICIPANT_JOINED_EVENT_NAME,
      (e) => {
        console.log("PARTICIPANT_JOINED_EVENT_NAME", e);
      }
    );


    //This method called from android for ios you must see example below
    const subscription_retrieve_participants = eventManagerEmitter.addListener(
      JitsiMeetSdk.CONST_JS_RETRIEVE_PARTICIPANT_INFO_EVENT_NAME,
      (e) => {
        console.log("RETRIEVE_PARTICIPANT_INFO_EVENT_NAME", e);
      }
    );


    return () => {
      subscription_will_join.remove();
      subscription_joined.remove();
      subscription_terminated.remove();
      subscription_participant_left.remove();
      subscription_participant_joined.remove()
      subscription_retrieve_participants.remove()
    };
  });

```

Retrieve participants info :

```js
import { retrieveParticipantsInfo } from 'react-native-jitsi-meet-sdk';

// ...

   // For Android you must implement the "subscription_retrieve_participants" listener in order to retrieve the participants and call this function
retrieveParticipantsInfo((retrieveParticipant)=>{

})

```


## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
