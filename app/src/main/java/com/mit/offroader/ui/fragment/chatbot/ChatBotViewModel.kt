package com.mit.offroader.ui.fragment.chatbot

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.mit.offroader.data.model.ai.Message
import com.mit.offroader.data.repository.AiRepository
import com.mit.offroader.ui.fragment.chatbot.database.BongbongData
import com.mit.offroader.ui.fragment.chatbot.database.HikeyData
import kotlinx.coroutines.launch
import java.util.UUID

private const val TAG = "ChatBotViewModel"

class ChatBotViewModel(
    private val aiRepo: AiRepository = AiRepository(),
    private val hikeyRepository: HikeyRepository,
    private val bongbongRepository: BongbongRepository
) : ViewModel() {


    private var _spinnerPosition: MutableLiveData<String> = MutableLiveData()

    private var _conversationUiState: MutableLiveData<ChatUiState?> = MutableLiveData()
    val conversationUiState: LiveData<ChatUiState?> get() = _conversationUiState


    val hikeyUiState: LiveData<ChatUiState?> =
        hikeyRepository.hikeyConversation.map { conversationRecords ->
            createHikeyConversationUiState(conversationRecords)

        }

    val bongbongUiState: LiveData<ChatUiState?> =
        bongbongRepository.bongbongConversation.map { conversationRecords ->
            createBongbongConversationUiState(conversationRecords)
        }

    init {
        _conversationUiState.value = ChatUiState.init()
    }


    private fun createHikeyConversationUiState(conversationRecords: List<HikeyData>): ChatUiState {
        val conversation: ArrayList<Message> = arrayListOf()

        // 데이터베이스에서 가져온 메세지들을 히키랑 봉봉이 메세지로 나누는 부분
        conversationRecords.forEach { record ->
            conversation.add(Message(record.role, record.content))
        }

        return ChatUiState(conversation.toList(), "hikey")
    }

    private fun createBongbongConversationUiState(conversationRecords: List<BongbongData>): ChatUiState {
        val conversation: ArrayList<Message> = arrayListOf()

        // 데이터베이스에서 가져온 메세지들을 히키랑 봉봉이 메세지로 나누는 부분
        conversationRecords.forEach { record ->
            conversation.add(Message(record.role, record.content))
        }
        return ChatUiState(conversation.toList(), "bongbong")
    }


    fun setBotSpinner(position: Int) {
        Log.d(TAG, "2. setBotSpinner, position : $position")
        Log.d(
            TAG,
            "3. setBotSpinner, conversationUiState : ${conversationUiState.value.toString()}"
        )
        Log.d(TAG, "3. setBotSpinner, hikeyUiState : ${hikeyUiState.value.toString()}")

        _conversationUiState.value = when (position) {
            0 -> {
                if (hikeyUiState.value == null) {
                    conversationWhenDbNull("hikey")

                } else {
                    setInitConversationUiState(hikeyUiState.value)
                }
            }

            1 -> {
                if (bongbongUiState.value == null) {
                    conversationWhenDbNull("bongbong")
                } else {
                    setInitConversationUiState(bongbongUiState.value)

                }
            }

            else -> {
                hikeyUiState.value?.let {
                    conversationUiState.value?.copy(
                        chat = it.chat,
                        position = it.position
                    )
                }
            }
        }


    }

    private fun setInitConversationUiState(uiState: ChatUiState?) =
        uiState?.let {
            conversationUiState.value?.copy(
                chat = it.chat,
                position = it.position
            )
        }


    private fun conversationWhenDbNull(aiType: String) =
        conversationUiState.value?.copy(
            chat = listOf(),
            position = aiType
        )


    private suspend fun addMessageToDB(role: String, message: String) {
        when (conversationUiState.value?.position) {
            "hikey" -> {
                hikeyRepository.insertHikeyChat(
                    HikeyData(
                        UUID.randomUUID().toString(),
                        role,
                        message
                    )
                )
            }

            "bongbong" -> {
                bongbongRepository.insertBongbongChat(
                    BongbongData(
                        UUID.randomUUID().toString(),
                        role,
                        message
                    )
                )
            }

        }


    }

    private fun getReplyFromChatGpt(text: String, input: String) {
        viewModelScope.launch {
            runCatching {

                addMessageToDB("user", text)

                addMessageToDB(
                    "assistant",
                    aiRepo.createChatCompletion(input).choices.first().message.content
                )

            }.onSuccess {
                Log.d("Connect ChatGPT", "^^Successful!")

            }.onFailure {
                Log.d("Connect ChatGPT", "^^Failed!")

            }
        }

    }

    fun setSearch(text: String) {
        when (conversationUiState.value?.position) {
            "hikey" -> {

                getReplyFromChatGpt(
                    text,
                    "$text. 최대 두문장으로 간단히 대답해줘. 대답할 때 감정은 배제하고 오로지 사실에 기반해서만 대답해줘."
                )

            }

            "bongbong" -> {
                getReplyFromChatGpt(
                    text,
                    "$text. 최대 두문장으로 간단히 대답해줘. 대답할 때 내가 상처받지 않게 내 감정을 살펴가며 대답해줘."
                )

            }

        }


    }


    fun setClearChat() {
        viewModelScope.launch {
            when (conversationUiState.value?.position) {
                "hikey" -> {
                    hikeyRepository.deleteHikeyConversation()
                }

                "bongbong" -> {
                    bongbongRepository.deleteBongbongConversation()
                }
            }

        }
    }

}