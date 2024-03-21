package com.ing.offroader.ui.fragment.community

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.ing.offroader.databinding.FragmentCommunityBinding
import com.ing.offroader.ui.activity.add_post.AddPostActivity
import com.ing.offroader.ui.fragment.community.viewmodel.CommunityViewModel

private const val TAG = "ChatBotFragment"

class CommunityFragment : Fragment() {


    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!
    private val communityViewModel: CommunityViewModel by viewModels()
//    }
//    private val chatAdapter: CommunityAdapter by lazy {
//        CommunityAdapter(chatBotViewModel)

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
//        binding.rvChatbot.adapter = chatAdapter
        initView()
        initViewModel()
        initObserver()

    }

    private fun initViewModel() {
        communityViewModel.setPosts()
    }

    private fun initObserver() {

    }

    private fun initView() {
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