package com.ing.offroader.ui.activity.intro

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ing.offroader.databinding.ActivityIntroSplashBinding
import com.ing.offroader.ui.activity.main.MainActivity

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
        binding.laIntroSplash.apply {
            setMinAndMaxProgress(0.0f,1f)
            playAnimation()
            addAnimatorListener(object : Animator.AnimatorListener{
                override fun onAnimationStart(p0: Animator) {
                }

                override fun onAnimationEnd(p0: Animator) {
                    val intent = Intent(this@IntroSplashActivity,MainActivity::class.java)
                    startActivity(intent)
                    finish()                }

                override fun onAnimationCancel(p0: Animator) {
                }

                override fun onAnimationRepeat(p0: Animator) {
                }

            })
        }

    }

//    companion object {
//        private const val DURATION : Long = 3000
//    }
//
//    override fun onBackPressed() {
//        super.onBackPressed()
//    }
}