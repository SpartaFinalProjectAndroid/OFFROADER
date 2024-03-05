package com.mit.offroader.ui.activity.main

import android.app.PendingIntent
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mit.offroader.R
import com.mit.offroader.databinding.ActivityMainBinding
import com.mit.offroader.ui.activity.main.adapters.RadioListAdapter
import com.mit.offroader.ui.fragment.chatbot.ChatBotFragment
import com.mit.offroader.ui.fragment.home.HomeFragment
import com.mit.offroader.ui.fragment.map.SanMapFragment
import com.mit.offroader.ui.fragment.mydetail.MyDetailFragment
import com.mit.offroader.ui.fragment.sanlist.SanListFragment
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import com.mit.offroader.data.RadioChannelURL
import com.mit.offroader.data.RetrofitInstance
import com.mit.offroader.ui.activity.main.adapters.RadioChannelItem
import com.mit.offroader.ui.activity.main.adapters.TestAdapater
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


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var radioPlayer : ExoPlayer

    private val radioListViewModel by viewModels<MainViewModel>()
    private var radioUrl : String ?= null
    private var isPlay : Boolean = false
    private var whoPlay : String ?= null

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

    // <-------------------------------- 라디오 관련 설정들 --------------------------------------->
    // 라디오 관련 기능들 초기화
    private fun initRadio() {
        loadData()
        radioSetting()
    }

    fun adapterTest(map: Map<String, String>) : MutableList<RadioChannelItem> {
        val testList : MutableList<RadioChannelItem> = mutableListOf()
        map.keys.forEach {
            testList.add(RadioChannelItem(it))
        }
        return testList
    }

    // 각 방송국과 즐겨찾기 라디오 채널 리스트 초기화
    @OptIn(UnstableApi::class) private fun radioSetting() {

        with(binding) {
            cvFavorites.setOnClickListener {
                val adapter  = radioListViewModel.radioLikeList.value?.let { RadioListAdapter(it, it)}

                adapter?.itemClick = object : RadioListAdapter.ItemClick {
                    override fun onClick(key: String) {
                        if (RadioChannelURL.MBC_LIST.contains(key) || RadioChannelURL.SBS_LIST.contains(key)) {
                            RadioChannelURL.RADIO_API_URL[key]?.let { httpNetWork(it) }
                        } else {
                            RadioChannelURL.RADIO_API_URL[key]?.let { httpNetWork2(it) }
                        }
                        Thread.sleep(500)
                        preparePlayer()
                        radioPlay()
                        binding.tvBottomRadioTitle.text = key
                        ivRadioProfile.setImageResource(R.drawable.ic_kbs_radio)
                    }
                }
                adapter?.heartClick = object : RadioListAdapter.HeartClick {

                    override fun heartClick(key: String) {
                        if (radioListViewModel.radioLikeList.value?.contains(key) == true) {
                            radioListViewModel.removeList(key)
                            saveData()
                        } else {
                            radioListViewModel.addList(key)
                            saveData()
                        }
                    }
                }
                rvChannelList.adapter = adapter
            }

            cvKbs.setOnClickListener {
                val adapter = radioListViewModel.radioLikeList.value?.let { TestAdapater(it) }
                rvChannelList.adapter = adapter
                adapter?.submitList(adapterTest(RadioChannelURL.KBS_LIST))

                //val adapter  = radioListViewModel.radioLikeList.value?.let{ RadioListAdapter(RadioChannelURL.KBS_LIST.keys.toMutableList(), it) }
                adapter?.itemClick = object : TestAdapater.ItemClick {
                    override fun onClick(key: String, pos: Int) {
                        RadioChannelURL.KBS_LIST[key]?.let { httpNetWork2(it) }
                        Thread.sleep(500)
                        preparePlayer()
                        radioPlay(key)
                        binding.tvBottomRadioTitle.text = key
                        ivRadioProfile.setImageResource(R.drawable.ic_kbs_radio)

                        val test = adapter?.currentList?.toMutableList()

                        test?.removeAt(pos)
                        test?.add(pos, RadioChannelItem(key, true))
                        adapter?.submitList(test)
                        Log.i("Minyong", adapter?.currentList?.size.toString())
                    }
                }

                adapter?.heartClick = object : TestAdapater.HeartClick {
                    override fun heartClick(key: String) {
                        if (radioListViewModel.radioLikeList.value?.contains(key) == true) {
                            radioListViewModel.removeList(key)
                            saveData()
                        } else {
                            radioListViewModel.addList(key)
                            saveData()
                        }
                    }
                }

                //rvChannelList.adapter = adapter
            }

            cvSbs.setOnClickListener {
                val adapter  = radioListViewModel.radioLikeList.value?.let { RadioListAdapter(RadioChannelURL.SBS_LIST.keys.toMutableList(), it) }

                adapter?.itemClick = object : RadioListAdapter.ItemClick {
                    override fun onClick(key: String) {
                        RadioChannelURL.SBS_LIST[key]?.let { httpNetWork(it) }
                        Thread.sleep(500)
                        preparePlayer()
                        radioPlay()
                        binding.tvBottomRadioTitle.text = key
                        ivRadioProfile.setImageResource(R.drawable.ic_sbs_radio)
                    }
                }
                adapter?.heartClick = object : RadioListAdapter.HeartClick {
                    override fun heartClick(key: String) {
                        if (radioListViewModel.radioLikeList.value?.contains(key) == true) {
                            radioListViewModel.removeList(key)
                            saveData()
                        } else {
                            radioListViewModel.addList(key)
                            saveData()
                        }
                    }
                }
                rvChannelList.adapter = adapter
            }

            cvMbc.setOnClickListener {
                val adapter  = radioListViewModel.radioLikeList.value?.let { RadioListAdapter(RadioChannelURL.MBC_LIST.keys.toMutableList(), it) }
                adapter?.itemClick = object : RadioListAdapter.ItemClick {
                    override fun onClick(key : String) {
                        RadioChannelURL.MBC_LIST[key]?.let { httpNetWork(it) }
                        Thread.sleep(300)
                        preparePlayer()
                        radioPlay()
                        binding.tvBottomRadioTitle.text = key
                        ivRadioProfile.setImageResource(R.drawable.ic_mbc_radio)
                    }
                }

                adapter?.heartClick = object : RadioListAdapter.HeartClick {
                    override fun heartClick(key: String) {
                        if (radioListViewModel.radioLikeList.value?.contains(key) == true) {
                            radioListViewModel.removeList(key)
                            saveData()
                        } else {
                            radioListViewModel.addList(key)
                            saveData()
                        }
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

    // SBS, MBC 라디오 API 호출 함수(KBS 하고 API 호출 값이 다르기 때문에 따로 함수 구현)
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
    fun httpNetWork2(channelUrl: String) {

        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url(channelUrl)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.i("Minyong", "fail!")
            }

            override fun onResponse(call: Call, response: Response) {
                thread {
                    val jsonObject =
                        JSONTokener(response.body?.string()).nextValue() as JSONObject
                    val url = jsonObject.getJSONArray("channel_item")
                        .getJSONObject(0)
                        .getString("service_url")
                    radioUrl = url
                }
            }
        })
    }

    // API로 받아온 라디오 소스 파일을 Radio Player에 준비
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
    private fun radioPlay(key: String = "") {
        binding.viewTest.player?.play()
        whoPlay = key
        isPlay = true
    }

    // 라디오 정지
    private fun radioPause() {
        binding.viewTest.player?.stop()
        isPlay = false
    }

    // MainViewModel에 있는 좋아요 한 라디오 채널을 담은 radioLikeList를 getSharedPreferences를 사용 하여 로컬에 저장 하기 위한 함수
    private fun saveData() {
        val pref = getSharedPreferences(RadioChannelURL.PREFERENCE_KEY, 0)
        val edit = pref.edit()
        edit.clear()
        val gson = Gson()
        val json = gson.toJson(radioListViewModel.radioLikeList.value)

        edit.putString(RadioChannelURL.DATA_KEY, json)
        edit.apply()
    }

    // getSharedPreferences를 통해서 저장된 값을 가져 와서 MainViewModel에 있는 radioLikeList 값을 저장된 값으로 초기화
    private fun loadData() {
        val pref = getSharedPreferences(RadioChannelURL.PREFERENCE_KEY, 0)
        if (pref.contains(RadioChannelURL.DATA_KEY)) {
            val gson = Gson()
            val json = pref.getString(RadioChannelURL.DATA_KEY, "")
            try {
                val typeToken = object : TypeToken<MutableList<String>>() {}.type
                val storeMap : MutableList<String> = gson.fromJson(json, typeToken)
                radioListViewModel.loadRadioData(storeMap)
            } catch (e: JsonParseException) {
                e.printStackTrace()
            }
        }
    }
}
