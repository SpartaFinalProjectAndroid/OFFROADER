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
import com.mit.offroader.ui.activity.main.adapters.RadioListAdapter
import java.security.Provider.Service

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var radioPlayer : ExoPlayer

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
        radioSetting()
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fl_main, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    // 각 방송국의 라디오 채널 리스트 초기화
    @OptIn(UnstableApi::class) private fun radioSetting() {
        val test : ArrayList<String> = arrayListOf("test", "test2", "", "","","","","","")
        binding.rvChannelList.adapter = RadioListAdapter(test)


        with(binding) {
            cvFavorites.setOnClickListener {
                val test2 : ArrayList<String> = arrayListOf("test", "test2", "", "")
                rvChannelList.adapter = RadioListAdapter(test2)

            }

            cvKbs.setOnClickListener {
                val test3 : ArrayList<String> = arrayListOf("test", "test2", "", "","","","")
                rvChannelList.adapter = RadioListAdapter(test3)
                ivRadioProfile.setImageResource(R.drawable.ic_kbs_radio)
            }

            cvSbs.setOnClickListener {
                val test4 : ArrayList<String> = arrayListOf("test", "test2")
                rvChannelList.adapter = RadioListAdapter(test4)
                ivRadioProfile.setImageResource(R.drawable.ic_sbs_radio)
            }

            cvMbc.setOnClickListener {
                val test5 : ArrayList<String> = arrayListOf("test", "test2", "", "","")
                rvChannelList.adapter = RadioListAdapter(test5)
                ivRadioProfile.setImageResource(R.drawable.ic_mbc_radio)
            }

            ivRadioPlayBtn.setOnClickListener {
                radioPlayer.release()
                val url =  "https://1radio.gscdn.kbs.co.kr/1radio_192_4.m3u8?Expires=1709351911&Policy=eyJTdGF0ZW1lbnQiOlt7IlJlc291cmNlIjoiaHR0cHM6Ly8xcmFkaW8uZ3NjZG4ua2JzLmNvLmtyLzFyYWRpb18xOTJfNC5tM3U4IiwiQ29uZGl0aW9uIjp7IkRhdGVMZXNzVGhhbiI6eyJBV1M6RXBvY2hUaW1lIjoxNzA5MzUxOTExfX19XX0_&Signature=ARjqb4PV~pVAq3AFWPVgDEeGw76xKvTLcTj-w~KsbKvoCdCRXTY8GL1CLV2o9eTO4Mh3UjyLzPUR5uryItMzqmzegTBCeGCsfOrHM-ivWYFbQXTzQ7nbGb7Z1Y2fGmtqsEmVZKj53-RUbwRPF-742S7TbqoB3~P7hVbmLdNW1Fgri3iJLNKD9e9MyrMe0s91gOcnDTHPL-hMOkyVq58gPDp9fATKR0BbBko0T2qC9p33C27wVh4Ts8txvZIuILnrB4aSXM5IgTyujs7trPRYR9GCilAcIYAQiv04vvb4mAqC5v5hpY0xYTdKi0IZHbKHGN9d0OL3vNHIl~8oMUNXiQ__&Key-Pair-Id=APKAICDSGT3Y7IXGJ3TA"

                val mediaItem = MediaItem.fromUri(url)
                radioPlayer.setMediaItem(mediaItem)

                radioPlayer.prepare()
                radioPlayer.play()
            }
        }
    }
}
