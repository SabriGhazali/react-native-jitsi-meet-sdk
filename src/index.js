import { NativeModules, Platform } from 'react-native';
import * as constants from './constants';


const JitsiMeetSdk = NativeModules.JitsiMeetSdk

  
export function startCall(url,user)  {
  return JitsiMeetSdk.startCall(url, user);
}

export function endCall()  {
  return JitsiMeetSdk.endCall();
}

export function retrieveParticipantsInfo(callback)  {
 return Platform.OS === "ios" ? JitsiMeetSdk.retrieveParticipantsInfo((error, arrayInfo) => {
    if (error) {
      console.error(error);
    } else {
      callback(arrayInfo)
    }
  }) : JitsiMeetSdk.retrieveParticipantsInfo();
 
  
}





export const Constants = constants

export default JitsiMeetSdk
