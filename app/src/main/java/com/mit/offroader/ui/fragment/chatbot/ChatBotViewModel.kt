package com.mit.offroader.ui.fragment.chatbot

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mit.offroader.data.model.ai.Message
import com.mit.offroader.data.repository.AiRepository
import com.mit.offroader.ui.fragment.chatbot.database.ConversationRecord
import kotlinx.coroutines.launch
import java.util.UUID

class ChatBotViewModel(private val aiRepo: AiRepository = AiRepository(), private val chatBotRepository: ChatBotRepository) : ViewModel() {


    private var _chatBotUiState = MutableLiveData<ChatBotUiState>()

    val chatBotUiState: LiveData<ChatBotUiState> = _chatBotUiState

    var conversation: LiveData<List<ConversationRecord>> = chatBotRepository.conversation.asLiveData()

    init {
        _chatBotUiState.value = ChatBotUiState.init()
    }

    fun setBotSpinner(position: Int) {
        when (position) {
            0 -> {
                // TODO : 하이키 채팅 목록 업데이트 해주기
                setChat()
            }

            1 -> {
                // TODO : 봉봉이 채팅 목록 업데이트 해주기
                setChat()
            }
        }

    }

    fun onSearch(text: String) {
        lateinit var channelResponse : String
        viewModelScope.launch {
            runCatching {

                val message = "$text. 한 문장으로 대답해줘. "
                val myUuId = UUID.randomUUID().toString()
                val input = ConversationRecord(myUuId, "hikey","user",text)
                chatBotRepository.insertChat(input)

                Log.d("Connect ChatGpt", "^^ -1. $message")

                channelResponse =
                    aiRepo.createChatCompletion(text).choices.first().message.content

                Log.d("Connect ChatGPT", "^^ 0. channelResponse = $channelResponse")

                Log.d("Connect ChatGPT", "^^ 1. ViewModel")



            }.onSuccess {
                Log.d("Connect ChatGPT", "^^Successful!")
                val myUuId = UUID.randomUUID()
                val chat = ConversationRecord(myUuId.toString(),"hikey", "assistant", channelResponse)
                chatBotRepository.insertChat(chat)
                setChat()

//                _chatBotUiState.value = _chatBotUiState.value?.copy(chatContent = Conversation.hikeyConversation)
            }.onFailure {
                Log.d("Connect ChatGPT", "^^Failed!")

            }
        }

    }



    private fun setChat() {
        conversation.value?.apply {
            val hikeyConversation: ArrayList<Message> = arrayListOf()
            val bongbongConversation: ArrayList<Message> = arrayListOf()
            for (i in this.indices) {
                when (this[i].aiType) {
                    "hikey" -> {
                        hikeyConversation.add(Message(this[i].role, this[i].content))
                    }
                    "bongbong" -> {
                        bongbongConversation.add(Message(this[i].role, this[i].content))
                    }
                }
            }
            _chatBotUiState.value = _chatBotUiState.value?.copy(chatWithHikey = hikeyConversation, chatWithBongBong = bongbongConversation)
        }

    }

}