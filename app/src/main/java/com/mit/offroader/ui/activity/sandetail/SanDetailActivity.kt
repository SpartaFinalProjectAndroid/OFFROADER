package com.mit.offroader.ui.activity.sandetail

import android.os.Bundle
import android.os.Handler
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
import java.text.DecimalFormat

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

        initData()
        viewMoreText(binding.tvIntroInfo, binding.tvIntroPlus)
        viewMoreText(binding.tvRecommendInfo, binding.tvRecommendPlus)
        initImage()
//        initBookMarkButton()

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

    // Firebase 데이터 받아오기
    private fun initData() {

        firestore.collection("sanlist")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    if (document.getString("name") == "한라산") {
                        val sanlist = SanDetailUiState(
                            document.getString("name") ?: "none",
                            document.getString("address") ?: "none",
                            document.getLong("difficulty") ?: 0,
                            document.getDouble("height") ?: 0.0,
                            document.getLong("time_uphill") ?: 0,
                            document.getLong("time_downhill") ?: 0,
                            document.getString("summary") ?: "none",
                            document.getString("recommend") ?: "none"
                        )

                        with(binding) {
                            tvMountain.text = sanlist.mountain
                            tvAddress.text = sanlist.address

                            val dec = DecimalFormat("#,###")
                            val height = sanlist.height
                            tvHeightInfo.text = "${dec.format(height)}m"
                            tvIntroInfo.text = sanlist.summary
                            tvRecommendInfo.text = sanlist.recommend

                            // 숫자에 따라 난이도 부여 & 색상 부여
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

                            //상행시간, 하행시간, 총 등산시간
                            val uphilltime = sanlist.uphilltime
                            val downhilltime = sanlist.downhilltime
                            if (uphilltime % 60 == 0L) {
                                tvUptimeInfo.text =
                                    "${uphilltime / 60}시간"
                            } else {
                                tvUptimeInfo.text =
                                    "${uphilltime / 60}시간 ${uphilltime % 60}분"
                            }
                            if (downhilltime % 60 == 0L) {
                                tvDowntimeInfo.text =
                                    "${downhilltime / 60}시간"
                            } else {
                                tvDowntimeInfo.text =
                                    "${downhilltime / 60}시간 ${downhilltime % 60}분"
                            }
                            if ((uphilltime + downhilltime) % 60 == 0L) {
                                tvTimeInfo.text =
                                    "${(uphilltime + downhilltime) / 60}시간"
                            } else {
                                tvTimeInfo.text =
                                    "${(uphilltime + downhilltime) / 60}시간 ${(uphilltime + downhilltime) % 60}분"
                            }
                        }
                    }
                }

            }
    }

    // 자세히 보기 클릭 시 텍스트 전부 출력
    private fun viewMoreText(contentTextView: TextView, viewMoreTextView: TextView) {
        contentTextView.post {
            val lineCount = contentTextView.layout.lineCount
            if (lineCount > 0) {
                if (contentTextView.layout.getEllipsisCount(lineCount - 1) > 0) {
                    viewMoreTextView.visibility = View.VISIBLE

                    viewMoreTextView.setOnClickListener {
                        contentTextView.maxLines = Int.MAX_VALUE
                        viewMoreTextView.visibility = View.GONE
                    }
                }
            }
        }
    }

    // 자동 스크롤되는 ViewPager2 이미지
    private fun initImage() {
        val mountain = binding.tvMountain.text
        imageAdapter = when (mountain) {
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

        binding.vpMountain.adapter = imageAdapter
        binding.vpMountain.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.vpMountain.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                slideImageHandler.removeCallbacks(slideImageRunnable)
                slideImageHandler.postDelayed(slideImageRunnable, 5000)
            }
        })
    }


//     좋아요 기능
//    private fun initBookMarkButton() {
//
//        if (sanDetailUiState.isLiked) binding.ivBookmark.setImageResource(R.drawable.ic_bookmark_on)
//
//        binding.ivBookmark.setOnClickListener {
//            sanDetailUiState.isLiked = !sanDetailUiState.isLiked
//            binding.ivBookmark.setImageResource(
//                if (sanDetailUiState.isLiked) R.drawable.ic_bookmark_on else R.drawable.ic_bookmark_off
//            )
//            OnBookmarkClickListener.onBookmarkClick(sanDetailUiState)
//        }
//    }

    // 뒤로가기 버튼
    private fun initBackButton() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }
}