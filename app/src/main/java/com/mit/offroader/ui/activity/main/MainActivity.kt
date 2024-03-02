package com.mit.offroader.ui.activity.main

import android.app.PendingIntent
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.OptIn
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.rtsp.RtspMediaSource
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.session.MediaSession
import com.mit.offroader.R
import com.mit.offroader.databinding.ActivityMainBinding
import com.mit.offroader.ui.fragment.chatbot.ChatBotFragment
import com.mit.offroader.ui.fragment.home.HomeFragment
import com.mit.offroader.ui.fragment.map.SanMapFragment
import com.mit.offroader.ui.fragment.mydetail.MyDetailFragment
import com.mit.offroader.ui.fragment.sanlist.SanListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.mit.offroader.data.RadioChannelURL
import com.mit.offroader.data.RetrofitInstance
import com.mit.offroader.ui.activity.main.adapters.RadioListAdapter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.security.Provider.Service
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var radioPlayer : ExoPlayer

    private var radioUrl : String ?= null

    @OptIn(UnstableApi::class) override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        radioPlayer = ExoPlayer.Builder(this)
            .setMediaSourceFactory(DefaultMediaSourceFactory(this).setLiveTargetOffsetMs(5000))
            .build()
        binding.viewTest.player = radioPlayer

        bottomNavigationView = binding.navMain

        replaceFragment(HomeFragment())

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            // 아이콘 색상 변경

            // 각 아이템에 따라 적절한 작업 수행
            when (menuItem.itemId) {
                R.id.navigation_1 -> {
                    replaceFragment(HomeFragment())
                    //애니메이션 쓸거면 여기
                    true
                }
                R.id.navigation_2 -> {
                    replaceFragment(SanListFragment())
                    true
                }
                R.id.navigation_3 -> {
                    replaceFragment(SanMapFragment())
                    true
                }
                R.id.navigation_4 -> {
                    replaceFragment(ChatBotFragment())
                    true
                }
                R.id.navigation_5 -> {
                    replaceFragment(MyDetailFragment())
                    true
                }
                else -> false
            }
        }

        initRadio()

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fl_main, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    private fun initRadio() {
        radioSetting()
    }

    // 각 방송국의 라디오 채널 리스트 초기화
    @OptIn(UnstableApi::class) private fun radioSetting() {
        val test : ArrayList<String> = arrayListOf("test", "test2", "", "","","","","","")
       // binding.rvChannelList.adapter = RadioListAdapter(test)


        with(binding) {
            cvFavorites.setOnClickListener {
               // rvChannelList.adapter = RadioListAdapter(test2)

            }

            cvKbs.setOnClickListener {
                //rvChannelList.adapter = RadioListAdapter(test3)
                ivRadioProfile.setImageResource(R.drawable.ic_kbs_radio)
            }

            cvSbs.setOnClickListener {
                ivRadioProfile.setImageResource(R.drawable.ic_sbs_radio)

                val adapter  = RadioListAdapter(RadioChannelURL.SBS_LIST.keys.toMutableList())
                adapter.itemClick = object : RadioListAdapter.ItemClick {
                    override fun onClick(key : String) {
                        RadioChannelURL.SBS_LIST[key]?.let { httpNetWork(it) }
                        preparePlayer()
                        radioPlay()
                    }
                }
                rvChannelList.adapter = adapter
            }

            cvMbc.setOnClickListener {
                ivRadioProfile.setImageResource(R.drawable.ic_mbc_radio)

                val adapter  = RadioListAdapter(RadioChannelURL.MBC_LIST.keys.toMutableList())
                adapter.itemClick = object : RadioListAdapter.ItemClick {
                    override fun onClick(key : String) {
                        var test2 : String ?= null
//                        RadioChannelURL.MBC_LIST[key]?.let { httpNetWork(it) }
//                        preparePlayer()
//                        radioPlay()
                        RadioChannelURL.MBC_LIST[key]?.let { test2 = httpNetWork(it) }
                        testRadioPlay(test2)
                    }
                }
                rvChannelList.adapter = adapter
            }

            ivRadioPlayBtn.setOnClickListener {

                //radioPlayer.pause()

                val mediaItem = radioUrl?.let { MediaItem.fromUri(it) }
                if (mediaItem != null) {
                    radioPlayer.setMediaItem(mediaItem)
                }

                radioPlayer.prepare()
                radioPlayer.play()
            }
        }
    }

    fun httpNetWork(channelUrl : String) : String? {

        var test : String ?= null

        val client = OkHttpClient()
        val request : Request = Request.Builder()
            .url(channelUrl)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.i("Minyong", "fail!")
            }

            override fun onResponse(call: Call, response: Response) {
                //thread {
                    //radioUrl = response.body?.string()
                test = response.body?.string()
                radioUrl = test
                //}
            }
        })

        return test
    }

    private fun preparePlayer() {
        binding.viewTest.player?.stop()
        val mediaItem = radioUrl?.let { MediaItem.fromUri(it) }
        if (mediaItem != null) {
            binding.viewTest.player?.setMediaItem(mediaItem)
        }

        binding.viewTest.player?.prepare()
        binding.viewTest.player?.playWhenReady = true
    }

    private fun radioPlay() {
        binding.viewTest.player?.play()
    }

    private fun testRadioPlay(url : String?) {
        //binding.viewTest.player?.stop()
        val mediaItem = url?.let { MediaItem.fromUri(it) }
        if (mediaItem != null) {
            binding.viewTest.player?.setMediaItem(mediaItem)
        }

        binding.viewTest.player?.prepare()
        binding.viewTest.player?.playWhenReady = true
        binding.viewTest.player?.play()
    }
}
