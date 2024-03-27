package com.ing.offroader.ui.activity.main

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.common.util.concurrent.MoreExecutors
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import com.ing.offroader.R
import com.ing.offroader.data.RadioChannelURL
import com.ing.offroader.data.liked.LikedConstants
import com.ing.offroader.databinding.ActivityMainBinding
import com.ing.offroader.ui.activity.chatbot.ChatbotActivity
import com.ing.offroader.ui.activity.main.adapters.HttpItem
import com.ing.offroader.ui.activity.main.adapters.RadioChannelItem
import com.ing.offroader.ui.activity.main.adapters.RadioListAdapter
import com.ing.offroader.ui.activity.main.adapters.ViewPagerAdapter
import com.ing.offroader.ui.activity.main.mediasession.PlaybackService
import com.ing.offroader.ui.activity.sandetail.MyLikedSan
import com.ing.offroader.ui.fragment.community.CommunityFragment
import com.ing.offroader.ui.fragment.home.HomeFragment
import com.ing.offroader.ui.fragment.map.SanMapFragment
import com.ing.offroader.ui.fragment.mydetail.MyDetailFragment
import com.ing.offroader.ui.fragment.sanlist.SanListFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

private const val TAG = "태그 : MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var radioPlayer: ExoPlayer
    private var isRadioLikeTab = true

    private val mainViewModel by viewModels<MainViewModel>()
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
        loadLikedData()
    }

    override fun onRestart() {
        super.onRestart()
        loadLikedData()
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

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Log.d(TAG, "onDetachedFromWindow: ")
    }

    private fun initObserver() {
        mainViewModel.radioLikeList.observe(this) {
            setRadioView(it.size)
        }
    }

    private fun setRadioView(size: Int) = with(binding) {
        if (size == 0 && isRadioLikeTab) {
            tvFavoriteNotify.visibility = View.VISIBLE
            tvFavoriteNotify.text = "즐겨찾기 목록이 없어요..."
        } else {
            tvFavoriteNotify.visibility = View.GONE
            tvFavoriteNotify.text = ""
        }
    }

    private fun initView() {
        setUpListeners()
        setRadioPlayer()
        setBottomNavigation()
        initRadio()
    }


    private fun setBottomNavigation() {

        binding.vpMain.isUserInputEnabled = false

        binding.tlBottomTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position) {
                    2 -> binding.mlMain.setTransition(R.id.trs_empty)
                    3 -> binding.mlMain.setTransition(R.id.trs_empty)
                    else -> binding.mlMain.setTransition(R.id.trs_basic)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })


        val viewPagerAdapter = ViewPagerAdapter(this)
        binding.vpMain.offscreenPageLimit = 5
        binding.vpMain.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tlBottomTab, binding.vpMain) { tab, position ->

            val tabView : View = LayoutInflater.from(this).inflate(R.layout.custum_tab_button, null)

            val tabIcon = tabView.findViewById<ImageView>(R.id.iv_tab_icon)
            val tabTitle = tabView.findViewById<TextView>(R.id.tv_tab_title)


            when(position) {
                0 -> {
                    tabIcon.setImageResource(R.drawable.ic_tab_home_unselected)
                    tabTitle.setText(R.string.tab_home_title)
                    tab.setCustomView(tabView)

                }
                1 -> {
                    tabIcon.setImageResource(R.drawable.ic_tab_san_unselected)
                    tabTitle.setText(R.string.tab_san_list_title)
                    tab.setCustomView(tabView)

                }
                2 -> {
                    tabIcon.setImageResource(R.drawable.ic_tab_map_unselected)
                    tabTitle.setText(R.string.tab_map_title)
                    tab.setCustomView(tabView)
                }
                3 -> {
                    tabIcon.setImageResource(R.drawable.ic_tab_community_unselected)
                    tabTitle.setText(R.string.tab_community_title)
                    tab.setCustomView(tabView)
                }
                4 -> {
                    tabIcon.setImageResource(R.drawable.ic_tab_my_unselected)
                    tabTitle.setText(R.string.tab_my_page_title)
                    tab.setCustomView(tabView)
                }
            }
        }.attach()
    }

    @OptIn(UnstableApi::class)
    private fun setRadioPlayer() {
        radioPlayer = ExoPlayer.Builder(this)
            .setMediaSourceFactory(DefaultMediaSourceFactory(this).setLiveTargetOffsetMs(5000))
            .build()
        binding.viewTest.player = radioPlayer
    }

    //클릭이 아니라 터치를 사용하면 생기는 경고를 무시해주는 Suppress인데 Lint 경고는 성능, 접근성, 국제화, 보안등 코드에서 잠재적인 문제가 발생할 수 있는 부분의 경고를 무시해 줌
    @SuppressLint("ClickableViewAccessibility")
    private fun setUpListeners() {
        //10dp를 픽셀로 변환해 마진으로 사용
        val moveMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, resources.displayMetrics)

        //터치 전의 뷰 위치를 저장
        var initViewLocationX = 0f
        var initViewLocationY = 0f

        //뷰의 내부 터치 위치를 저장
        var touchLocationX = 0f
        var touchLocationY = 0f

        //드래그 상태를 저장
        var isDragging = false

        //플로팅 버튼 이동을 위한 터치 리스너, ClickableViewAccessibility 경고 무시해 줘야 노랑색 경고 없어짐
        binding.fabChatbot.setOnTouchListener { view, event ->
            when (event.action) {

                /** 터치 다운 */
                MotionEvent.ACTION_DOWN -> {
                    //뷰의 현재 위치 좌표를 저장
                    initViewLocationX = view.x
                    initViewLocationY = view.y
                    //터치 위치의 좌표를 저장, event.raw[축]은 화면 좌측 상단을 기준으로 한 좌표를 반환
                    touchLocationX = event.rawX
                    touchLocationY = event.rawY
                    //드래그 상태 초기화, ACTION_UP의 끝에서 false 초기화 해도 되지만 명확한 구분을 위해 ACTION_DOWN 에서 초기화 해줌
                    isDragging = false
                    true
                }

                /** 터치 다운 후 드래그 */
                MotionEvent.ACTION_MOVE -> {
                    //델타값은 실시간으로 변하는 event.raw[축]에서 터치 다운 시 저장했던 위치를 빼 이동량을 저장
                    val deltaX = event.rawX - touchLocationX
                    val deltaY = event.rawY - touchLocationY

                    Log.d(TAG, "플로팅: $deltaX , $deltaY")

                    //위치 마진인 moveMargin을 재활용해 드래그인지 클릭 이벤트인지 구분, 클릭 이벤트가 실행되려면 델타값이 0 이여야 하는데 그러기 쉽지 않음
                    //abs는 absolute value인데 절대값을 반환하는 함수 즉 수의 크기 = 양수로 반환, 현재 위치에서 음 방향으로 이동한 거리도 양수로 반환
                    isDragging = abs(deltaX) > (moveMargin / 2) || abs(deltaY) > (moveMargin / 2) //임시로 %2 해줌
                    if (isDragging) {
                        //터치 다운시 저장했던 위치에 델타만큼 계산해 실시간 이동
                        view.x = initViewLocationX + deltaX
                        view.y = initViewLocationY + deltaY
                    }
                    true
                }
                /** 터치 업 */
                MotionEvent.ACTION_UP -> {
                    if (isDragging) {

                        /** 플로팅 버튼이 화면 밖으로 나가도 돌아올 수 있게 스냅 최종 좌표 설정 */

                        //최종 가로값의 한계 좌표를 지정 + 좌/우의 moveMargin 만큼 간격을 줘서 최종 좌표로 스냅될 수 있도록 좌표를 지정
                        val finalX =
                            when {
                                view.x + view.width / 2 < view.rootView.width / 2 -> moveMargin
                                else -> view.rootView.width - view.width.toFloat() - moveMargin
                            }

                        //최종 세로값의 한계 좌표를 지정
                        //clRadioContainer와 bottomNav를 빼줘야 하는데 민용님 뷰페이저 작업중이라 일단 임시값으로 지정
                        //현재 문제 : 하단 제한값이 적용되지 않고 있고, xml레이아웃에서 초기위치 마진이 tablayout으로 변경된 후 조정할 수 없는 문제가 있음
                        val finalY = max(0f + moveMargin, min(view.y, view.rootView.height - view.height.toFloat() - binding.clRadioContainer.height - binding.tlBottomTab.height - 125- moveMargin))

                        //ACTION_UP 시점 위치에서 계산한 스냅 위치로 애니메이션 이동
                        view.animate()
                            .x(finalX)
                            .y(finalY)
                            .setDuration(300)
                            .start()
                    } else {
                        //드래그 상태가 아닌 경우 클릭으로 간주
                        view.performClick()
                    }
                    true
                }
                //ACTION_[???] 에서 true는 이벤트를 지속적으로 처리하고자 한다를 리스너에게 전달한다고 생각하면 됨
                //뷰의 관점에서 "나는 이 이벤트를 처리했다" = "나는 이 이벤트를 계속해서 수행할 거다"라고 리스너에게 말해주는 것
                //false일 경우 "이 이벤트를 처리했지만 더 이상 수행할 생각이 없다. 다른 뷰에게 수행할 기회를 건내줘라" 라고 생각할 수 있음
                //리스너 하위의 모든 이벤트들이 false를 반환했다면 그 뷰의 루트뷰까지 모두 이벤트를 수행할 생각이 없다고 판단 = 리스너 종료의 시퀸스이다
                else -> false
            }
        }

        binding.fabChatbot.setOnClickListener { view ->
            val intent = Intent(this@MainActivity, ChatbotActivity::class.java)
            startActivity(intent)
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
        // 다시 액티비티가 실행 되었을 때 중첩이 될 수 있는 이쓔가 있을 수 있어서 떼주어야함.
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
            previousFragment = when (tag) {
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

        mainViewModel.isPlaying.observe(this@MainActivity){
            if (it) {
                val item = mainViewModel.httpItem.value
                //playingMarkCurrent(item?.key, item?.position)
            } else {
                playingMarkChange()
            }
        }

        broadcastInit(RadioChannelURL.RADIO_API_URL, R.drawable.ic_favorite)

        cvFavorites.setOnClickListener {
            isRadioLikeTab = true
            mainViewModel.radioLikeList.value?.let { setRadioView(it.size) }

            broadcastInit(
                RadioChannelURL.RADIO_API_URL,
                R.drawable.ic_favorite
            )
        }
        cvKbs.setOnClickListener {
            isRadioLikeTab = false
            setRadioView(0)

            broadcastInit(
                RadioChannelURL.KBS_LIST,
                R.drawable.ic_kbs_radio
            )
        }
        cvSbs.setOnClickListener {
            isRadioLikeTab = false
            setRadioView(0)

            broadcastInit(
                RadioChannelURL.SBS_LIST,
                R.drawable.ic_sbs_radio
            )
        }
        cvMbc.setOnClickListener {
            isRadioLikeTab = false
            setRadioView(0)

            broadcastInit(
                RadioChannelURL.MBC_LIST,
                R.drawable.ic_mbc_radio
            )
        }

        llRadioPlayBtn.setOnClickListener {
            if (mainViewModel.isPlaying.value == true) {
                radioPause()
            } else {
                preparePlayer()
                mainViewModel.whoPlay.value?.let { bottomRadioPlay(it) }
            }
        }

        firstSetting()
    }

    // 처음 앱 시작시 KBS 1Radio로 시작하도록 초기화
    // 추후에 마지막으로 들었던 채널로 시작하도록 구현 예정
    private fun firstSetting() {

        val item = HttpItem(
            "https://cfpwwwapi.kbs.co.kr/api/v1/landing/live/channel_code/21",
            "1Radio",
            R.drawable.ic_kbs_radio,
            0
        )

        mainViewModel.addHttpItem(item)
        CoroutineScope(Dispatchers.Main).launch {
            val channelUrl =
                CoroutineScope(Dispatchers.Default).async {
                    mainViewModel.getHttpNetWork(item)
                }.await()

            mainViewModel.addChannelUrl(channelUrl)
            preparePlayer()
            radioPlay(item.key, item.radioIcon)
            radioPause()
        }
    }

    // 각 방송국들의 채널에 대한 초기화 함수
// RecyclerView 적용, 아이템 클릭 이벤트
    private fun broadcastInit(urlList: Map<String, String>, radioIcon: Int) =
        with(binding) {

            if (urlList == RadioChannelURL.RADIO_API_URL) {
                rvAdapter = RadioListAdapter(mainViewModel)
                rvChannelList.adapter = rvAdapter
                rvAdapter.submitList(mainViewModel.radioLikeList.value?.let {
                    initAdapter(it)
                })
            } else {
                rvAdapter = RadioListAdapter(mainViewModel)
                rvChannelList.adapter = rvAdapter
                rvAdapter.submitList(initAdapter(urlList))
            }

            rvAdapter.itemClick = object : RadioListAdapter.ItemClick {
                override fun onClick(key: String, pos: Int) {
                    if (mainViewModel.whoPlay.value != key) {
                        urlList[key]?.let {

                            val item = HttpItem(it, key, radioIcon, pos)
                            mainViewModel.addHttpItem(item)
                            CoroutineScope(Dispatchers.Main).launch {
                                val channelUrl =
                                    CoroutineScope(Dispatchers.Default).async {
                                        mainViewModel.getHttpNetWork(item)
                                    }.await()

                                mainViewModel.addChannelUrl(channelUrl)
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
                    if (mainViewModel.radioLikeList.value?.contains(key) == true) {
                        mainViewModel.removeList(key)
                        if (isRadioLikeTab)
                            rvAdapter.submitList(mainViewModel.radioLikeList.value?.let { initAdapter(it) })
                    } else {
                        mainViewModel.addList(key)
                    }
                    saveData(
                        RadioChannelURL.PREFERENCE_KEY,
                        RadioChannelURL.DATA_KEY,
                        mainViewModel.radioLikeList.value
                    )
                }
            }
            //rvChannelList.adapter = rvAdapter
        }

    // 현재 재생중인 라디오 채널의 재생중 표시를 제거하고 어댑터를 업데이트 해준다.
    private fun playingMarkChange() {

        rvAdapterList = rvAdapter.currentList.toMutableList()
        val whoPlay = mainViewModel.whoPlay.value.toString()
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
        val test = mainViewModel.channelUrl.value

        mediaItem = test?.let { MediaItem.fromUri(test) }
        mediaItem?.let { item -> viewTest.player?.setMediaItem(item) }
        viewTest.player?.prepare()
        viewTest.player?.playWhenReady = true

    }

    // 하단 라디오 플레이어의 재생 상태를 설정하는 함수
    private fun bottomRadioPlay(key: String?) {
        binding.viewTest.player?.play()
        mainViewModel.checkIsPlaying(true)
        binding.ivRadioPlayBtn.setImageResource(R.drawable.ic_pause)
        mainViewModel.addWhoPlay(key)
    }

    // 라디오를 재생 하고 뷰모델 whoPlay 변수에 어떤 채널이 재생 되고 있는지 저장
    private fun radioPlay(key: String?, icon: Int) = with(binding) {
        viewTest.player?.play()
        mainViewModel.checkIsPlaying(true)
        ivRadioPlayBtn.setImageResource(R.drawable.ic_pause)
        tvBottomRadioTitle.text = key
        ivRadioProfile.setImageResource(icon)
        mainViewModel.addWhoPlay(key)
    }

    // 라디오 정지
    private fun radioPause() = with(binding) {
        viewTest.player?.stop()
        mainViewModel.checkIsPlaying(false)
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
                mainViewModel.loadRadioData(storeMap)
            } catch (e: JsonParseException) {
                e.printStackTrace()
            }
        }
    }

    // 좋아요 SharedPreference
    private fun loadLikedData() {
        val prefs = getSharedPreferences(LikedConstants.LIKED_PREFS, Context.MODE_PRIVATE)
        if (prefs?.contains(LikedConstants.LIKED_PREF_KEY) == true) {
            val gson = Gson()
            val json = prefs.getString(LikedConstants.LIKED_PREF_KEY, "")
            try {
                val type = object : TypeToken<MutableList<MyLikedSan>>() {}.type
                val sanStore: MutableList<MyLikedSan> = gson.fromJson(json, type)
                mainViewModel.loadSanLikedList(sanStore)

                Log.d(TAG, "저장된 목록 = $sanStore")

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