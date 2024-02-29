package com.mit.offroader.ui.activity.sandetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.mit.offroader.R
import com.mit.offroader.databinding.ActivityMainBinding
import com.mit.offroader.databinding.ActivitySanDetailBinding
import com.mit.offroader.ui.activity.main.MainViewModel

class SanDetailActivity : AppCompatActivity() {
    private var _binding: ActivitySanDetailBinding? = null
    private val binding get() = _binding!!
    private val sanDetailViewModel by viewModels<SanDetailViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySanDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initImage()
        initBookMarkButton()


        initBackButton()
    }

    private fun initImage() {
        imageAdapter = SanImageAdapter(mItems = List<SanDetailImageData>)
    }

    private fun initBookMarkButton() {
        val bookmarkOn = resources.getDrawable(R.drawable.ic_bookmark_on)
        val bookmarkOff = resources.getDrawable(R.drawable.ic_bookmark_off)



        binding.ivBookmark.setOnClickListener {
            binding.ivBookmark.setImageResource(
                if(isLiked) bookmarkOn else bookmarkOff
            )
        }
    }

    private fun initBackButton() {
        binding.ivBack.setOnClickListener {
            finish()
        }


    }
}