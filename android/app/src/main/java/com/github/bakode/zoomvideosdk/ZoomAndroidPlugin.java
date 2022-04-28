package com.github.bakode.zoomvideosdk;

import android.util.Log;

import com.getcapacitor.PermissionState;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;

@CapacitorPlugin(
  name = "ZoomAndroid",
  permissions = {
    @Permission(
      alias = "camera",
      strings = {
        android.Manifest.permission.CAMERA,
      }
    ),
    @Permission (
      alias = "audio",
      strings = {
        android.Manifest.permission.RECORD_AUDIO,
      }
    ),
    @Permission (
      alias = "storage",
      strings = {
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
      }
    ),
    @Permission (
      alias = "bluetooth",
      strings = {
        "android.permission.READ_PHONE_STATE",
        "android.permission.BLUETOOTH_CONNECT"
      }
    )
  }
)
public class ZoomAndroidPlugin extends Plugin {
  private static final String TAG = ZoomAndroidPlugin.class.getSimpleName();
  private static final Integer REQ_VIDEO_AUDIO_CODE = 1010;

  @PluginMethod()
  public void tryJoinMeeting(PluginCall call) {
    if ((getPermissionState("camera") != PermissionState.GRANTED) && (getPermissionState("audio") != PermissionState.GRANTED) && (getPermissionState("storage") != PermissionState.GRANTED) && (getPermissionState("bluetooth") != PermissionState.GRANTED)) {
      requestAllPermissions(call, "onPermissionGranted");
    } else {
      this.performJoinMeeting(call);
    }
  }

  @PermissionCallback
  private void onPermissionGranted(PluginCall call) {
    if ((getPermissionState("camera") == PermissionState.GRANTED) && (getPermissionState("audio") == PermissionState.GRANTED) && (getPermissionState("bluetooth") == PermissionState.GRANTED)) {
      performJoinMeeting(call);
    } else {
      call.reject("Permission is required to join consultation session");
    }
  }

  private void performJoinMeeting(PluginCall call) {
    String appointmentToken = call.getString("appointmentToken", "");
    String appointmentSessionName = call.getString("appointmentSessionName", "");
    String appointmentSessionPassword = call.getString("appointmentSessionPassword", "");
    String customerFullName = call.getString("customerFullName", "");

    Log.d(TAG, appointmentToken);
    Log.d(TAG, appointmentSessionName);
    Log.d(TAG, appointmentSessionPassword);
    Log.d(TAG, customerFullName);
  }
}
