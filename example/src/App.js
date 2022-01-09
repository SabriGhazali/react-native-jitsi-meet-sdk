
import React, { useEffect, useState } from 'react';
import { StyleSheet, View, Text, TouchableOpacity, NativeEventEmitter } from 'react-native';
import JitsiMeetSdk, { startCall, endCall, retrieveParticipantsInfo } from 'react-native-jitsi-meet-sdk';

export default function App() {

  const eventManagerEmitter = new NativeEventEmitter(JitsiMeetSdk);

  const [url, setUrl] = useState("https://meet.jit.si/react-native-jitsi-meet-sdk")

  const callAction = () => {
    startCall(url, {
      name: "name",
      email: "test@mail.com",
      avatar: "https://avatar.png"
    })
  }

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


    //This method called only from android 
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

  return (
    <View style={styles.container}>

      <TouchableOpacity
        onPress={() => callAction()}
      ><Text>Start Call</Text>
      </TouchableOpacity>

    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  }
});
