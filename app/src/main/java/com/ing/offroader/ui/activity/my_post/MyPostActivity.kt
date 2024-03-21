package com.ing.offroader.ui.activity.my_post

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.ing.offroader.databinding.ActivityMyPostBinding
import com.ing.offroader.ui.fragment.community.MyApplication
import com.ing.offroader.ui.fragment.community.adapter.CommunityAdapter
import com.ing.offroader.ui.fragment.community.viewmodel.CommunityViewModel
import com.ing.offroader.ui.fragment.community.viewmodel.CommunityViewModelFactory

class MyPostActivity : AppCompatActivity() {

    private var _binding: ActivityMyPostBinding? = null
    private val binding get() = _binding!!

    private val communityViewModel: CommunityViewModel by viewModels {
        CommunityViewModelFactory((this.application as MyApplication).postRepository)
    }

    private val communityAdapter: CommunityAdapter by lazy {
        CommunityAdapter(communityViewModel)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMyPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()

    }

    private fun initView() {

    }
}
