package com.mit.offroader.ui.fragment.chatbot

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mit.offroader.data.model.ai.Message
import com.mit.offroader.data.repository.AiRepository
import com.mit.offroader.ui.fragment.chatbot.database.Conversation
import com.mit.offroader.ui.fragment.chatbot.database.ConversationRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class ChatBotViewModel(private val aiRepo: AiRepository = AiRepository(), private val chatBotRepository: ChatBotRepository) : ViewModel() {


    private var _chatBotUiState = MutableLiveData<ChatBotUiState>()

    val chatBotUiState: LiveData<ChatBotUiState> = _chatBotUiState

    val conversation: LiveData<List<ConversationRecord>> = chatBotRepository.conversation.asLiveData()

    init {
        _chatBotUiState.value = ChatBotUiState.init()
        conversation = chatBotRepository.insertChat(ConversationRecord("hikey","assistant", "궁금한걸 물어보세요!"))
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

                val message = "한 문장으로 대답해줘. $text"

                Log.d("Connect ChatGpt", "^^ -1. $message")

                channelResponse =
                    aiRepo.createChatCompletion(text).choices.first().message.content

                Log.d("Connect ChatGPT", "^^ 0. channelResponse = $channelResponse")

                Log.d("Connect ChatGPT", "^^ 1. ViewModel")
                Conversation.addText(Message("user", text))
                Conversation.addText(Message("assistant",channelResponse))



            }.onSuccess {
                Log.d("Connect ChatGPT", "^^Successful!")
                val chat = ConversationRecord("hikey", "user", channelResponse)
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