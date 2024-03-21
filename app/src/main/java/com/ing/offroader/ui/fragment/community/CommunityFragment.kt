package com.ing.offroader.ui.fragment.community

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.ing.offroader.databinding.FragmentCommunityBinding
import com.ing.offroader.ui.activity.add_post.AddPostActivity
import com.ing.offroader.ui.fragment.community.adapter.CommunityAdapter
import com.ing.offroader.ui.fragment.community.model.PostDTO
import com.ing.offroader.ui.fragment.community.viewmodel.CommunityViewModel

private const val TAG = "태그 : ChatBotFragment"

class CommunityFragment : Fragment() {

    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!
    private val communityViewModel: CommunityViewModel by viewModels()

    private val communityAdapter: CommunityAdapter by lazy {
        CommunityAdapter(communityViewModel)
    }

    private val user = FirebaseAuth.getInstance().currentUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserver()

    }

    private fun initObserver() {
        Log.d(TAG, "initObserver: 옵져빙 시작!")
        communityViewModel.communityUiState.observe(viewLifecycleOwner) {
            Log.d(TAG, "initObserver: 커뮤니티유아이스테이트 옵져빙 됨.")
            if (it != null) {
                Log.d(TAG, "initObserver: postItem 업데이트 ${it.postItems}")
                setItemView(it.postItems)
            }

        }
    }

    private fun setItemView(postItems: ArrayList<PostDTO?>?){
        Log.d(TAG, "setItemView: 셋 아이템 뷰 여기서 서브밋 함.")
        communityAdapter.submitList(postItems)
    }

    private fun initView() {
        // 어댑터를 가장 먼저 바인딩 해줘야만 서브밋이 가능함
        binding.rvCommunity.adapter = communityAdapter
        // 리포지토리에서 우선 커뮤니티 게시글을 가져와야함.
//        communityViewModel.setPosts()

        setAddPostButton()

    }

    private fun setAddPostButton() {
        binding.ivAddPost.setOnClickListener {
            if (user == null) {
                Toast.makeText(requireActivity(),"회원가입을 해야만 포스팅이 가능합니다.",Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(requireActivity(), AddPostActivity::class.java)
                startActivity(intent)
            }

        }
    }

// 뷰 모델 옵져빙해주는 함수

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}