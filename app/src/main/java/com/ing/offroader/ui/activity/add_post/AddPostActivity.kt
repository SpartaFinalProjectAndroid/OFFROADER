package com.ing.offroader.ui.activity.add_post

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ing.offroader.R
import com.ing.offroader.databinding.ActivityAddPostBinding
import com.ing.offroader.databinding.ActivityChatbotBinding
private const val TAG = "AddPostActivity"
class AddPostActivity : AppCompatActivity() {


    private var _binding: ActivityAddPostBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initObserver()

    }

    private fun initObserver() {

    }

    private fun initView() {
    }
}