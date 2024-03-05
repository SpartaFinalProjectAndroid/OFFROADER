package com.mit.offroader.ui.fragment.chatbot

import android.os.Bundle
import com.mit.offroader.data.model.ai.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mit.offroader.R
import com.mit.offroader.databinding.FragmentChatBotBinding
import com.mit.offroader.ui.activity.main.MainActivity
import com.mit.offroader.ui.fragment.chatbot.adapter.ChatAdapter

private const val TAG = "ChatBotFragment"

class ChatBotFragment : Fragment() {


    private var _binding: FragmentChatBotBinding? = null
    private val binding get() = _binding!!
    private val chatBotViewModel: ChatBotViewModel by viewModels {
        ChatBotViewModelFactory(
            (requireActivity().application as ChatBotApplication).hikeyRepository,
            (requireActivity().application as ChatBotApplication).bongbongRepository
        )
    }
    private val chatAdapter: ChatAdapter by lazy {
        ChatAdapter(chatBotViewModel)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatBotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvChatbot.adapter = chatAdapter
        initView()
        initViewModel()
        initObserver()

    }

    private fun initObserver() {


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


        chatBotViewModel.hikeyUiState.observe(viewLifecycleOwner) {
            if (it == null) {
                chatAdapter.submitList(listOf())

            } else {
                chatAdapter.submitList(it.chat)
            }


        }
        chatBotViewModel.bongbongUiState.observe(viewLifecycleOwner) {
            if (it == null) {
                chatAdapter.submitList(listOf())

            } else {
                chatAdapter.submitList(it.chat)
            }

        }


    }

    private fun chooseAiChatBot(message: List<Message>?, position: String) {
        chatAdapter.submitList(message)
        if (message != null) {
            if (message.isNotEmpty()) {
                binding.rvChatbot.smoothScrollToPosition(message.lastIndex)
            }
        }

        when (position) {
            "hikey" -> {
                binding.ivBot.setImageResource(R.drawable.ic_hikey)
                binding.tvBotMbti.text = getString(R.string.chatbot_mbti_t)
            }

            "bongbong" -> {
                binding.ivBot.setImageResource(R.drawable.ic_bongbong)
                binding.tvBotMbti.text = getString(R.string.chatbot_mbti_f)
            }
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

        setClear()
    }

    private fun setClear() {
        binding.tvClearChat.setOnClickListener {
            chatBotViewModel.setClearChat()
        }
    }

    // 에딧 텍스트에서 챗봇 검색 실행 코드.
    private fun setSearch() {


        // 키보드에서 검색을 눌렀을 때 질문이 질문을 뷰모델로 넘겨주는 함수
        binding.etAsk.setOnEditorActionListener { textView, actionId, keyEvent ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                chatBotViewModel.setSearch(textView.text.toString())
                (binding.etAsk as TextView).text = getString(R.string.chatbot_clear)
                // handled가 false이면 검색 클릭 이후 키보드가 비활성화된다.
                handled = false
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
                binding.ivBot.setImageResource(R.drawable.ic_bongbong)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}