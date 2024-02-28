package com.mit.offroader.ui.fragment.chatbot

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
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
        chatBotViewModel.chatBotUiState.observe(viewLifecycleOwner) {
            Log.d("CHATGPT CHATBOT", "^^ ${it.response}")
        }

    }


    private fun initViewModel() {

    }

    /**
    프래그먼트를 최초 실행했을 때 실행 되는 함수를 모아놓은 함수
     **/
    private fun initView() {

        // 채팅 봇 "스피너"를 셋팅해주는 함수
        setBotServiceProvider()

        // 스피너를 작동 (하이키/봉봉이) 을 셋팅해주는 함수
        setBot()
        // 봇을 셋팅하면 봇에 따라서 챗을 셋팅해 줄 수 있도록 setChat() 함수는 setBot()함수 안에 추가해둠.

        // EditText 검색 셋팅 함수
        setSearch()
    }

    // 에딧 텍스트에서 챗봇 검색 실행 코드.
    private fun setSearch() {
        binding.etAsk.setOnEditorActionListener { textView, actionId, keyEvent ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                chatBotViewModel.onSearch(textView.toString())
                handled = true
            }
            handled
        }
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

            // 스피너가 선택 되었을 때 : position 값이 선택된 스피너 값의 인덱스를 알려줌)
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                chatBotViewModel.setBotSpinner(position)
//                when (position) {
//                    0 -> {
//                        binding.ivBot.setImageResource(R.drawable.ic_hikey)
//                        // TODO : 채팅 화면 전환하기 (+ MVVM 모델 구조로 바꾸어야함)
//                        setChat(position)
//                    }
//                    1 -> {
//                        binding.ivBot.setImageResource(R.drawable.ic_bongbong)
//                        // TODO : 채팅 화면 전환하기 (+ MVVM 모델 구조로 바꾸어야함)
//                        setChat(position)
//                    }
//                }
//                // 로직 부분 구현하기

            }

            // 스피너가 선택되지 않았을 때 (초기 상태) : 초기에 봉봉이를 선택해 둠.
            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.ivBot.setImageResource(R.drawable.ic_bongbong)
                // TODO : 채팅 화면 셋팅 ? sharedpref 또는 Room DB에 저장되어 있는 채팅 내역 가져오기
                setChat(1)
            }

        }

    }

    // 채팅 창을 셋팅해주는 함수
    private fun setChat(bot: Int) { // 정수형 매개변수 bot 이 0이면 봉봉이 1이면 하이키
        when (bot) {
            0 -> {

            }

            1 -> {

            }
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}