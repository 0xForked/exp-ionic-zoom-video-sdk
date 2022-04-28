package com.github.bakode.zoomvideosdk.zoom

import us.zoom.sdk.*

object ZoomOpts {
  private const val WEB_DOMAIN = "zoom.us"

  private val DEFAULT_MEMORY_MODE = ZoomVideoSDKRawDataMemoryMode
    .ZoomVideoSDKRawDataMemoryModeHeap

  val zoomSDKParams: () -> ZoomVideoSDKInitParams = {
    ZoomVideoSDKInitParams().apply {
      this.domain = WEB_DOMAIN
      this.videoRawDataMemoryMode = DEFAULT_MEMORY_MODE
      this.audioRawDataMemoryMode = DEFAULT_MEMORY_MODE
      this.logFilePrefix = "ZoomOpts"
      this.enableLog = true
    }
  }

  val zoomAudioOpts: () -> ZoomVideoSDKAudioOption = {
    ZoomVideoSDKAudioOption().apply {
      this.connect = true
      this.mute = false
    }
  }

  val zoomVideoOpts: () -> ZoomVideoSDKVideoOption = {
    ZoomVideoSDKVideoOption().apply {
      this.localVideoOn = true
    }
  }

  /**
   * We recommend that, you can generate jwttoken on your own server instead of hardcore in the code.
   * We hardcore it here, just to run the demo.
   *
   * You can generate a jwttoken on the https://jwt.io/
   * with this payload:
   * {
   * "appKey": "string", // app key
   * "iat": long, // access token issue timestamp
   * "exp": long, // access token expire time
   * "tokenExp": long // token expire time 1800
   * }
   */
  fun zoomSessionCtx(
    sessionIdleTimeout: Int = 10,
    appointmentToken: String,
    appointmentSessionName: String,
    appointmentSessionPassword: String,
    customerFullName: String,
  ): ZoomVideoSDKSessionContext {
    return ZoomVideoSDKSessionContext().apply {
      this.audioOption = zoomAudioOpts()
      this.videoOption = zoomVideoOpts()
      this.sessionIdleTimeoutMins = sessionIdleTimeout
      this.token = appointmentToken
      this.sessionName = appointmentSessionName
      this.sessionPassword = appointmentSessionPassword
      this.userName = customerFullName
    }
  }
}
