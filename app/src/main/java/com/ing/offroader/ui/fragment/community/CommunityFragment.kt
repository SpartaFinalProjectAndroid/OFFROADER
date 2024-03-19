package com.ing.offroader.ui.fragment.community

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ing.offroader.databinding.FragmentCommunityBinding
import com.ing.offroader.ui.activity.add_post.AddPostActivity
import com.ing.offroader.ui.activity.main.MainActivity

private const val TAG = "ChatBotFragment"

class CommunityFragment : Fragment() {


    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!
//    private val chatBotViewModel: CommunityViewModel by viewModels()
//    }
//    private val chatAdapter: CommunityAdapter by lazy {
////        CommunityAdapter(chatBotViewModel)


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
        initObserver()

    }

    private fun initObserver() {

    }

    private fun initView() {
        setAddPostButton()
    }

    private fun setAddPostButton() {
        binding.ivAddPost.setOnClickListener {
            val intent = Intent(requireActivity(), AddPostActivity::class.java)
            startActivity(intent)
        }
    }

// 뷰 모델 옵져빙해주는 함수

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}