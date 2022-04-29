package com.github.bakode.zoomvideosdk.zoom

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.github.bakode.zoomvideosdk.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import us.zoom.sdk.*
import java.util.*

class MeetingActivity : AppCompatActivity(), View.OnTouchListener, ZoomVideoSDKDelegate {

    companion object
    {
        val TAG: String = MeetingActivity::class.simpleName as String
        var secondaryVideoContainerXPos = 0f
        var secondaryVideoContainerYPos = 0f
        var secondaryVideoLastAction = 0
    }

    private lateinit var zoomSDKInstance: ZoomVideoSDK
    private lateinit var remoteUser: ZoomVideoSDKUser

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting)

        val intent: Intent = intent
        val appointmentToken: String = intent.getStringExtra("appointmentToken") ?: ""
        val appointmentSessionName: String = intent.getStringExtra("appointmentSessionName") ?: ""
        val appointmentSessionPassword: String = intent.getStringExtra("appointmentSessionPassword") ?: ""
        val customerFullName: String = intent.getStringExtra("customerFullName") ?: ""
        val enableCamera: Boolean = intent.getBooleanExtra("enableCamera", true)
        val enableMicrophone: Boolean = intent.getBooleanExtra("enableMicrophone", true)
        val appointmentSessionStartAt: Int = intent.getIntExtra("appointmentSessionStartAt", 0)
        val appointmentSessionEndAt: Int = intent.getIntExtra("appointmentSessionEndAt", 0)

        this.zoomSDKInstance = ZoomVideoSDK.getInstance()

        this.zoomSDKInstance.joinSession(ZoomOpts.zoomSessionCtx(
            appointmentToken = appointmentToken,
            appointmentSessionName = appointmentSessionName,
            appointmentSessionPassword = appointmentSessionPassword,
            customerFullName = customerFullName,
            localAudioStatus = !enableMicrophone,
            localVideoStatus = enableCamera
        )).let { session ->
            if (session == null) {
                Log.d(TAG, "NO ZOOM SESSION")
                return
            } else {
              this.setMicrophoneStatus(enableMicrophone)
              this.setCameraStatus(enableCamera)
              this.countingSessionTime(appointmentSessionStartAt, appointmentSessionEndAt)
            }
        }

        this.zoomSDKInstance.videoHelper
          .rotateMyVideo(display?.rotation as Int)

        this.zoomSDKInstance.addListener(this)
    }

    override fun onStart()
    {
        super.onStart()
        this.initViewListener()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, event: MotionEvent): Boolean
    {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                secondaryVideoContainerXPos = view.x - event.rawX
                secondaryVideoContainerYPos = view.y - event.rawY
                secondaryVideoLastAction = MotionEvent.ACTION_DOWN
            }

            MotionEvent.ACTION_MOVE -> {
                view.y = event.rawY + secondaryVideoContainerYPos
                view.x = event.rawX + secondaryVideoContainerXPos
                secondaryVideoLastAction = MotionEvent.ACTION_MOVE
            }

            MotionEvent.ACTION_UP -> {
                if (secondaryVideoLastAction == MotionEvent.ACTION_DOWN) {
                  Log.d(TAG, "CONTAINER_CLICKED")
                }
            }

            else -> return false
        }

        return true
    }

    private fun initViewListener()
    {
      findViewById<FloatingActionButton>(R.id.fabSwitchCamera)
        .setOnClickListener { this.onCameraSwitched() }

      findViewById<FloatingActionButton>(R.id.fabCameraStatus)
        .setOnClickListener { this.onChangeCameraStatus() }

      findViewById<FloatingActionButton>(R.id.fabMicrophoneStatus)
        .setOnClickListener { this.onChangeMicrophoneStatus() }

      findViewById<FloatingActionButton>(R.id.fabDismissMeeting)
        .setOnClickListener { this.finish() }

      findViewById<View>(R.id.secondaryVideoContainer)
        .setOnTouchListener(this)
    }

    private fun countingSessionTime(startAt: Int, endAt: Int)
    {
        val startAtDate = Date(startAt.toLong() * 1000)
        val endAtDate = Date(endAt.toLong() * 1000)
        val diffInSeconds: Long = ((endAtDate.time - startAtDate.time))

        object: CountDownTimer(diffInSeconds, 1000) {
          override fun onTick(millisUntilFinished: Long) {
            val minutes = (((millisUntilFinished/(1000*60)) % 60))
              .toString().padStart(2, '0')
            val seconds = ((millisUntilFinished / 1000) % 60)
              .toString().padStart(2, '0')
            val displayCountingTime = "$minutes : $seconds"

            findViewById<TextView>(R.id.sessionCountingTime)
              .text = displayCountingTime
          }
          override fun onFinish() {
            Log.d(TAG, "FINISH_COUNTDOWN_DISMISS_THE_CALL")
          }
        }.start()
    }

    private fun onChangeMicrophoneStatus()
    {
        val customer = this.zoomSDKInstance.session.mySelf

        val isMuted = customer.audioStatus.isMuted

        this.setMicrophoneStatus(isMuted)
    }

    private fun setMicrophoneStatus(status: Boolean)
    {
      if (status) {
        this.zoomSDKInstance
          .audioHelper
          .unMuteAudio(this.zoomSDKInstance.session.mySelf)
      } else {
        this.zoomSDKInstance
          .audioHelper
          .muteAudio(this.zoomSDKInstance.session.mySelf)
      }

      findViewById<FloatingActionButton>(R.id.fabMicrophoneStatus)
        .setImageDrawable(getMicrophoneStatusIcon(status))
    }

    private fun getMicrophoneStatusIcon(isMuted: Boolean): Drawable
    {
        val iconOn = ContextCompat.getDrawable(
          this, R.drawable.ic_baseline_mic)
        val iconOff = ContextCompat.getDrawable(
          this, R.drawable.ic_baseline_mic_off)

        return if (isMuted) iconOn as Drawable
        else iconOff as Drawable
    }

    private fun onChangeCameraStatus()
    {
        val customer = this.zoomSDKInstance.session.mySelf

        val isEnable = customer.videoStatus.isOn

        this.setCameraStatus(!isEnable)
    }

    private fun setCameraStatus(status: Boolean)
    {
      if (status) {
        this.zoomSDKInstance
          .videoHelper.startVideo()
      } else {
        this.zoomSDKInstance
          .videoHelper.stopVideo()
      }

      findViewById<FloatingActionButton>(R.id.fabCameraStatus)
        .setImageDrawable(getCameraStatusIcon(status))
    }

    private fun getCameraStatusIcon(isEnabled: Boolean): Drawable
    {
        val iconOn = ContextCompat.getDrawable(
          this, R.drawable.ic_baseline_videocam)
        val iconOff = ContextCompat.getDrawable(
          this, R.drawable.ic_baseline_videocam_off)

        return if (isEnabled) iconOn as Drawable
        else iconOff as Drawable
    }

    private fun onCameraSwitched()
    {
        this.zoomSDKInstance.videoHelper.switchCamera()
    }

    override fun onUserJoin(
      userHelper: ZoomVideoSDKUserHelper?,
      userList: MutableList<ZoomVideoSDKUser>?
    ) {
      if (userList != null) {
          this.remoteUser = userList[0]
          this.setupVideoFrame()
      }
    }

    private fun setupVideoFrame()
    {
        findViewById<TextView>(R.id.remoteUserName).text = this.remoteUser.userName
        val primaryVideoView = findViewById<ZoomVideoSDKVideoView>(R.id.primaryVideoView)
        val secondaryVideoView = findViewById<ZoomVideoSDKVideoView>(R.id.secondaryVideoView)

        primaryVideoView.visibility = View.VISIBLE
        secondaryVideoView.visibility = View.VISIBLE

        val originalRatio = ZoomVideoSDKVideoAspect.ZoomVideoSDKVideoAspect_Original
        val fullFilledRatio = ZoomVideoSDKVideoAspect.ZoomVideoSDKVideoAspect_Full_Filled

        this.remoteUser.videoCanvas.subscribe(primaryVideoView, originalRatio)
        this.zoomSDKInstance.session.mySelf.videoCanvas.subscribe(secondaryVideoView, fullFilledRatio)
    }

    // TODO: Handle Error
    override fun onError(errorCode: Int)
    {
      Log.d(TAG, "ON_ERROR ${ZoomErrorMsgs.getMessageByCode(errorCode)}")
    }

    // =============================================================================================
    override fun onUserVideoStatusChanged(videoHelper: ZoomVideoSDKVideoHelper?, userList: MutableList<ZoomVideoSDKUser>?) {}
    override fun onUserAudioStatusChanged(audioHelper: ZoomVideoSDKAudioHelper?, userList: MutableList<ZoomVideoSDKUser>?) { }
    override fun onUserActiveAudioChanged(audioHelper: ZoomVideoSDKAudioHelper?, list: MutableList<ZoomVideoSDKUser>?) {}
    override fun onMixedAudioRawDataReceived(rawData: ZoomVideoSDKAudioRawData?) { }
    override fun onOneWayAudioRawDataReceived(rawData: ZoomVideoSDKAudioRawData?, user: ZoomVideoSDKUser?) {}
    override fun onShareAudioRawDataReceived(rawData: ZoomVideoSDKAudioRawData?) {}
    override fun onHostAskUnmute() {}
    override fun onUserLeave(userHelper: ZoomVideoSDKUserHelper?, userList: MutableList<ZoomVideoSDKUser>?) {}
    override fun onUserShareStatusChanged(shareHelper: ZoomVideoSDKShareHelper?, userInfo: ZoomVideoSDKUser?, status: ZoomVideoSDKShareStatus?) {}
    override fun onLiveStreamStatusChanged(liveStreamHelper: ZoomVideoSDKLiveStreamHelper?, status: ZoomVideoSDKLiveStreamStatus?) { }
    override fun onChatNewMessageNotify(chatHelper: ZoomVideoSDKChatHelper?, messageItem: ZoomVideoSDKChatMessage?) {}
    override fun onUserHostChanged(userHelper: ZoomVideoSDKUserHelper?, userInfo: ZoomVideoSDKUser?) {}
    override fun onUserManagerChanged(user: ZoomVideoSDKUser?) {}
    override fun onUserNameChanged(user: ZoomVideoSDKUser?) { }
    override fun onSessionNeedPassword(handler: ZoomVideoSDKPasswordHandler?) { }
    override fun onSessionPasswordWrong(handler: ZoomVideoSDKPasswordHandler?) {}
    override fun onCommandReceived(sender: ZoomVideoSDKUser?, strCmd: String?) {}
    override fun onCommandChannelConnectResult(isSuccess: Boolean) {}
    override fun onCloudRecordingStatus(status: ZoomVideoSDKRecordingStatus?) {}
    override fun onInviteByPhoneStatus(status: ZoomVideoSDKPhoneStatus?, reason: ZoomVideoSDKPhoneFailedReason?) {}
    override fun onSessionLeave() {}
    override fun onSessionJoin() {}
    // =============================================================================================

    override fun onDestroy()
    {
        super.onDestroy()
        zoomSDKInstance.leaveSession(false)
    }
}
