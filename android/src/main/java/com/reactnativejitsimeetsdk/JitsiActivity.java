package com.reactnativejitsimeetsdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.facebook.react.modules.core.PermissionListener;

import org.jitsi.meet.sdk.BroadcastEvent;
import org.jitsi.meet.sdk.JitsiMeetActivityDelegate;
import org.jitsi.meet.sdk.JitsiMeetActivityInterface;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetUserInfo;
import org.jitsi.meet.sdk.JitsiMeetView;
import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;

public class JitsiActivity extends FragmentActivity implements JitsiMeetActivityInterface {
  private JitsiMeetView view;

  @Override
  protected void onActivityResult(
    int requestCode,
    int resultCode,
    Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    JitsiMeetActivityDelegate.onActivityResult(
      this, requestCode, resultCode, data);
  }

  @Override
  public void onBackPressed() {
    JitsiMeetActivityDelegate.onBackPressed();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);


    Intent intent = getIntent();
    String url = intent.getStringExtra("url");
    JitsiMeetUserInfo _userInfo = new JitsiMeetUserInfo();
    if (intent.hasExtra("name")) {
      _userInfo.setDisplayName(intent.getStringExtra("name"));
    }
    if (intent.hasExtra("email")) {
      _userInfo.setEmail(intent.getStringExtra("email"));
    }
    if (intent.hasExtra("avatar")) {
      String avatarURL = intent.getStringExtra("avatar");
      try {
        _userInfo.setAvatar(new URL(avatarURL));
      } catch (MalformedURLException e) {
      }
    }


    view = new JitsiMeetView(this);

    JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
      .setRoom(url)
      .setAudioOnly(false)
      .setUserInfo(_userInfo)
      .setFeatureFlag("pip.enabled",false)
      .setFeatureFlag("invite.enabled",false)
      .setFeatureFlag("meeting-name.enabled",false)
      .setFeatureFlag("conference-timer.enabled",false)
      .setFeatureFlag("toolbox.alwaysVisible",true)
      .setFeatureFlag("security-options.enabled",false)
      .build();

    view.join(options);

    setContentView(view);

    registerForBroadcastMessages();

  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

    view.dispose();
    view = null;

    JitsiMeetActivityDelegate.onHostDestroy(this);
  }

  @Override
  public void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    JitsiMeetActivityDelegate.onNewIntent(intent);
  }

  @Override
  public void onRequestPermissionsResult(
    final int requestCode,
    final String[] permissions,
    final int[] grantResults) {
    JitsiMeetActivityDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }

  @Override
  protected void onResume() {
    super.onResume();

    JitsiMeetActivityDelegate.onHostResume(this);
  }

  @Override
  protected void onStop() {
    super.onStop();

    JitsiMeetActivityDelegate.onHostPause(this);
  }

  @Override
  public void requestPermissions(String[] strings, int i, PermissionListener permissionListener) {

  }

  private void registerForBroadcastMessages() {
    IntentFilter intentFilter = new IntentFilter();

    for (BroadcastEvent.Type type : BroadcastEvent.Type.values()) {
      intentFilter.addAction(type.getAction());
    }

    LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
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

  private void onBroadcastReceived(Intent intent) throws JSONException {
    if (intent != null) {
      BroadcastEvent event = new BroadcastEvent(intent);
      switch (event.getType()) {
        case CONFERENCE_TERMINATED:
          this.finish();
          break;
      }
    }
  }
}
