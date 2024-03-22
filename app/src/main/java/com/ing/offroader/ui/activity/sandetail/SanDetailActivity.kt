package com.ing.offroader.ui.activity.sandetail

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.appbar.AppBarLayout
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import com.ing.offroader.R
import com.ing.offroader.data.liked.LikedConstants
import com.ing.offroader.data.model.weather.WeekendWeatherData
import com.ing.offroader.data.repository.WeatherRepository
import com.ing.offroader.databinding.ActivitySanDetailBinding
import com.ing.offroader.ui.activity.sandetail.viewmodel.SanDetailViewModel
import com.ing.offroader.ui.activity.sandetail.viewmodel.SanDetailViewModelFactory
import com.ing.offroader.ui.fragment.community.MyApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import kotlin.math.abs

private const val TAG = "SanDetailActivity"

class SanDetailActivity : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener {
    private var _binding: ActivitySanDetailBinding? = null
    private val binding get() = _binding!!

    // 자동 스크롤
    private val slideImageHandler: Handler = Handler()
    private val slideImageRunnable =
        Runnable { binding.vpMountain.currentItem = binding.vpMountain.currentItem + 1 }

    private val sanDetailViewModel: SanDetailViewModel by viewModels {
        return@viewModels SanDetailViewModelFactory(
            (application as MyApplication).sanListRepository
        )
    }

    /** 날씨 */
    private var weatherData = mutableListOf<WeekendWeatherData>()
    private val netWorkRepository = WeatherRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySanDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //스크롤 오프셋을 감지하는 리스너를 툴바에 연결
        binding.appbar.addOnOffsetChangedListener(this)

        window.statusBarColor = ContextCompat.getColor(this, R.color.transparent)
        //전체화면으로 설정하면 상단 parent 아이콘 배치 margin 주어야 함 안그러면 상태바 아래로 기어드감
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        //보고 필요하면 상태바 아이콘 어둡게
        //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        sanDetailViewModel.getSelectedSan(getSanName())
//        Log.d(TAG, "산이름 : ${sanName}")

