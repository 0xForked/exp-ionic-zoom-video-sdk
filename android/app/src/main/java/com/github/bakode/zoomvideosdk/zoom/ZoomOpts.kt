@file:JvmName("ZoomVideoOptions")
package com.github.bakode.zoomvideosdk.zoom

import us.zoom.sdk.*

object ZoomOpts {
  private const val WEB_DOMAIN = "zoom.us"

  private val DEFAULT_MEMORY_MODE = ZoomVideoSDKRawDataMemoryMode
    .ZoomVideoSDKRawDataMemoryModeHeap

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
    localVideoStatus: Boolean
  ): ZoomVideoSDKSessionContext {
    return ZoomVideoSDKSessionContext().apply {
      this.audioOption = ZoomVideoSDKAudioOption().apply {
        this.connect = true
        this.mute = false
      }
      this.videoOption = ZoomVideoSDKVideoOption().apply {
        this.localVideoOn = localVideoStatus
      }
      this.sessionIdleTimeoutMins = sessionIdleTimeout
      this.token = appointmentToken
      this.sessionName = appointmentSessionName
      this.sessionPassword = appointmentSessionPassword
      this.userName = customerFullName
    }
  }
}
