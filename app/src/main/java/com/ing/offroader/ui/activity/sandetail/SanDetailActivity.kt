package com.ing.offroader.ui.activity.sandetail

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.ing.offroader.R
import com.ing.offroader.data.liked.OnBookmarkClickListener
import com.ing.offroader.data.model.weather.WeekendWeatherData
import com.ing.offroader.data.repository.WeatherRepository
import com.ing.offroader.databinding.ActivitySanDetailBinding
import com.ing.offroader.ui.activity.sandetail.viewmodel.SanDetailViewModel
import com.ing.offroader.ui.activity.sandetail.viewmodel.SanDetailViewModelFactory
import com.ing.offroader.ui.fragment.chatbot.MyApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat

private const val TAG = "SanDetailActivity"

class SanDetailActivity : AppCompatActivity() {
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

        window.statusBarColor = ContextCompat.getColor(this, R.color.transparent)
        //전체화면으로 설정하면 상단 parent 아이콘 배치 margin 주어야 함 안그러면 상태바 아래로 기어드감
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        //보고 필요하면 상태바 아이콘 어둡게
        //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        sanDetailViewModel.getSelectedSan(getSanName())
//        Log.d(TAG, "산이름 : ${sanName}")


        val coordinates = Pair(37.5665, 126.9780)

        val recyclerViewAdapter = WeatherRecyclerAdapter(this, weatherData)
        binding.rvWeather.adapter = recyclerViewAdapter

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val weatherCurrentList =
                    netWorkRepository.getCurrentList(coordinates.first, coordinates.second)
                val forecastWeatherData =
                    netWorkRepository.getWeekendList(coordinates.first, coordinates.second)

                val weekendData = forecastWeatherData.list
                recyclerViewAdapter.updateData(weekendData)

            } catch (e: Exception) {
                Log.e("NetworkError", e.message ?: "Unknown error")
            }
        }

        initBackButton()
        initObserver()
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
        var isExp: Boolean = false

        ivIntroToggle.setOnClickListener {
            viewMoreText(it, !isExp, clIntroAccordian)

            // 확대 & 축소 전환
            if(clIntroAccordian.visibility == View.VISIBLE) isExp = true
            else isExp = false
        }

        ivRecommendToggle.setOnClickListener {
            viewMoreText(it, !isExp, clRecommendAccordian)

            // 확대 & 축소 전환
            if(clRecommendAccordian.visibility == View.VISIBLE) isExp = true
            else isExp = false
        }
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
        val totalTime = sanlist.uphillTime

        viewHillTime(totalTime, tvTimeInfo)
    }

    // 아코디언 UI
    private fun viewMoreText(view: View, isExpanded: Boolean, layoutExpand: ConstraintLayout) {
        ToggleAnimation.toggleArrow(view, isExpanded)
        if(isExpanded) {
            ToggleAnimation.expand(layoutExpand)
        } else {
            ToggleAnimation.collapse(layoutExpand)
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
            if (sanlist.isLiked) ivBookmark.setImageResource(R.drawable.ic_bookmark_on)

            ivBookmark.setOnClickListener {
                sanlist.isLiked = !sanlist.isLiked
                ivBookmark.setImageResource(
                    if (sanlist.isLiked) R.drawable.ic_bookmark_on else R.drawable.ic_bookmark_off
                )
                Log.d(TAG, "좋아요 클릭")
                OnBookmarkClickListener.onBookmarkClick(sanlist)
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