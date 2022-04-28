@file:JvmName("ZoomVideoOptions")
package com.github.bakode.zoomvideosdk.zoom

import us.zoom.sdk.*

object ZoomOpts {
  private const val WEB_DOMAIN = "zoom.us"

  private val DEFAULT_MEMORY_MODE = ZoomVideoSDKRawDataMemoryMode
    .ZoomVideoSDKRawDataMemoryModeHeap

  private val zoomAudioOpts: () -> ZoomVideoSDKAudioOption = {
    ZoomVideoSDKAudioOption().apply {
      this.connect = true
      this.mute = false
    }
  }

  private val zoomVideoOpts: () -> ZoomVideoSDKVideoOption = {
    ZoomVideoSDKVideoOption().apply {
      this.localVideoOn = true
    }
  }

  fun zoomSDKParams(): ZoomVideoSDKInitParams {
    return ZoomVideoSDKInitParams().apply {
      this.domain = WEB_DOMAIN
      this.videoRawDataMemoryMode = DEFAULT_MEMORY_MODE
      this.audioRawDataMemoryMode = DEFAULT_MEMORY_MODE
      this.logFilePrefix = "ZoomOpts"
      this.enableLog = true
    }
  }

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
