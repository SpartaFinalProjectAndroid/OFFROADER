package com.mit.offroader.ui.activity.main

<<<<<<< HEAD
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
=======
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
>>>>>>> 54172fc4ffc6a5db5d2785a2505415cb0f4d2f3c
import com.mit.offroader.R
import com.mit.offroader.databinding.ActivityMainBinding
import com.mit.offroader.ui.activity.main.adapters.RadioListAdapter
import com.mit.offroader.ui.fragment.chatbot.ChatBotFragment
import com.mit.offroader.ui.fragment.home.HomeFragment
import com.mit.offroader.ui.fragment.map.SanMapFragment
import com.mit.offroader.ui.fragment.mydetail.MyDetailFragment
import com.mit.offroader.ui.fragment.sanlist.SanListFragment
<<<<<<< HEAD
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
import org.json.JSONTokener
import java.io.IOException
import java.security.Provider.Service
import kotlin.concurrent.thread
=======
>>>>>>> 54172fc4ffc6a5db5d2785a2505415cb0f4d2f3c

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var radioPlayer : ExoPlayer

    private var radioUrl : String ?= null
    private var isPlay : Boolean = false

    @OptIn(UnstableApi::class) override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

<<<<<<< HEAD
        radioPlayer = ExoPlayer.Builder(this)
            .setMediaSourceFactory(DefaultMediaSourceFactory(this).setLiveTargetOffsetMs(5000))
            .build()
        binding.viewTest.player = radioPlayer
=======
>>>>>>> 54172fc4ffc6a5db5d2785a2505415cb0f4d2f3c

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

    override fun onDestroy() {
        super.onDestroy()
        radioPlayer.release()
        binding.viewTest.player?.release()
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fl_main, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    // 라디오 관련 기능들 초기화
    private fun initRadio() {
        radioSetting()
    }

    // 각 방송국의 라디오 채널 리스트 초기화
<<<<<<< HEAD
    @OptIn(UnstableApi::class) private fun radioSetting() {
       // binding.rvChannelList.adapter = RadioListAdapter(test)

        with(binding) {
            cvFavorites.setOnClickListener {
               // rvChannelList.adapter = RadioListAdapter(test2)

            }

            cvKbs.setOnClickListener {

                val adapter  = RadioListAdapter(RadioChannelURL.KBS_LIST.keys.toMutableList())
                adapter.itemClick = object : RadioListAdapter.ItemClick {
                    override fun onClick(key: String) {
                        RadioChannelURL.KBS_LIST[key]?.let { httpNetWork2(it) }
                        Thread.sleep(1000)
                        preparePlayer()
                        radioPlay()
                        binding.tvBottomRadioTitle.text = key
                        ivRadioProfile.setImageResource(R.drawable.ic_kbs_radio)
                    }
                }
                rvChannelList.adapter = adapter
            }

            cvSbs.setOnClickListener {
                val adapter  = RadioListAdapter(RadioChannelURL.SBS_LIST.keys.toMutableList())
                adapter.itemClick = object : RadioListAdapter.ItemClick {
                    override fun onClick(key: String) {
                        RadioChannelURL.SBS_LIST[key]?.let { httpNetWork(it) }
                        Thread.sleep(300)
                        preparePlayer()
                        radioPlay()
                        binding.tvBottomRadioTitle.text = key
                        ivRadioProfile.setImageResource(R.drawable.ic_sbs_radio)
                    }
                }
                rvChannelList.adapter = adapter
            }

            cvMbc.setOnClickListener {
                val adapter  = RadioListAdapter(RadioChannelURL.MBC_LIST.keys.toMutableList())
                adapter.itemClick = object : RadioListAdapter.ItemClick {
                    override fun onClick(key : String) {
                        RadioChannelURL.MBC_LIST[key]?.let { httpNetWork(it) }
                        Thread.sleep(300)
                        preparePlayer()
                        radioPlay()
                        binding.tvBottomRadioTitle.text = key
                        ivRadioProfile.setImageResource(R.drawable.ic_mbc_radio)
                    }
                }
                rvChannelList.adapter = adapter
            }

            ivRadioPlayBtn.setOnClickListener {
                if (isPlay) {
                    radioPause()
                } else {
                    preparePlayer()
                    radioPlay()
                }
            }
        }
    }

    fun httpNetWork(channelUrl : String) {

        val client = OkHttpClient()
        val request : Request = Request.Builder()
            .url(channelUrl)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.i("Minyong", "fail!")
            }

            override fun onResponse(call: Call, response: Response) {
                thread {
                    radioUrl = response.body?.string()
                }
            }
        })
    }

    // KBS 라디오 API 호출 함수 (SBS, MBC 하고 API 호출 값이 다르기 때문에 따로 함수 구현)
    fun httpNetWork2(channelUrl : String) {

        val client = OkHttpClient()
        val request : Request = Request.Builder()
            .url(channelUrl)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.i("Minyong", "fail!")
=======
    private fun radioSetting() {
        val test: ArrayList<String> = arrayListOf("test", "test2", "", "", "", "", "", "", "")
        binding.rvChannelList.adapter = RadioListAdapter(test)

        with(binding) {
            cvFavorites.setOnClickListener {
                val test2: ArrayList<String> = arrayListOf("test", "test2", "", "")
                rvChannelList.adapter = RadioListAdapter(test2)
            }

            cvKbs.setOnClickListener {
                val test3: ArrayList<String> = arrayListOf("test", "test2", "", "", "", "", "")
                rvChannelList.adapter = RadioListAdapter(test3)
                ivRadioProfile.setImageResource(R.drawable.ic_kbs_radio)
            }

            cvSbs.setOnClickListener {
                val test4: ArrayList<String> = arrayListOf("test", "test2")
                rvChannelList.adapter = RadioListAdapter(test4)
                ivRadioProfile.setImageResource(R.drawable.ic_sbs_radio)
            }

            cvMbc.setOnClickListener {
                val test5: ArrayList<String> = arrayListOf("test", "test2", "", "", "")
                rvChannelList.adapter = RadioListAdapter(test5)
                ivRadioProfile.setImageResource(R.drawable.ic_mbc_radio)
>>>>>>> 54172fc4ffc6a5db5d2785a2505415cb0f4d2f3c
            }

            override fun onResponse(call: Call, response: Response) {
                thread {
                    val jsonObject = JSONTokener(response.body?.string()).nextValue() as JSONObject
                    val url = jsonObject.getJSONArray("channel_item")
                        .getJSONObject(0)
                        .getString("service_url")
                    radioUrl = url
                }
            }
        })
    }

    private fun preparePlayer() {
        with(binding) {
            viewTest.player?.stop()
            val mediaItem = radioUrl?.let { MediaItem.fromUri(it) }
            if (mediaItem != null) {
                viewTest.player?.setMediaItem(mediaItem)
            }

            viewTest.player?.prepare()
            viewTest.player?.playWhenReady = true
        }
    }

    // 라디오 재생
    private fun radioPlay() {
        binding.viewTest.player?.play()
        isPlay = true
    }

    // 라디오 정지
    private fun radioPause() {
        binding.viewTest.player?.stop()
        isPlay = false
    }
}
