package com.github.bakode.zoomvideosdk.zoom

import us.zoom.sdk.ZoomVideoSDKErrors.*

object ZoomErrorMsgs {
  fun getMessageByCode(errorCode: Int): String {
    when (errorCode) {
      Errors_Wrong_Usage -> return "Incorrect use"
      Errors_Internal_Error -> return "Internal error"
      Errors_Uninitialize -> return "Uninitialized"
      Errors_Memory_Error -> return "Memory error"
      Errors_Load_Module_Error -> return "Load module failed"
      Errors_UnLoad_Module_Error -> return "Unload module failed"
      Errors_Invalid_Parameter -> return "Parameter error"
      Errors_Unknown -> return "Unknown error"
      Errors_Auth_Error -> return "Authentication error"
      Errors_Auth_Empty_Key_or_Secret -> return "Empty key or secret"
      Errors_Auth_Wrong_Key_or_Secret -> return "Incorrect key or secret"
      Errors_Auth_DoesNot_Support_SDK -> return "Authenticated key or secret does not support SDK"
      Errors_Auth_Disable_SDK -> return "Disabled SDK with authenticated key or secret"
      Errors_SessionModule_Not_Found -> return "Module not found"
      Errors_SessionService_Invaild -> return "The service is invalid"
      Errors_Session_Join_Failed -> return "Join session failed"
      Errors_Session_No_Rights -> return "You don’t have permission to join this session"
      Errors_Session_Already_In_Progress -> return "Joining session…"
      Errors_Session_Dont_Support_SessionType -> return "Unsupported session type"
      Errors_Session_Reconncting -> return "Reconnecting session…"
      Errors_Session_Disconnect -> return "Disconnecting session…"
      Errors_Session_Not_Started -> return "This session has not started"
      Errors_Session_Need_Password -> return "This session requires password"
      Errors_Session_Password_Wrong -> return "Incorrect password"
      Errors_Session_Remote_DB_Error -> return "Error received from remote database"
      Errors_Session_Invalid_Param -> return "Parameter error when joining the session"
      Errors_Session_Audio_Error -> return "Session audio module error"
      Errors_Session_Audio_No_Microphone -> return "Session audio no microphone"
      Errors_Session_Video_Error -> return "Session video module error"
      Errors_Session_Video_Device_Error -> return "Session video device module error"
      Errors_Session_Live_Stream_Error -> return "Live stream error"
      RawDataError_MALLOC_FAILED -> return "Raw data memory allocation error"
      RawDataError_NOT_IN_Session -> return "Not in session when subscribing to raw data"
      RawDataError_NO_LICENSE -> return "License without raw data"
      RawDataError_VIDEO_MODULE_NOT_READY -> return "Video module is not ready"
      RawDataError_VIDEO_MODULE_ERROR -> return "Video module error"
      RawDataError_VIDEO_DEVICE_ERROR -> return "Video device error"
      RawDataError_NO_VIDEO_DATA -> return "No video data"
      RawDataError_SHARE_MODULE_NOT_READY -> return "Share module is not ready"
      RawDataError_SHARE_MODULE_ERROR -> return "Share module error"
      RawDataError_NO_SHARE_DATA -> return "No sharing data"
      RawDataError_AUDIO_MODULE_NOT_READY -> return "Audio module is not ready"
      RawDataError_AUDIO_MODULE_ERROR -> return "Audio module error"
      RawDataError_NO_AUDIO_DATA -> return "No audio data"
    }
    return errorCode.toString()
  }
}
