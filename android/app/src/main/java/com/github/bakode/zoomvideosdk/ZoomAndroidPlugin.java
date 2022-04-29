package com.github.bakode.zoomvideosdk;

import android.content.Intent;
import android.util.Log;

import com.getcapacitor.PermissionState;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;
import com.github.bakode.zoomvideosdk.zoom.MeetingActivity;
import com.github.bakode.zoomvideosdk.zoom.ZoomErrorMsgs;
import com.github.bakode.zoomvideosdk.zoom.ZoomOpts;

import us.zoom.sdk.ZoomVideoSDK;
import us.zoom.sdk.ZoomVideoSDKErrors;

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
    if ((getPermissionState("camera") == PermissionState.GRANTED) && (getPermissionState("audio") == PermissionState.GRANTED) && (getPermissionState("storage") == PermissionState.GRANTED) && (getPermissionState("bluetooth") == PermissionState.GRANTED)) {
      performJoinMeeting(call);
    } else {
      call.reject("Permission is required to join consultation session");
    }
  }

  private void performJoinMeeting(PluginCall call) {
    getActivity().runOnUiThread(() -> {
      String appointmentToken = call.getString("appointmentToken", "");
      String appointmentSessionName = call.getString("appointmentSessionName", "");
      String appointmentSessionPassword = call.getString("appointmentSessionPassword", "");
      String customerFullName = call.getString("customerFullName", "");

      ZoomVideoSDK instance = ZoomVideoSDK.getInstance();

      int initZoomSDK = instance.initialize(
        getActivity().getApplication().getApplicationContext(),
        ZoomOpts.INSTANCE.zoomSDKParams()
      );

      if (initZoomSDK != ZoomVideoSDKErrors.Errors_Success) {
        String errorMessage = ZoomErrorMsgs.INSTANCE.getMessageByCode(initZoomSDK);
        call.reject(errorMessage);
      }

      String zoomSDKVersion = instance.getSDKVersion();
      Log.d(TAG, "ZOOM VIDEO SDK CONNECTED WITH CURRENT VERSION: " + zoomSDKVersion);

      Intent intent = new Intent(getActivity(), MeetingActivity.class);
      intent.putExtra("appointmentToken", appointmentToken);
      intent.putExtra("appointmentSessionName", appointmentSessionName);
      intent.putExtra("appointmentSessionPassword", appointmentSessionPassword);
      intent.putExtra("customerFullName", customerFullName);
      getActivity().startActivity(intent);
    });
  }
}
