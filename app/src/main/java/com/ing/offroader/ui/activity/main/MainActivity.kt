package com.ing.offroader.ui.activity.main

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionToken
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.common.util.concurrent.MoreExecutors
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import com.ing.offroader.R
import com.ing.offroader.data.RadioChannelURL
import com.ing.offroader.databinding.ActivityMainBinding
import com.ing.offroader.ui.activity.chatbot.ChatbotActivity
import com.ing.offroader.ui.activity.main.adapters.HttpItem
import com.ing.offroader.ui.activity.main.adapters.RadioChannelItem
import com.ing.offroader.ui.activity.main.adapters.RadioListAdapter
import com.ing.offroader.ui.activity.main.mediasession.PlaybackService
import com.ing.offroader.ui.fragment.community.CommunityFragment
import com.ing.offroader.ui.fragment.home.HomeFragment
import com.ing.offroader.ui.fragment.map.SanMapFragment
import com.ing.offroader.ui.fragment.mydetail.MyDetailFragment
import com.ing.offroader.ui.fragment.sanlist.SanListFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

private const val TAG = "태그 : MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var radioPlayer: ExoPlayer
    private var isRadioLikeTab = true

    private val radioListViewModel by viewModels<MainViewModel>()
    private var lastTimeBackPressed: Long = -1500

    private lateinit var rvAdapter: RadioListAdapter
    private lateinit var rvAdapterList: MutableList<RadioChannelItem>


    override fun onStart() {
        super.onStart()

        val sessionToken = SessionToken(this, ComponentName(this, PlaybackService::class.java))
        val controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()

        controllerFuture.addListener(
            { binding.viewTest.player = controllerFuture.get() },
            MoreExecutors.directExecutor()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initObserver()
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
    }
    private fun initObserver() {
        radioListViewModel.radioLikeList.observe(this) {
            setRadioView(it.size)
        }
    }

    private fun setRadioView(size: Int) {
        if (size == 0 && isRadioLikeTab) {
            binding.tvFavoriteNotify.visibility = View.VISIBLE
            binding.tvFavoriteNotify.text = "즐겨찾기 목록이 없습니다."
        } else {
            binding.tvFavoriteNotify.visibility = View.GONE
            binding.tvFavoriteNotify.text = ""
        }
    }

    private fun initView() {
        setUpListeners()
        setRadioPlayer()
        setBottomNavigation()
        initRadio()
    }

    private fun setBottomNavigation() {
        bottomNavigationView = binding.navMain

//        replaceFragment(HomeFragment())
        showFragment(HomeFragment(), "HOME_FRAGMENT")

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            // 아이콘 색상 변경


            // 각 아이템에 따라 적절한 작업 수행
            when (menuItem.itemId) {
                R.id.navigation_1 -> {
                    showFragment(HomeFragment(), "HOME_FRAGMENT")
                    binding.mlMain.setTransition(R.id.trs_basic)
                    binding.mlMain.transitionToStart()
                    true
                }
                R.id.navigation_2 -> {
                    showFragment(SanListFragment(), "SAN_LIST_FRAGMENT")
                    binding.mlMain.setTransition(R.id.trs_basic)
                    binding.mlMain.transitionToStart()
                    true
                }
                R.id.navigation_3 -> {
                    showFragment(SanMapFragment(), "SAN_MAP_FRAGMENT")
                    binding.mlMain.setTransition(R.id.trs_empty)
                    binding.mlMain.transitionToStart()

                    true
                }
                R.id.navigation_4 -> {
                    showFragment(CommunityFragment(), "CHAT_BOT_FRAGMENT")
                    binding.mlMain.setTransition(R.id.trs_empty)
                    binding.mlMain.transitionToStart()
                    true
                }
                R.id.navigation_5 -> {
                    showFragment(MyDetailFragment(), "MY_DETAIL_FRAGMENT")
                    binding.mlMain.setTransition(R.id.trs_basic)
                    binding.mlMain.transitionToStart()
                    true
                }
                else -> false
            }
        }
    }

    @OptIn(UnstableApi::class)
    private fun setRadioPlayer() {
        radioPlayer = ExoPlayer.Builder(this)
            .setMediaSourceFactory(DefaultMediaSourceFactory(this).setLiveTargetOffsetMs(5000))
            .build()
        binding.viewTest.player = radioPlayer
    }

    private fun setUpListeners() {
        binding.fabChatbot.setOnClickListener {
            val intent = Intent(this, ChatbotActivity::class.java)
            startActivity(intent)

        }
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
    private fun showFragment(fragment: Fragment, tag: String) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        var previousFragment = supportFragmentManager.findFragmentByTag(tag)
        if (previousFragment == null) {
            previousFragment = when(tag) {
                "HOME_FRAGMENT" -> HomeFragment()
                "SAN_LIST_FRAGMENT" -> SanListFragment()
                "SAN_MAP_FRAGMENT" -> SanMapFragment()
                "CHAT_BOT_FRAGMENT" -> CommunityFragment()
                "MY_DETAIL_FRAGMENT" -> MyDetailFragment()
                else -> null
            }

            previousFragment?.let { fragmentTransaction.add(R.id.fl_main, it, tag) }
        }

        val currentFragments = supportFragmentManager.fragments
        for (fragment in currentFragments) {
            if (fragment != previousFragment) {
                fragmentTransaction.hide(fragment)
            }
        }

        previousFragment?.let { fragmentTransaction.show(it) }

        fragmentTransaction.commit()
    }

    // <-------------------------------- 라디오 관련 설정들 --------------------------------------->

    // 라디오 관련 기능들 초기화
    private fun initRadio() {
        loadData()
        radioSetting()

        binding.ivRadioBackBtn.setOnClickListener {
            binding.mlMain.transitionToStart()
        }
    }

    // 각 방송국과 즐겨찾기 라디오 채널 리스트 초기화
    @OptIn(UnstableApi::class)
    private fun radioSetting() = with(binding) {

        radioListViewModel.isPlaying.observe(this@MainActivity){
            if (it) {
                val item = radioListViewModel.httpItem.value
                //playingMarkCurrent(item?.key, item?.position)
            } else {
                playingMarkChange()
            }
        }

        firstSetting()
        broadcastInit(RadioChannelURL.RADIO_API_URL, R.drawable.ic_favorite)

        cvFavorites.setOnClickListener {
            isRadioLikeTab = true
            broadcastInit(
                RadioChannelURL.RADIO_API_URL,
                R.drawable.ic_favorite
            )
        }
        cvKbs.setOnClickListener {
            isRadioLikeTab = false
            broadcastInit(
                RadioChannelURL.KBS_LIST,
                R.drawable.ic_kbs_radio
            )
        }
        cvSbs.setOnClickListener {
            isRadioLikeTab = false
            broadcastInit(
                RadioChannelURL.SBS_LIST,
                R.drawable.ic_sbs_radio
            )
        }
        cvMbc.setOnClickListener {
            isRadioLikeTab = false
            broadcastInit(
                RadioChannelURL.MBC_LIST,
                R.drawable.ic_mbc_radio
            )
        }

        llRadioPlayBtn.setOnClickListener {
            if (radioListViewModel.isPlaying.value == true) {
                radioPause()
            } else {
                preparePlayer()
                radioListViewModel.whoPlay.value?.let { bottomRadioPlay(it) }
            }
        }
    }

    // 처음 앱 시작시 KBS 1Radio로 시작하도록 초기화
