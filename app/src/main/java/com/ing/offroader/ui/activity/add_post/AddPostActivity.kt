package com.ing.offroader.ui.activity.add_post

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.ing.offroader.databinding.ActivityAddPostBinding
import com.ing.offroader.ui.fragment.mydetail.MyDetailViewModel

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

    }

    private fun initView() {
        setupListener()
    }

    private fun setupListener() = with(binding){
        etTitle.addTextChangedListener {

        }
        etContent.addTextChangedListener {

        }
        tvComplete.setOnClickListener {  }
    }
}