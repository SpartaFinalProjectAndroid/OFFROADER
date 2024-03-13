package com.ing.offroader.ui.fragment.chatbot

import android.os.Bundle
import com.ing.offroader.data.model.ai.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ing.offroader.R
import com.ing.offroader.databinding.FragmentChatBotBinding
import com.ing.offroader.ui.activity.main.MainActivity
import com.ing.offroader.ui.fragment.chatbot.adapter.ChatAdapter
import com.ing.offroader.ui.fragment.chatbot.viewmodel.ChatBotViewModel
import com.ing.offroader.ui.fragment.chatbot.viewmodel.ChatBotViewModelFactory

private const val TAG = "ChatBotFragment"

class ChatBotFragment : Fragment() {


    private var _binding: FragmentChatBotBinding? = null
    private val binding get() = _binding!!
    private val chatBotViewModel: ChatBotViewModel by viewModels {
        ChatBotViewModelFactory(
            (requireActivity().application as MyApplication).hikeyRepository,
            (requireActivity().application as MyApplication).bongbongRepository
        )
    }
    private val chatAdapter: ChatAdapter by lazy {
        ChatAdapter(chatBotViewModel)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvChatbot.adapter = chatAdapter
        initView()
        initObserver()

    }

    // 뷰 모델 옵져빙해주는 함수
    private fun initObserver() {


        // conversationUiState가 옵져빙되면 스피너에서 봉봉이랑 하이키의 값이 바뀌었을 때 채팅 화면을 바꾸어줌.
        chatBotViewModel.conversationUiState.observe(viewLifecycleOwner) {
            Log.d(TAG, it?.chat.toString())
            if (it == null) {
                chatAdapter.submitList(listOf())
                chooseAiChatBot(listOf(), "hikey")
            } else {
                chatAdapter.submitList(it.chat)
                chooseAiChatBot(it.chat, it.position)
            }
        }


        // hikeyUiState는 hikey 대화내역이 DB에 저장되면 옵져브됨
        // 옵져빙되면 chatAdapter에 대화내역 넣어서 화면에 보여줌.
        chatBotViewModel.hikeyUiState.observe(viewLifecycleOwner) {
            if (it == null) {
                chatAdapter.submitList(listOf())

            } else {
                chatAdapter.submitList(it.chat)
            }

        }

        // bongbongUiState는 bongbong 대화내역이 DB에 저장되면 옵져브됨
        // 옵져빙되면 chatAdapter에 대화내역 넣어서 화면에 보여줌.
        chatBotViewModel.bongbongUiState.observe(viewLifecycleOwner) {
            if (it == null) {
                chatAdapter.submitList(listOf())

            } else {
                chatAdapter.submitList(it.chat)
            }

        }


    }

    // 스피너로 선택된 AI에 맞기 UI 수정하는 함수
    private fun chooseAiChatBot(message: List<Message>?, position: String) {
        chatAdapter.submitList(message)

        // 스크롤 유지
        if (message != null) {
            if (message.isNotEmpty()) {
                binding.rvChatbot.smoothScrollToPosition(message.lastIndex)
            }
        }

        when (position) {
            "hikey" -> {
                binding.ivBot.setImageResource(R.drawable.ic_hikey)
                binding.tvBotMbti.text = getString(R.string.chatbot_hikey)
            }

            "bongbong" -> {
                binding.ivBot.setImageResource(R.drawable.ic_bongbong)
                binding.tvBotMbti.text = getString(R.string.chatbot_bongbong)
            }
        }

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

        setClear()
    }

    // 대화내역 삭제하기 버튼 누르면 대화내역 삭제
    private fun setClear() {
        binding.tvClearChat.setOnClickListener {
            chatBotViewModel.setClearChat()
        }
    }

    // 에딧 텍스트에서 챗봇 검색 실행 코드.
    private fun setSearch() {

        // 키보드에서 검색을 눌렀을 때 질문이 질문을 뷰모델로 넘겨주는 함수
        binding.etAsk.setOnEditorActionListener { textView, actionId, _ ->

            if (textView.text.toString().isBlank()) {
                Toast.makeText(requireContext(),"궁금한 것을 입력해주세요",Toast.LENGTH_SHORT).show()
                true

            } else {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    chatBotViewModel.setSearch(textView.text.toString())
                    (binding.etAsk as TextView).text = getString(R.string.chatbot_clear)

                    // handled가 false이면 검색 클릭 이후 키보드가 비활성화된다.
                }
                // handled 가 항상 false이기 때문에 그냥 false 리턴해줌
                false
            }
        }
    }

    // 채팅 봇 "스피너"를 셋팅해주는 함수
    private fun setBotServiceProvider() {
        binding.spBot.adapter = ArrayAdapter(
            activity as MainActivity,
            android.R.layout.simple_spinner_dropdown_item,
            listOf(
                getString(R.string.chatbot_mbti_t),
                getString(R.string.chatbot_mbti_f)
            )
        )
    }

    // 스피너를 작동 (히키/봉봉이) 을 셋팅해주는 함수
    private fun setBot() {
        chatBotViewModel.setBotSpinner(0)

        binding.spBot.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            // 스피너가 선택 되었을 때 : position 값이 선택된 스피너 값의 인덱스를 알려줌)
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.d(TAG, "1. 스피너 선택 position : $position")

                chatBotViewModel.setBotSpinner(position)

            }

            // 스피너가 선택되지 않았을 때 (초기 상태) : 초기에 봉봉이를 선택해 둠.
            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.ivBot.setImageResource(R.drawable.ic_hikey)
            }
        }
    }

}