package com.reactnativejitsimeetsdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import org.jitsi.meet.sdk.BroadcastEvent;
import org.jitsi.meet.sdk.BroadcastIntentHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ReactModule(name = JitsiMeetSdkModule.NAME)
public class JitsiMeetSdkModule extends ReactContextBaseJavaModule implements JSEventSender{
    public static final String NAME = "JitsiMeetSdk";
    private final ReactApplicationContext mReactContext;

    public JitsiMeetSdkModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.mReactContext = reactContext;
        registerForBroadcastMessages();
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

  private void registerForBroadcastMessages() {
    IntentFilter intentFilter = new IntentFilter();

    for (BroadcastEvent.Type type : BroadcastEvent.Type.values()) {
      intentFilter.addAction(type.getAction());
    }

    LocalBroadcastManager.getInstance(mReactContext).registerReceiver(broadcastReceiver, intentFilter);
  }

  public final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      try {
        onBroadcastReceived(intent);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
  };

  @ReactMethod
  public void startCall(String url, ReadableMap userInfo , ReadableMap options) {
    UiThreadUtil.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        Intent intent = new Intent(getCurrentActivity(), JitsiActivity.class);

        if (userInfo != null) {
          if (userInfo.hasKey("name")) {
            intent.putExtra("name", userInfo.getString("name"));
          }
          if (userInfo.hasKey("email")) {
            intent.putExtra("email", userInfo.getString("email"));
          }
          if (userInfo.hasKey("avatar")) {
            intent.putExtra("avatar", userInfo.getString("avatar"));
          }
        }

        if (options != null) {
          if (options.hasKey("token")) {
            intent.putExtra("token", options.getString("token"));
          }
          if (options.hasKey("subject")) {
            intent.putExtra("subject", options.getString("subject"));
          }
          if (options.hasKey("audioMuted")) {
            intent.putExtra("audioMuted", options.getBoolean("audioMuted"));
          }
          if (options.hasKey("videoMuted")) {
            intent.putExtra("videoMuted", options.getBoolean("videoMuted"));
          }
          if (options.hasKey("audioOnly")) {
            intent.putExtra("audioOnly", options.getBoolean("audioOnly"));
          }
        }

        intent.putExtra("url", url);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        Objects.requireNonNull(getCurrentActivity()).startActivity(intent);
        getCurrentActivity().overridePendingTransition(0, 0);

      }

    });
  }

  @ReactMethod
  public void endCall() {
    UiThreadUtil.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        hangUp();
      }
    });
  }


  @ReactMethod
  public void retrieveParticipantsInfo() {
    Intent retrieveBroadcastIntent = new Intent(Constants.RETRIEVE_INTENT);
    retrieveBroadcastIntent.putExtra("requestId", "1212");
    LocalBroadcastManager.getInstance(mReactContext).sendBroadcast(retrieveBroadcastIntent);
  }

  public void hangUp() {
    Intent hangupBroadcastIntent = BroadcastIntentHelper.buildHangUpIntent();
    LocalBroadcastManager.getInstance(Objects.requireNonNull(getCurrentActivity())).sendBroadcast(hangupBroadcastIntent);
  }

  private void onBroadcastReceived(Intent intent) throws JSONException {
    if (intent != null) {
      BroadcastEvent event = new BroadcastEvent(intent);


      switch (event.getType()) {
        case CONFERENCE_JOINED:
          WritableMap eventData = Arguments.createMap();
          sendEventToJS(mReactContext, Constants.CONST_JS_CONFERENCE_JOINED_EVENT_NAME, eventData);
          break;
        case CONFERENCE_WILL_JOIN:
          WritableMap eventDataWillJoin = Arguments.createMap();
          sendEventToJS(mReactContext, Constants.CONST_JS_CONFERENCE_WILL_JOIN_EVENT_NAME, eventDataWillJoin);
          break;
        case CONFERENCE_TERMINATED:
          WritableMap eventDataTerminated = Arguments.createMap();
          sendEventToJS(mReactContext, Constants.CONST_JS_CONFERENCE_TERMINATED_EVENT_NAME, eventDataTerminated);
          break;
        case PARTICIPANT_LEFT:
          WritableMap eventDataParticipantLeft = Arguments.createMap();
          eventDataParticipantLeft.putString("participantId", String.valueOf(event.getData().get("participantId")));
          sendEventToJS(mReactContext, Constants.CONST_JS_PARTICIPANT_LEFT_EVENT_NAME, eventDataParticipantLeft);
          break;
        case PARTICIPANT_JOINED:
          WritableMap eventDataParticipantJoined = Arguments.createMap();
          eventDataParticipantJoined.putString("participantId", String.valueOf(event.getData().get("participantId")));
          sendEventToJS(mReactContext, Constants.CONST_JS_PARTICIPANT_JOINED_EVENT_NAME, eventDataParticipantJoined);

          break;
        case PARTICIPANTS_INFO_RETRIEVED:
          WritableMap eventDataParticipantsInfoRetrieved = Arguments.createMap();
          WritableMap returnMap = Utils.convertJsonToMap(new JSONObject(event.getData()));
          eventDataParticipantsInfoRetrieved.putMap("retrievedInfo", returnMap);
          sendEventToJS(mReactContext, Constants.CONST_JS_RETRIEVE_PARTICIPANT_INFO_EVENT_NAME, eventDataParticipantsInfoRetrieved);
          break;
      }


    }
  }

  @Override
  public void sendEventToJS(ReactContext reactContext, String eventName, @Nullable WritableMap params) {
    reactContext
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
      .emit(eventName, params);
  }


  @Nullable
  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants = new HashMap<>();
    constants.put("CONST_JS_CONFERENCE_JOINED_EVENT_NAME", Constants.CONST_JS_CONFERENCE_JOINED_EVENT_NAME);
    constants.put("CONST_JS_CONFERENCE_TERMINATED_EVENT_NAME", Constants.CONST_JS_CONFERENCE_TERMINATED_EVENT_NAME);
    constants.put("CONST_JS_CONFERENCE_WILL_JOIN_EVENT_NAME", Constants.CONST_JS_CONFERENCE_WILL_JOIN_EVENT_NAME);
    constants.put("CONST_JS_PARTICIPANT_LEFT_EVENT_NAME", Constants.CONST_JS_PARTICIPANT_LEFT_EVENT_NAME);
    constants.put("CONST_JS_PARTICIPANT_JOINED_EVENT_NAME", Constants.CONST_JS_PARTICIPANT_JOINED_EVENT_NAME);
    constants.put("CONST_JS_RETRIEVE_PARTICIPANT_INFO_EVENT_NAME", Constants.CONST_JS_RETRIEVE_PARTICIPANT_INFO_EVENT_NAME);
    return constants;
  }

}
