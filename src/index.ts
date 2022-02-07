import { NativeModules, Platform } from 'react-native';

import {FLAGS} from './constants';


const JitsiMeetSdk = NativeModules.JitsiMeetSdk

  
export function startCall( url : String, user : { name: string, email: string, avatar: string })  {
  return JitsiMeetSdk.startCall(url, user);
}

export function endCall()  {
  return JitsiMeetSdk.endCall();
}

export function retrieveParticipantsInfo(callback : Function)  {
 return Platform.OS === "ios" ? JitsiMeetSdk.retrieveParticipantsInfo((error : String, arrayInfo : String) => {
    if (error) {
      console.error(error);
    } else {
      callback(arrayInfo)
    }
  }) : JitsiMeetSdk.retrieveParticipantsInfo();
 
  
}

export {FLAGS} from './constants';


export default {
  JitsiMeetSdk,
};