        loadData()
        initBackButton()
        initObserver()
    }

    //본문 읽는 중 사진 자동 스크롤 중지
    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        //화면 세로 오프셋 절대값과 앱바의 위치가 동인한지 확인, abs는 절대값을 구하는 함수
        val isToolbarCollapsed = abs(verticalOffset) == appBarLayout.totalScrollRange

        //툴바가 상단에 닿았을 경우 멈춤
        //아닌 경우 자동 스크롤 재시작
        if (isToolbarCollapsed) {
            onPause() //아래 액티비티 종료시 메서드 사용
        } else {
            onResume() //아래 액티비티 시작시 메서드 사용
        }
    }

    private fun initObserver() {
        sanDetailViewModel.info.observe(this) {
            initView(it)
            Log.d(TAG, "initObserver: $it")
        }
    }

    // 액티비티가 다시 시작될 때 자동스크롤도 다시 시작
    override fun onResume() {
        super.onResume()
        slideImageHandler.postDelayed(slideImageRunnable, 5000)
    }

    // 액티비티가 멈출 때 자동스크롤도 같이 멈춤
    override fun onPause() {
        super.onPause()
        slideImageHandler.removeCallbacks(slideImageRunnable)
    }


    @SuppressLint("SetTextI18n")
    private fun initView(sanlist: SanDetailDTO) {
        // 산 정보 표시
        setSanInfoView(sanlist)
        // 자세히 보기 클릭 시 텍스트 전부 출력
        setMoreView(sanlist)
        // 숫자에 따라 난이도 부여 & 색상 부여
        setDifficultyView(sanlist)
        //상행시간, 하행시간, 총 등산시간
        setHikingTimeView(sanlist)
        // 뷰페이저 어댑터 기본 설정
        initImage(sanlist)
        // 좋아요 기능
        initBookmark(sanlist)
    }

    // 자동 스크롤되는 ViewPager2 이미지
    private fun initImage(sanlist: SanDetailDTO) {
        Log.d("SanDetailImage", "산 이미지 개수 : ${sanlist.img.size}")

        // 뷰페이저 어댑터 기본 설정
        setImageAdapter(sanlist)
        // 자동 스크롤 콜백 설정
        setImageCallBack(sanlist)

    }

    private fun setImageCallBack(sanlist: SanDetailDTO) {
        val imageSize = sanlist.img.size

        binding.vpMountain.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                slideImageHandler.removeCallbacks(slideImageRunnable)
                // 이미지 사진이 1장일 때 자동 스크롤 방지
                if (imageSize > 2) slideImageHandler.postDelayed(slideImageRunnable, 5000)
            }
        })
    }

    private fun setImageAdapter(sanlist: SanDetailDTO) {

        binding.vpMountain.adapter = getImageAdapter(sanlist)
        binding.vpMountain.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }

    private fun getImageAdapter(sanlist: SanDetailDTO) =
        SanImageAdapter(sanlist.img, binding.vpMountain)

    // 인텐트로 넘오는 산 이름 받아줌.
    private fun getSanName() = intent.getStringExtra("name")


    @SuppressLint("SetTextI18n")
    private fun setSanInfoView(sanlist: SanDetailDTO) = with(binding) {
        tvMountain.text = sanlist.mountain
        tvAddress.text = sanlist.address

        val dec = DecimalFormat("#,###")
        val height = sanlist.height
        tvHeightInfo.text = "${dec.format(height)}m"

        //날씨
        val coordinates = Pair(sanlist.lat, sanlist.lng)


        val recyclerViewAdapter = WeatherRecyclerAdapter(this@SanDetailActivity, weatherData)
        binding.rvWeather.adapter = recyclerViewAdapter

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val weatherCurrentData =
                    netWorkRepository.getCurrentList(coordinates.first, coordinates.second)
                val forecastWeatherData =
                    netWorkRepository.getWeekendList(coordinates.first, coordinates.second)

                val currentData = weatherCurrentData

                val icon = currentData.weather.first().icon
                val iconUrl = "http://openweathermap.org/img/w/$icon.png"
                Log.d(TAG, "onBindViewHolder: $iconUrl")


                Glide.with(this@SanDetailActivity)
                    .load(iconUrl)
                    .placeholder(R.drawable.ic_gif_loading)
                    .error(R.drawable.ic_gif_loading)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            Log.e(TAG, "Glide load failed", e)
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }
                    })
                    .into(ivWeather)


                val weekendData = forecastWeatherData.list
                recyclerViewAdapter.updateData(weekendData)

            } catch (e: Exception) {
                Log.e("NetworkError", e.message ?: "Unknown error")
            }
        }


        /* // 데이터 구조 변경 후 사용
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val weatherCurrentList = netWorkRepository.getCurrentList(sanlist.lat, sanlist.lng)
                val iconIndex = weatherCurrentList.weather[0]
                val iconUrl = "http://openweathermap.org/img/w/$iconIndex.png"

                //CoroutineScope로 감싸져 있기 때문에 this의 위치를 SanDetailActivity로 지정해 주어햐 함
                //그냥 this는 CoroutineScope로 인식함
                Glide.with(this@SanDetailActivity)
                    .load(iconUrl)
                    .into(ivWeather)
                */
        /**받아오는 화질 줄이는 방법 사용하기*//*

            } catch (e: Exception) {
                Log.e("NetworkError", e.message ?: "Unknown error")
            }
        }
        */

    }

    private fun setMoreView(sanlist: SanDetailDTO) = with(binding) {
        tvIntroInfo.text = sanlist.summary
        tvRecommendInfo.text = sanlist.recommend

        // 산 개요 옆 화살표 토글
        setOnClickToggle(ivIntroToggle, clIntroAccordian)

        // 추천 이유 옆 화살표 토글
        setOnClickToggle(ivRecommendToggle, clRecommendAccordian)

    }

    private fun setDifficultyView(sanlist: SanDetailDTO) = with(binding) {
        val difficulty = sanlist.difficulty
        tvDifficultyInfo.text = when (difficulty) {
            1L -> "하"
            2L -> "중"
            else -> "상"
        }

        when (tvDifficultyInfo.text) {
            "하" -> tvDifficultyInfo.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.offroader_blue
                )
            )

            "중" -> tvDifficultyInfo.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.offroader_orange
                )
            )

            else -> tvDifficultyInfo.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.offroader_red
                )
            )
        }
    }

    private fun setHikingTimeView(sanlist: SanDetailDTO) = with(binding) {
        val totalTime = sanlist.time

        viewHillTime(totalTime, tvTimeInfo)
    }

    // 아코디언 UI 텍스트 편
    private fun viewMoreText(view: View, isExpanded: Boolean, layoutExpand: ConstraintLayout) {
        ToggleAnimation.toggleArrow(view, isExpanded)
        if (isExpanded) {
            ToggleAnimation.expand(layoutExpand)
        } else {
            ToggleAnimation.collapse(layoutExpand)
        }
    }

    // 토글 클릭 시 아코디언 UI 펼첬다 접었다 하기
    private fun setOnClickToggle(toggle: ImageView, accordian: ConstraintLayout) {
        var isExp = false

        toggle.setOnClickListener {
            viewMoreText(it, !isExp, accordian)

            isExp = if (accordian.visibility == View.VISIBLE) true
            else false
        }
    }

    // 등산 시간을 출력시켜주는 함수
    @SuppressLint("SetTextI18n")
    private fun viewHillTime(time: Long, info: TextView) {
        if (time % 60 == 0L) {
            info.text =
                "${time / 60}시간"
        } else {
            info.text =
                "${time / 60}시간 ${time % 60}분"
        }
    }


    // 좋아요 기능
    private fun initBookmark(sanlist: SanDetailDTO) {
        with(binding) {
            // 초기화
            if (sanDetailViewModel.sanLikedList.value?.contains(sanlist.mountain) == true) {
                sanlist.isLiked = true
                ivBookmark.setImageResource(R.drawable.ic_bookmark_on)
            } else {
                sanlist.isLiked = false
                ivBookmark.setImageResource(R.drawable.ic_bookmark_off)
            }

            ivBookmark.setOnClickListener {
                Log.d(TAG, "좋아요 클릭")

                //ViewModel LiveData로 저장
                if (sanlist.isLiked) {
                    sanDetailViewModel.removeSanLikedList(sanlist.mountain)
                } else {
                    sanDetailViewModel.addSanLikedList(sanlist.mountain)
                }

                sanlist.isLiked = !sanlist.isLiked

                ivBookmark.setImageResource(
                    if (sanlist.isLiked) R.drawable.ic_bookmark_on else R.drawable.ic_bookmark_off
                )

                saveData(
                    LikedConstants.LIKED_PREFS,
                    LikedConstants.LIKED_PREF_KEY,
                    sanDetailViewModel.sanLikedList.value
                )
            }
        }

    }

    // SharedPreference 저장
    private fun <T> saveData(preferKey: String, dataKey: String, data: T) {
        val prefs = getSharedPreferences(preferKey, Context.MODE_PRIVATE)
        val edit = prefs.edit()
        edit.clear()

        val json = Gson().toJson(data)

        edit.putString(dataKey, json).apply()
        Log.d(TAG, "SavedSanList")
    }

    // SharedPreference 불러오기
    private fun loadData() {
        val prefs = getSharedPreferences(LikedConstants.LIKED_PREFS, Context.MODE_PRIVATE)
        if (prefs.contains(LikedConstants.LIKED_PREF_KEY)) {
            val gson = Gson()
            val json = prefs.getString(LikedConstants.LIKED_PREF_KEY, "")
            try {
                val type = object : TypeToken<MutableList<String>>() {}.type
                val sanStore: MutableList<String> = gson.fromJson(json, type)
                sanDetailViewModel.loadSanLikedList(sanStore)

                Log.d(TAG, "저장된 목록 : ${sanStore}")
            } catch (e: JsonParseException) {
                e.printStackTrace()
            }
        }
    }

    // 뒤로가기 버튼
    private fun initBackButton() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }
}