package com.mit.offroader.ui.activity.intro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.mit.offroader.databinding.ActivityIntroSplashBinding

class IntroSplashActivity : AppCompatActivity() {
    private var _binding: ActivityIntroSplashBinding? = null
    private val binding get() = _binding!!
    private val introSplashViewModel by viewModels<IntroSplashViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityIntroSplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}