package com.ing.offroader.ui.activity.add_post

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ing.offroader.databinding.ActivityAddPostBinding

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