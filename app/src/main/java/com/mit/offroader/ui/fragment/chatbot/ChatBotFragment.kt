package com.mit.offroader.ui.fragment.chatbot

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.mit.offroader.R
import com.mit.offroader.databinding.FragmentChatBotBinding
import com.mit.offroader.ui.activity.main.MainActivity
import com.mit.offroader.ui.fragment.mydetail.MyDetailViewModel

class ChatBotFragment : Fragment() {

    companion object {
        fun newInstance() = ChatBotFragment()
    }

    private var _binding: FragmentChatBotBinding? = null
    private val binding get() = _binding!!
    private val chatBotViewModel by viewModels<ChatBotViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatBotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
        initObserver()
    }

    private fun initObserver() {

    }


    private fun initViewModel() {

    }

    /**
    프래그먼트를 최초 실행했을 때 실행 되는 함수를 모아놓은 함수
     **/
    private fun initView() {
        setBotServiceProvider()            // 채팅 봇 "스피너"를 셋팅해주는 함수
        setBot()                           // 스피너를 작동 (하이키/봉봉이) 을 셋팅해주는 함수
    }

    // 채팅 봇 "스피너"를 셋팅해주는 함수
    private fun setBotServiceProvider() {
        binding.spBot.adapter = ArrayAdapter(
            activity as MainActivity,
            android.R.layout.simple_spinner_dropdown_item,
            listOf(
                getString(R.string.chatbot_hikey),
                getString(R.string.chatbot_bongbong)
            )
        )
    }

    // 스피너를 작동 (하이키/봉봉이) 을 셋팅해주는 함수
    private fun setBot() {
        binding.spBot.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                chatBotViewModel.setBotSpinner()
                when (position) {
                    0 -> {
                        binding.ivBot.setImageResource(R.drawable.ic_hikey)
                        // TODO : 채팅 화면 바꾸기
                    }
                    1 -> {
                        binding.ivBot.setImageResource(R.drawable.ic_bongbong)
                        // TODO : 채팅 화면 바꾸기
                    }
                }
                // 로직 부분 구현하기

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.ivBot.setImageResource(R.drawable.ic_bongbong)
                // TODO : 채팅 화면 바꾸기
            }

        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}