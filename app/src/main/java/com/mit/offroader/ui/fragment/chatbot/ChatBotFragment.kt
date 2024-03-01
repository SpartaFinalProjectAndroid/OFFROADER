package com.mit.offroader.ui.fragment.chatbot

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mit.offroader.R
import com.mit.offroader.databinding.FragmentChatBotBinding
import com.mit.offroader.ui.activity.main.MainActivity

class ChatBotFragment : Fragment() {


    private var _binding: FragmentChatBotBinding? = null
    private val binding get() = _binding!!
    private val chatBotViewModel by viewModels<ChatBotViewModel>()
    private val chatAdapter: ChatAdapter by lazy { ChatAdapter(chatBotViewModel) }


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

    override fun onResume() {
        super.onResume()
        Log.d("onResume in ChatBotFragment", "이거 다음에어댑터 연결하고 서브밋함..")

        Log.d("서브밋","${Conversation.hikeyConversation}")
        binding.rvChatbot.adapter = chatAdapter
        chatAdapter.submitList(Conversation.hikeyConversation)

    }

    private fun initObserver() {
        Log.d("옵져빙 함수", "^^ 옵져빙 되는 중")
        chatBotViewModel.chatBotUiState.observe(viewLifecycleOwner) {

            Log.d("CHATGPT CHATBOT", "^^ 값 가져오기 + 이 다음에 서브밋함. ${it.chatContent}")
            binding.rvChatbot.adapter = chatAdapter
            chatAdapter.submitList(it.chatContent)

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

        binding.etAsk.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        // 키보드에서 검색을 눌렀을 때 질문이 질문을 뷰모델로 넘겨주는 함수
        binding.etAsk.setOnEditorActionListener { textView, actionId, keyEvent ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Log.d("chatbot Fragment", "^^ setOnEditorAction Listener : ${textView.text.toString()}")
                chatBotViewModel.onSearch(textView.text.toString())
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
                        setChat(position)
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
        chatAdapter.submitList(Conversation.hikeyConversation)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}