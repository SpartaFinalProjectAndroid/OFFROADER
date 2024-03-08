package com.mit.offroader.ui.activity.sandetail

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
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

        initData()
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

    // Firebase 데이터 받아오기
    private fun initData() {
        val sanName = intent.getStringExtra("name")

        firestore.collection("sanlist")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    if (document["name"] == sanName) {
                        val sanlist = SanDetailUiState(
                            document.getString("name") ?: "none",
                            document.getString("address") ?: "none",
                            document.getLong("difficulty") ?: 0,
                            document.getDouble("height") ?: 0.0,
                            document.getLong("time_uphill") ?: 0,
                            document.getLong("time_downhill") ?: 0,
                            document.getString("summary") ?: "none",
                            document.getString("recommend") ?: "none",
                            document.getBoolean("isLiked") ?: false
                        )

                        //데이터를 view에 전달
                        initView(sanlist)
                    }
                }

            }
    }

    @SuppressLint("SetTextI18n")
    private fun initView(sanlist: SanDetailUiState) {
        with(binding) {
            tvMountain.text = sanlist.mountain
            tvAddress.text = sanlist.address

            val dec = DecimalFormat("#,###")
            val height = sanlist.height
            tvHeightInfo.text = "${dec.format(height)}m"
            tvIntroInfo.text = sanlist.summary
            tvRecommendInfo.text = sanlist.recommend

            // 자세히 보기 클릭 시 텍스트 전부 출력
            tvIntroInfo.post {
                val lineCount = tvIntroInfo.layout.lineCount
                if (lineCount > 0) {
                    if (tvIntroInfo.layout.getEllipsisCount(lineCount - 1) > 0) {
                        tvIntroPlus.visibility = View.VISIBLE

                        tvIntroPlus.setOnClickListener {
                            tvIntroInfo.maxLines = Int.MAX_VALUE
                            tvIntroPlus.visibility = View.GONE
                            tvIntroShort.visibility = View.VISIBLE
                        }

                        tvIntroShort.setOnClickListener {
                            tvIntroInfo.maxLines = 5
                            tvIntroPlus.visibility = View.VISIBLE
                            tvIntroShort.visibility = View.GONE
                        }
                    }
                }
            }

            tvRecommendInfo.post {
                val lineCount = tvRecommendInfo.layout.lineCount
                if (lineCount > 0) {
                    if (tvRecommendInfo.layout.getEllipsisCount(lineCount - 1) > 0) {
                        tvRecommendPlus.visibility = View.VISIBLE

                        tvRecommendPlus.setOnClickListener {
                            tvRecommendInfo.maxLines = Int.MAX_VALUE
                            tvRecommendPlus.visibility = View.GONE
                            tvRecommendShort.visibility = View.VISIBLE
                        }

                        tvRecommendShort.setOnClickListener {
                            tvRecommendInfo.maxLines = 5
                            tvRecommendPlus.visibility = View.VISIBLE
                            tvRecommendShort.visibility = View.GONE
                        }
                    }
                }
            }

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



    // 자동 스크롤되는 ViewPager2 이미지
    private fun initImage() {
        val sanName = intent.getStringExtra("name")
        imageAdapter = when (sanName) {
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