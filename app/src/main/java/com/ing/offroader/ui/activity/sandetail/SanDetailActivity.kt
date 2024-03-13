package com.ing.offroader.ui.activity.sandetail

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.ing.offroader.R
import com.ing.offroader.databinding.ActivitySanDetailBinding
import com.ing.offroader.ui.activity.sandetail.viewmodel.SanDetailViewModel
import com.ing.offroader.ui.activity.sandetail.viewmodel.SanDetailViewModelFactory
import com.ing.offroader.ui.fragment.chatbot.MyApplication
import java.text.DecimalFormat

private const val TAG = "SanDetailActivity"

class SanDetailActivity : AppCompatActivity() {
    private var _binding: ActivitySanDetailBinding? = null
    private val binding get() = _binding!!

    // 자동 스크롤
    private val slideImageHandler: Handler = Handler()
    private val slideImageRunnable =
        Runnable { binding.vpMountain.currentItem = binding.vpMountain.currentItem + 1 }

    private lateinit var imageAdapter: SanImageAdapter
    private val sanDetailViewModel: SanDetailViewModel by viewModels { SanDetailViewModelFactory((application as MyApplication).sanDetailRepository) }

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


        initBackButton()
        initObserver()
    }

    private fun initObserver() {

        sanDetailViewModel.info.observe(this) {
            initView(it)
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
    }
    // 자동 스크롤되는 ViewPager2 이미지
    private fun initImage(sanlist : SanDetailDTO) {

        // 뷰페이저 어댑터 기본 설정
        setImageAdapter(sanlist)
        //자동 스크롤 콜백 설정
        setImageCallBack()

    }

    private fun setImageCallBack() {
        binding.vpMountain.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                slideImageHandler.removeCallbacks(slideImageRunnable)
                slideImageHandler.postDelayed(slideImageRunnable, 5000)
            }
        })
    }

    private fun setImageAdapter(sanlist: SanDetailDTO) {

        binding.vpMountain.adapter = getImageAdapter(sanlist)
        binding.vpMountain.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }

    private fun getImageAdapter(sanlist: SanDetailDTO) = SanImageAdapter(sanlist.img, binding.vpMountain)
    // 인텐트로 넘오는 산 이름 받아줌.
    private fun getSanName() = intent.getStringExtra("name")


    private fun setSanInfoView(sanlist: SanDetailDTO) = with(binding){
        tvMountain.text = sanlist.mountain
        tvAddress.text = sanlist.address

        val dec = DecimalFormat("#,###")
        val height = sanlist.height
        tvHeightInfo.text = "${dec.format(height)}m"
    }

    private fun setMoreView(sanlist: SanDetailDTO) = with(binding){
        tvIntroInfo.text = sanlist.summary
        tvRecommendInfo.text = sanlist.recommend
        viewMoreText(tvIntroInfo, tvIntroPlus, tvIntroShort)
        viewMoreText(tvRecommendInfo, tvRecommendPlus, tvRecommendShort)
    }

    private fun setDifficultyView(sanlist: SanDetailDTO) = with(binding){
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
        val uphillTime = sanlist.uphillTime
        val downhillTime = sanlist.downhillTime
        val totalTime = uphillTime + downhillTime

//        viewHillTime(uphillTime, tvUptimeInfo)
//        viewHillTime(downhillTime, tvDowntimeInfo)
        viewHillTime(totalTime, tvTimeInfo)    }

    // 자세히 보기 클릭 시 텍스트 전부 출력하는 함수
    private fun viewMoreText(info: TextView, plus: TextView, short: TextView) {
        info.post {
            val lineCount = info.layout.lineCount
            if (lineCount > 0) {
                if (info.layout.getEllipsisCount(lineCount - 1) > 0) {
                    plus.visibility = View.VISIBLE

                    plus.setOnClickListener {
                        info.maxLines = Int.MAX_VALUE
                        plus.visibility = View.GONE
                        short.visibility = View.VISIBLE
                    }

                    short.setOnClickListener {
                        info.maxLines = 5
                        plus.visibility = View.VISIBLE
                        short.visibility = View.GONE
                    }
                }
            }
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
    private fun initBookmark() {
//        if (sanlist.isLiked) ivBookmark.setImageResource(R.drawable.ic_bookmark_on2)
//
//        ivBookmark.setOnClickListener {
//            sanlist.isLiked = !sanlist.isLiked
//            binding.ivBookmark.setImageResource(
//                if (sanlist.isLiked) R.drawable.ic_bookmark_on2 else R.drawable.ic_bookmark_off2
//            )
//            OnBookmarkClickListener.onBookmarkClick(sanlist)
//        }
    }

    // 뒤로가기 버튼
    private fun initBackButton() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }
}