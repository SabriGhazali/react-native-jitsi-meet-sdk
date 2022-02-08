import { NativeModules, Platform } from 'react-native';

const JitsiMeetSdk = NativeModules.JitsiMeetSdk

interface User { name?: string, email?: string, avatar?: string }

interface Options { token?: string, subject?: string, audioMuted?: boolean, videoMuted?: boolean, audioOnly?: boolean }


export function startCall(url: String, user: User, options: Options) {
  return JitsiMeetSdk.startCall(url, user, options);
}

export function endCall() {
  return JitsiMeetSdk.endCall();
}

export function retrieveParticipantsInfo(callback: Function) {
  return Platform.OS === "ios" ? JitsiMeetSdk.retrieveParticipantsInfo((error: String, arrayInfo: String) => {
    if (error) {
      console.error(error);
    } else {
      callback(arrayInfo)
    }
  }) : JitsiMeetSdk.retrieveParticipantsInfo();


}

export default JitsiMeetSdk