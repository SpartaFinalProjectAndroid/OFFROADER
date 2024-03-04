package com.mit.offroader.ui.activity.sandetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.mit.offroader.R
import com.mit.offroader.databinding.ActivityMainBinding
import com.mit.offroader.databinding.ActivitySanDetailBinding
import com.mit.offroader.ui.activity.main.MainViewModel
import com.mit.offroader.ui.activity.sandetail.SanDetailImageData.Companion.imageList

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

        initImage()
        initBookMarkButton()

        initBackButton()
    }

    override fun onResume() {
        super.onResume()
        slideImageHandler.postDelayed(slideImageRunnable, 5000)
    }

    override fun onPause() {
        super.onPause()
        slideImageHandler.removeCallbacks(slideImageRunnable)
    }

    // 자동 스크롤되는 ViewPager2 이미지
    private fun initImage() {
        imageAdapter = SanImageAdapter(imageList)
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