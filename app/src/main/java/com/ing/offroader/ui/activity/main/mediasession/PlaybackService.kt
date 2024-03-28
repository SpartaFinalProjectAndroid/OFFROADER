package com.ing.offroader.ui.activity.main.mediasession

import android.content.Context
import android.util.Log
import android.view.Display
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ServiceLifecycleDispatcher
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.google.api.Service

class PlaybackService : MediaSessionService() {

    private var mediaSession : MediaSession? = null

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? = mediaSession

    override fun onCreate() {
        super.onCreate()
        val player = ExoPlayer.Builder(this).build()
        mediaSession = MediaSession.Builder(this, player).build()

    }

    override fun onUpdateNotification(session: MediaSession, startInForegroundRequired: Boolean) {
        super.onUpdateNotification(session, startInForegroundRequired)
    }

    fun isPlay() : Boolean? {
        return mediaSession?.player?.isPlaying
    }

    fun play() {
        mediaSession?.player?.play()
    }

    fun pause() {
        mediaSession?.player?.pause()
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }

}