// 추후에 마지막으로 들었던 채널로 시작하도록 구현 예정
    private fun firstSetting() {
        if (radioListViewModel.isPlaying.value == true) {
            preparePlayer()
            radioPlay("test", R.drawable.ic_favorite)
        } else {
            preparePlayer()
        }
    }

    // 각 방송국들의 채널에 대한 초기화 함수
// RecyclerView 적용, 아이템 클릭 이벤트
    private fun broadcastInit(urlList: Map<String, String>, radioIcon: Int) =
        with(binding) {

            if (urlList == RadioChannelURL.RADIO_API_URL) {
                rvAdapter = RadioListAdapter(radioListViewModel)
                rvChannelList.adapter = rvAdapter
                rvAdapter.submitList(radioListViewModel.radioLikeList.value?.let {
                    initAdapter(
                        it
                    )
                })
            } else {
                rvAdapter = RadioListAdapter(radioListViewModel)
                rvChannelList.adapter = rvAdapter
                rvAdapter.submitList(initAdapter(urlList))
            }

            rvAdapter.itemClick = object : RadioListAdapter.ItemClick {
                override fun onClick(key: String, pos: Int) {
                    if (radioListViewModel.whoPlay.value != key) {
                        urlList[key]?.let {

                            val item = HttpItem(it, key, radioIcon, pos)
                            radioListViewModel.addHttpItem(item)
                            CoroutineScope(Dispatchers.Main).launch {
                                val channelUrl =
                                    CoroutineScope(Dispatchers.Default).async {
                                        radioListViewModel.getHttpNetWork(item)
                                    }.await()

                                radioListViewModel.addChannelUrl(channelUrl)
                                preparePlayer()
                                playingMarkChange()
                                radioPlay(item.key, item.radioIcon)
                                playingMarkCurrent(item.key, item.position)
                            }
                        }
                    }
                }
            }

            rvAdapter.heartClick = object : RadioListAdapter.HeartClick {
                override fun heartClick(key: String) {
                    if (radioListViewModel.radioLikeList.value?.contains(key) == true) {
                        radioListViewModel.removeList(key)
                        //rvAdapter.submitList(radioListViewModel.radioLikeList.value?.let { initAdapter(it) })
                    } else {
                        radioListViewModel.addList(key)
                    }
                    saveData(
                        RadioChannelURL.PREFERENCE_KEY,
                        RadioChannelURL.DATA_KEY,
                        radioListViewModel.radioLikeList.value
                    )
                }
            }
            rvChannelList.adapter = rvAdapter
        }

    // 현재 재생중인 라디오 채널의 재생중 표시를 제거하고 어댑터를 업데이트 해준다.
    private fun playingMarkChange() {

        rvAdapterList = rvAdapter.currentList.toMutableList()
        val whoPlay = radioListViewModel.whoPlay.value.toString()
        val playChannel = RadioChannelItem(whoPlay, true) // 현재 재생중인 라디오 채널

        if (rvAdapterList.contains(playChannel)) {
            val where = rvAdapterList.indexOf(playChannel)
            rvAdapterList.remove(playChannel)
            rvAdapterList.add(where, RadioChannelItem(whoPlay, false))
        }
        rvAdapter.submitList(rvAdapterList) // 어뎁터 업데이트
    }


    // 클릭한 라디오 채널의 재생중 표시를 나타내기 위한 함수
    private fun playingMarkCurrent(key: String, pos: Int) {
        rvAdapterList.removeAt(pos)
        rvAdapterList.add(pos, RadioChannelItem(key, true))
        rvAdapter.submitList(rvAdapterList)
    }

    // RadioListAdapter 초기화 함수
