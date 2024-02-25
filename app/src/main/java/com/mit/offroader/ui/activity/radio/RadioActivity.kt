package com.mit.offroader.ui.activity.radio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.mit.offroader.R
import com.mit.offroader.databinding.ActivityMainBinding
import com.mit.offroader.databinding.ActivityRadioBinding
import com.mit.offroader.ui.activity.main.MainViewModel

class RadioActivity : AppCompatActivity() {

    private var _binding: ActivityRadioBinding? = null
    private val binding get() = _binding!!
    private val radioViewModel by viewModels<RadioViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRadioBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}