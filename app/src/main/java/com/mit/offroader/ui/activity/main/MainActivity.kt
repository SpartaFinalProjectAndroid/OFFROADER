package com.mit.offroader.ui.activity.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import com.mit.offroader.R
import com.mit.offroader.data.RadioChannelURL
import com.mit.offroader.databinding.ActivityMainBinding
import com.mit.offroader.ui.activity.main.adapters.RadioChannelItem
import com.mit.offroader.ui.activity.main.adapters.RadioListAdapter
import com.mit.offroader.ui.fragment.chatbot.ChatBotFragment
import com.mit.offroader.ui.fragment.home.HomeFragment
import com.mit.offroader.ui.fragment.map.SanMapFragment
import com.mit.offroader.ui.fragment.mydetail.MyDetailFragment
import com.mit.offroader.ui.fragment.sanlist.SanListFragment
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var radioPlayer : ExoPlayer

    private val radioListViewModel by viewModels<MainViewModel>()
    private var radioUrl : String ?= null
    private var isPlay : Boolean = false

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

                    enableStatusBarTrans()

                    true
                }

                R.id.navigation_3 -> {
                    replaceFragment(SanMapFragment())
                    true
                }

                R.id.navigation_4 -> {
                    replaceFragment(ChatBotFragment())

                    disableStatusBarTrans()

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

    // 각 방송국과 즐겨찾기 라디오 채널 리스트 초기화
    @OptIn(UnstableApi::class) private fun radioSetting() = with(binding) {

        favoriteInit()
        firstSetting()

        cvFavorites.setOnClickListener { favoriteInit() }
        cvKbs.setOnClickListener { kbsInit() }
        cvSbs.setOnClickListener { sbsInit() }
        cvMbc.setOnClickListener { mbcInit() }

        llRadioPlayBtn.setOnClickListener {
            if (isPlay) { radioPause() }
            else {
                preparePlayer()
                radioListViewModel.whoPlay.value?.let { radioPlay(it) }
            }
        }
    }

    // 처음 앱 시작시 KBS 1Radio로 시작하도록 초기화
    // 추후에 마지막으로 들었던 채널로 시작하도록 구현 예정
    private fun firstSetting() {
        RadioChannelURL.KBS_LIST["1Radio"]?.let { httpNetWork2(it) }
        Thread.sleep(500)
        preparePlayer()
        radioPause()
        radioListViewModel.addWhoPlay("1Radio")
        binding.ivRadioProfile.setImageResource(R.drawable.ic_kbs_radio)
    }

    // 즐겨찾기 리사이클러뷰 리스트 초기화
    private fun favoriteInit() = with(binding) {
        val adapter = RadioListAdapter(radioListViewModel)
        rvChannelList.adapter = adapter
        adapter.submitList(radioListViewModel.radioLikeList.value?.let { initAdapter(it) })

        adapter.itemClick = object : RadioListAdapter.ItemClick {
            override fun onClick(key: String, pos: Int) {
                val whoPlay = radioListViewModel.whoPlay.value
                if (whoPlay != key) {
                    if (RadioChannelURL.MBC_LIST.contains(key) || RadioChannelURL.SBS_LIST.contains(key)) {
                        RadioChannelURL.RADIO_API_URL[key]?.let { httpNetWork(it) }
                    } else {
                        RadioChannelURL.RADIO_API_URL[key]?.let { httpNetWork2(it) }
                    }
                    Thread.sleep(500)
                    preparePlayer()
                    binding.tvBottomRadioTitle.text = key
                    ivRadioProfile.setImageResource(R.drawable.ic_favorite)

                    val adapterList = adapter.currentList.toMutableList()
                    if (adapterList.contains(RadioChannelItem(whoPlay.toString(), true))) {
                        val where = adapterList.indexOf(RadioChannelItem(whoPlay.toString(), true))
                        adapterList.remove(RadioChannelItem(whoPlay.toString(), true))
                        adapterList.add(where, RadioChannelItem(whoPlay.toString()))
                    }
                    radioPlay(key)
                    adapterList.removeAt(pos)
                    adapterList.add(pos, RadioChannelItem(key, true))

                    adapter.submitList(adapterList)
                }
            }
        }
        adapter.heartClick = object : RadioListAdapter.HeartClick {

            override fun heartClick(key: String) {
                if (radioListViewModel.radioLikeList.value?.contains(key) == true) {
                    radioListViewModel.removeList(key)
                    adapter.submitList(radioListViewModel.radioLikeList.value?.let { initAdapter(it) })
                    saveData()
                } else {
                    radioListViewModel.addList(key)
                    saveData()
                }
            }
        }
        rvChannelList.adapter = adapter
    }

    // KBS 리사이클러뷰 리스트 초기화
    private fun kbsInit() = with(binding) {
        val adapter = RadioListAdapter(radioListViewModel)
        rvChannelList.adapter = adapter
        adapter.submitList(initAdapter(RadioChannelURL.KBS_LIST))

        adapter.itemClick = object : RadioListAdapter.ItemClick {
            override fun onClick(key: String, pos: Int) {
                val whoPlay = radioListViewModel.whoPlay.value
                if (whoPlay != key) {
                    RadioChannelURL.KBS_LIST[key]?.let { httpNetWork2(it) }
                    Thread.sleep(500)
                    preparePlayer()
                    binding.tvBottomRadioTitle.text = key
                    ivRadioProfile.setImageResource(R.drawable.ic_kbs_radio)

                    val adapterList = adapter.currentList.toMutableList()
                    if (adapterList.contains(RadioChannelItem(whoPlay.toString(), true))) {
                        val where = adapterList.indexOf(RadioChannelItem(whoPlay.toString(), true))
                        adapterList.remove(RadioChannelItem(whoPlay.toString(), true))
                        adapterList.add(where, RadioChannelItem(whoPlay.toString()))
                    }
                    radioPlay(key)
                    adapterList.removeAt(pos)
                    adapterList.add(pos, RadioChannelItem(key, true))

                    adapter.submitList(adapterList)
                }
            }
        }
        adapter.heartClick = object : RadioListAdapter.HeartClick {
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
    }

    // SBS 리사이클러뷰 리스트 초기화
    private fun sbsInit() = with(binding){
        val adapter = RadioListAdapter(radioListViewModel)
        rvChannelList.adapter = adapter
        adapter.submitList(initAdapter(RadioChannelURL.SBS_LIST))

        adapter.itemClick = object : RadioListAdapter.ItemClick {
            override fun onClick(key: String, pos: Int) {

                val whoPlay = radioListViewModel.whoPlay.value

                if (whoPlay != key) {
                    RadioChannelURL.SBS_LIST[key]?.let { httpNetWork(it) }
                    Thread.sleep(500)
                    preparePlayer()
                    binding.tvBottomRadioTitle.text = key
                    ivRadioProfile.setImageResource(R.drawable.ic_sbs_radio)

                    val adapterList = adapter.currentList.toMutableList()

                    if (adapterList.contains(RadioChannelItem(whoPlay.toString(), true))) {
                        val where = adapterList.indexOf(RadioChannelItem(whoPlay.toString(), true))
                        adapterList.remove(RadioChannelItem(whoPlay.toString(), true))
                        adapterList.add(where, RadioChannelItem(whoPlay.toString()))
                    }

                    radioPlay(key)
                    adapterList.removeAt(pos)
                    adapterList.add(pos, RadioChannelItem(key, true))

                    adapter.submitList(adapterList)

                }
            }
        }
        adapter.heartClick = object : RadioListAdapter.HeartClick {
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

    // MBC 리사이클러뷰 리스트 초기화
    private fun mbcInit() = with(binding) {
        val adapter = RadioListAdapter(radioListViewModel)
        rvChannelList.adapter = adapter
        adapter.submitList(initAdapter(RadioChannelURL.MBC_LIST))

        adapter.itemClick = object : RadioListAdapter.ItemClick {
            override fun onClick(key : String, pos: Int) {

                val whoPlay = radioListViewModel.whoPlay.value

                if (whoPlay != key) {
                    RadioChannelURL.MBC_LIST[key]?.let { httpNetWork(it) }
                    Thread.sleep(300)
                    preparePlayer()
                    binding.tvBottomRadioTitle.text = key
                    ivRadioProfile.setImageResource(R.drawable.ic_mbc_radio)

                    val adapterList = adapter.currentList.toMutableList()

                    if (adapterList.contains(RadioChannelItem(whoPlay.toString(), true))) {
                        val where = adapterList.indexOf(RadioChannelItem(whoPlay.toString(), true))
                        adapterList.remove(RadioChannelItem(whoPlay.toString(), true))
                        adapterList.add(where, RadioChannelItem(whoPlay.toString()))
                    }

                    radioPlay(key)
                    adapterList.removeAt(pos)
                    adapterList.add(pos, RadioChannelItem(key, true))

                    adapter.submitList(adapterList)
                }
            }
        }

        adapter.heartClick = object : RadioListAdapter.HeartClick {
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

    // RadioListAdapter 초기화 함수
    // 즐겨찾기
    private fun initAdapter(map: Map<String, String>) : MutableList<RadioChannelItem> {
        val channelItemList : MutableList<RadioChannelItem> = mutableListOf()
        map.keys.forEach { channelItemList.add(RadioChannelItem(it)) }
        return channelItemList
    }

    // RadioListAdapter 초기화 함수
    private fun initAdapter(list: MutableList<String>) : MutableList<RadioChannelItem> {
        val channelItemList : MutableList<RadioChannelItem> = mutableListOf()
        list.forEach { channelItemList.add(RadioChannelItem(it)) }
        return channelItemList
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
                thread { radioUrl = response.body?.string() }
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

    // 라디오를 재생 하고 뷰모델 whoPlay 변수에 어떤 채널이 재생 되고 있는지 저장
    private fun radioPlay(key: String = "") {
        binding.viewTest.player?.play()
        binding.ivRadioPlayBtn.setImageResource(R.drawable.ic_pause)
        isPlay = true
        radioListViewModel.addWhoPlay(key)
    }

    // 라디오 정지
    private fun radioPause() {
        binding.viewTest.player?.stop()
        binding.ivRadioPlayBtn.setImageResource(R.drawable.ic_play)
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

    private fun enableStatusBarTrans(){
        window.statusBarColor = ContextCompat.getColor(this, R.color.transparent)
        //전체화면으로 설정하면 상단 parent 아이콘 배치 margin 주어야 함 안그러면 상태바 아래로 기어드감
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        //보고 필요하면 상태바 아이콘 어둡게
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    private fun disableStatusBarTrans(){
        window.statusBarColor = ContextCompat.getColor(this, R.color.offroader_background)
        //전체화면으로 설정하면 상단 parent 아이콘 배치 margin 주어야 함 안그러면 상태바 아래로 기어드감
        window.decorView.systemUiVisibility = 0
        //보고 필요하면 상태바 아이콘 어둡게
//        window.decorView.systemUiVisibility = 8191
    }



}