# react-native-jitsi-meet-sdk

React native wrapper for Jitsi Meet SDK Library

## Installation

```sh
npm install react-native-jitsi-meet-sdk
```

## Usage

Start meeting :

```js
import { startCall, endCall, retrieveParticipantsInfo } from 'react-native-jitsi-meet-sdk';

// ...

startCall("https://meet.jit.si/react-native-jitsi-meet-sdk", {
      name: "name",
      email: "test@mail.com",
      avatar: "https://avatar.png"
    })

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

Start meeting :

```js
import { retrieveParticipantsInfo } from 'react-native-jitsi-meet-sdk';

// ...

retrieveParticipantsInfo((retrieveParticipant)=>{
   // For Android you will get the participant in the callback "subscription_retrieve_participants"
})

```


## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