// 즐겨찾기
    private fun initAdapter(map: Map<String, String>): MutableList<RadioChannelItem> {
        val channelItemList: MutableList<RadioChannelItem> = mutableListOf()
        map.keys.forEach { channelItemList.add(RadioChannelItem(it)) }
        return channelItemList
    }

    // RadioListAdapter 초기화 함수
    private fun initAdapter(list: MutableList<String>): MutableList<RadioChannelItem> {
        val channelItemList: MutableList<RadioChannelItem> = mutableListOf()
        list.forEach { channelItemList.add(RadioChannelItem(it)) }
        return channelItemList
    }


    // API로 받아온 라디오 소스 파일을 Radio Player에 준비
    private fun preparePlayer() = with(binding) {
        var mediaItem: MediaItem? = null

        viewTest.player?.stop()
        //radioListViewModel.addChannelUrl(radioUrl.toString())
        val test = radioListViewModel.channelUrl.value

        mediaItem = test?.let { MediaItem.fromUri(test) }
        mediaItem?.let { item -> viewTest.player?.setMediaItem(item) }
        viewTest.player?.prepare()
        viewTest.player?.playWhenReady = true

    }

    // 하단 라디오 플레이어의 재생 상태를 설정하는 함수
    private fun bottomRadioPlay(key: String?) {
        binding.viewTest.player?.play()
        radioListViewModel.checkIsPlaying(true)
        binding.ivRadioPlayBtn.setImageResource(R.drawable.ic_pause)
        radioListViewModel.addWhoPlay(key)
    }

    // 라디오를 재생 하고 뷰모델 whoPlay 변수에 어떤 채널이 재생 되고 있는지 저장
    private fun radioPlay(key: String?, icon: Int) = with(binding) {
        viewTest.player?.play()
        radioListViewModel.checkIsPlaying(true)
        ivRadioPlayBtn.setImageResource(R.drawable.ic_pause)
        tvBottomRadioTitle.text = key
        ivRadioProfile.setImageResource(icon)
        radioListViewModel.addWhoPlay(key)
    }

    // 라디오 정지
    private fun radioPause() = with(binding) {
        viewTest.player?.stop()
        radioListViewModel.checkIsPlaying(false)
        ivRadioPlayBtn.setImageResource(R.drawable.ic_play)
    }

    // MainViewModel에 있는 좋아요 한 라디오 채널을 담은 radioLikeList를 getSharedPreferences를 사용 하여 로컬에 저장 하기 위한 함수
    private fun <T> saveData(preferKey: String, dataKey: String, data: T) {
        val pref = getSharedPreferences(preferKey, 0)
        val edit = pref.edit()
        edit.clear()
        val gson = Gson()
        //val json = gson.toJson(radioListViewModel.radioLikeList.value)
        val json = gson.toJson(data)

        edit.putString(dataKey, json)
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
                val storeMap: MutableList<String> = gson.fromJson(json, typeToken)
                radioListViewModel.loadRadioData(storeMap)
            } catch (e: JsonParseException) {
                e.printStackTrace()
            }
        }
    }

    private fun enableStatusBarTrans() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.transparent)
        //전체화면으로 설정하면 상단 parent 아이콘 배치 margin 주어야 함 안그러면 상태바 아래로 기어드감
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        //보고 필요하면 상태바 아이콘 어둡게
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    private fun disableStatusBarTrans() {
        window.statusBarColor =
            ContextCompat.getColor(this, R.color.offroader_background)
        //전체화면으로 설정하면 상단 parent 아이콘 배치 margin 주어야 함 안그러면 상태바 아래로 기어드감
        window.decorView.systemUiVisibility = 0
        //보고 필요하면 상태바 아이콘 어둡게
//        window.decorView.systemUiVisibility = 8191
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        // (현재 버튼 누른 시간-이전에 버튼 누른 시간) <=1.5초일 때 동작
        if (System.currentTimeMillis() - lastTimeBackPressed <= 1500)
            finish()
        lastTimeBackPressed = System.currentTimeMillis()
        Toast.makeText(this, "이전 버튼을 한 번 더 누르면 종료됩니다", Toast.LENGTH_SHORT).show()
    }

}