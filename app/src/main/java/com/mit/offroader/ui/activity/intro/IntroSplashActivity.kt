package com.mit.offroader.ui.activity.intro

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mit.offroader.databinding.ActivityIntroSplashBinding
import com.mit.offroader.ui.activity.main.MainActivity

class IntroSplashActivity : AppCompatActivity() {
    private var _binding: ActivityIntroSplashBinding? = null
    private val binding get() = _binding!!
    private val introSplashViewModel by viewModels<IntroSplashViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityIntroSplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        setAnimation()
//        Handler().postDelayed({
//            val intent = Intent(this, MainActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
//            startActivity(intent)
//            finish()
//        }, DURATION)
    }

    private fun setAnimation() {


    }

    companion object {
        private const val DURATION : Long = 3000
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}