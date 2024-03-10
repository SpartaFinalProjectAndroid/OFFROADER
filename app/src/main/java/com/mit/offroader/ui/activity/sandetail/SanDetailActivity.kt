package com.mit.offroader.ui.activity.sandetail

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.firestore.FirebaseFirestore
import com.mit.offroader.R
import com.mit.offroader.databinding.ActivitySanDetailBinding
import com.mit.offroader.ui.activity.sandetail.SanDetailImageData.Companion.hanrasanList
import com.mit.offroader.ui.activity.sandetail.SanDetailImageData.Companion.jirisanList
import com.mit.offroader.ui.activity.sandetail.SanDetailImageData.Companion.kyeryongsanList
import com.mit.offroader.ui.activity.sandetail.SanDetailImageData.Companion.naejangsanList
import com.mit.offroader.ui.activity.sandetail.SanDetailImageData.Companion.northhansanList
import com.mit.offroader.ui.activity.sandetail.SanDetailImageData.Companion.odaesanList
import com.mit.offroader.ui.activity.sandetail.SanDetailImageData.Companion.seullacksanList
import com.mit.offroader.ui.activity.sandetail.SanDetailImageData.Companion.sobaeksanList
import com.mit.offroader.ui.activity.sandetail.SanDetailImageData.Companion.sokrisanList
import com.mit.offroader.ui.activity.sandetail.viewmodel.SanDetailViewModel
import java.text.DecimalFormat

private const val TAG = "SanDetailActivity"

class SanDetailActivity : AppCompatActivity() {
    private var _binding: ActivitySanDetailBinding? = null
    private val binding get() = _binding!!

    // 자동 스크롤
    private val slideImageHandler: Handler = Handler()
    private val slideImageRunnable =
        Runnable { binding.vpMountain.currentItem = binding.vpMountain.currentItem + 1 }

    // 데이터 firebase로 받아오기
    private val firestore = FirebaseFirestore.getInstance()

    private lateinit var imageAdapter: SanImageAdapter
    private val sanDetailViewModel by viewModels<SanDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySanDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.transparent)
        //전체화면으로 설정하면 상단 parent 아이콘 배치 margin 주어야 함 안그러면 상태바 아래로 기어드감
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        //보고 필요하면 상태바 아이콘 어둡게
        //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

//        binding.tbMustBeTrans.setBackgroundColor(Color.TRANSPARENT)
//        binding.appbar.setBackgroundColor(Color.TRANSPARENT)
//        binding.collapsingToolbar.setBackgroundColor(Color.TRANSPARENT)
//        binding.collapsingToolbar.setContentScrimColor(ContextCompat.getColor(this, android.R.color.transparent))
//        tb.alpha = 0f

        val sanName = intent.getStringExtra("name") ?: ""
        Log.d(TAG, "산이름 : ${sanName}")

        initImage()

        initBackButton()
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
    private fun initView(sanlist: SanDetailUiState) {
        // 산 정보 표시
        setSanInfoView(sanlist)
        // 자세히 보기 클릭 시 텍스트 전부 출력
        setMoreView(sanlist)
        // 숫자에 따라 난이도 부여 & 색상 부여
        setDifficultyView(sanlist)
        //상행시간, 하행시간, 총 등산시간
        setHikingTimeView(sanlist)
    }
    // 자동 스크롤되는 ViewPager2 이미지
    private fun initImage() {

        // 뷰페이저 어댑터 기본 설정
        setImageAdapter()
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

    private fun setImageAdapter() {

        // getSanName() : 인텐트로 넘오는 산 이름 받아줌.
        // getImageAdapter : 산 이름에 따라서 산 리스트를 받아옴
        imageAdapter = getImageAdapter(getSanName())

        binding.vpMountain.adapter = imageAdapter
        binding.vpMountain.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }

    private fun getImageAdapter(sanName: String?) = when (sanName) {
        "계룡산" -> SanImageAdapter(kyeryongsanList, binding.vpMountain)
        "내장산" -> SanImageAdapter(naejangsanList, binding.vpMountain)
        "북한산" -> SanImageAdapter(northhansanList, binding.vpMountain)
        "설악산" -> SanImageAdapter(seullacksanList, binding.vpMountain)
        "소백산" -> SanImageAdapter(sobaeksanList, binding.vpMountain)
        "속리산" -> SanImageAdapter(sokrisanList, binding.vpMountain)
        "오대산" -> SanImageAdapter(odaesanList, binding.vpMountain)
        "지리산" -> SanImageAdapter(jirisanList, binding.vpMountain)
        else -> SanImageAdapter(hanrasanList, binding.vpMountain)
    }
    // 인텐트로 넘오는 산 이름 받아줌.
    private fun getSanName() =intent.getStringExtra("name")


    private fun setSanInfoView(sanlist: SanDetailUiState) = with(binding){
        tvMountain.text = sanlist.mountain
        tvAddress.text = sanlist.address

        val dec = DecimalFormat("#,###")
        val height = sanlist.height
        tvHeightInfo.text = "${dec.format(height)}m"
    }

    private fun setMoreView(sanlist: SanDetailUiState) = with(binding){
        tvIntroInfo.text = sanlist.summary
        tvRecommendInfo.text = sanlist.recommend
        viewMoreText(tvIntroInfo, tvIntroPlus, tvIntroShort)
        viewMoreText(tvRecommendInfo, tvRecommendPlus, tvRecommendShort)
    }

    private fun setDifficultyView(sanlist: SanDetailUiState) = with(binding){
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

    private fun setHikingTimeView(sanlist: SanDetailUiState) = with(binding) {
        val uphillTime = sanlist.uphilltime
        val downhillTime = sanlist.downhilltime
        val totalTime = uphillTime + downhillTime

        viewHillTime(uphillTime, tvUptimeInfo)
        viewHillTime(downhillTime, tvDowntimeInfo)
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