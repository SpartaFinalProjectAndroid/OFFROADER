package com.mit.offroader.ui.activity.sandetail

import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
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

class SanDetailActivity : AppCompatActivity() {
    private var _binding: ActivitySanDetailBinding? = null
    private val binding get() = _binding!!

    // 자동 스크롤
    private val slideImageHandler: Handler = Handler()
    private val slideImageRunnable =
        Runnable { binding.vpMountain.currentItem = binding.vpMountain.currentItem + 1 }

    private lateinit var imageAdapter: SanImageAdapter
    private val sanDetailViewModel by viewModels<SanDetailViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySanDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        initImage()
        initBookMarkButton()

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

        binding.vpMountain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                slideImageHandler.removeCallbacks(slideImageRunnable)
                slideImageHandler.postDelayed(slideImageRunnable, 5000)
            }
        })
    }

    // 좋아요 기능
    private fun initBookMarkButton() {
        val bookmarkOn = resources.getDrawable(R.drawable.ic_bookmark_on)
        val bookmarkOff = resources.getDrawable(R.drawable.ic_bookmark_off)


//        binding.ivBookmark.setOnClickListener {
//            binding.ivBookmark.setImageResource(
//                if(isLiked) bookmarkOn else bookmarkOff
//            )
//        }
    }

    // 뒤로가기 버튼
    private fun initBackButton() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }
}