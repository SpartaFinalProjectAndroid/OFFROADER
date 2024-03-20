package com.ing.offroader.ui.activity.add_post

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.ing.offroader.databinding.ActivityAddPostBinding

private const val TAG = "AddPostActivity"
class AddPostActivity : AppCompatActivity() {
    private var _binding: ActivityAddPostBinding? = null
    private val binding get() = _binding!!

    private val addPostViewModel by viewModels<AddPostViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initObserver()

    }

    private fun initObserver() {
        addPostViewModel.addPostUiState.observe(this) {
            if (!it.errorMessage.isNullOrBlank()) {
                Toast.makeText(this,it.errorMessage,Toast.LENGTH_SHORT).show()
            }
            if (!it.cycle) {
                finish()
            }
        }

    }

    private fun initView() {
        setupListener()
    }

    private fun setupListener() = with(binding){
        etTitle.addTextChangedListener {
            addPostViewModel.titleChangedListener(etTitle.text.toString())
        }
        etContent.addTextChangedListener {
            addPostViewModel.contentChangedListener(etContent.text.toString())
        }
        tvComplete.setOnClickListener {
            addPostViewModel.setOnCompleteButton()
        }
        // TODO : 1. 갤러리에서 이미지 가져오는 코드 짜기
        // TODO : 2. 카메라로 이동해서 사진 찍는 코드
    }
}