package com.ing.offroader.ui.activity.chatbot.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.ing.offroader.data.model.ai.Message
import com.ing.offroader.data.repository.AiRepository
import com.ing.offroader.data.repository.BongbongRepository
import com.ing.offroader.data.repository.HikeyRepository
import com.ing.offroader.ui.activity.chatbot.database.BongbongData
import com.ing.offroader.ui.activity.chatbot.database.ChatUiState
import com.ing.offroader.ui.activity.chatbot.database.HikeyData
import kotlinx.coroutines.launch
import java.util.UUID

private const val TAG = "ChatBotViewModel"

class ChatBotViewModel(
    private val aiRepo: AiRepository = AiRepository(),
    private val hikeyRepository: HikeyRepository,
    private val bongbongRepository: BongbongRepository
) : ViewModel() {



    private var _conversationUiState: MutableLiveData<ChatUiState?> = MutableLiveData()

    // 스피너가 업데이트 되었을 때 업데이트 되는 상수
    val conversationUiState: LiveData<ChatUiState?> get() = _conversationUiState

    // 히키와의 대화를 저장하는 상수
    val hikeyUiState: LiveData<ChatUiState?> =
        hikeyRepository.hikeyConversation.map { conversationRecords ->
            createHikeyConversationUiState(conversationRecords)
        }

    // 봉봉이와의 대화를 저장하는 상수
    val bongbongUiState: LiveData<ChatUiState?> =
        bongbongRepository.bongbongConversation.map { conversationRecords ->
            createBongbongConversationUiState(conversationRecords)
        }


    init {
        _conversationUiState.value = ChatUiState(hikeyUiState.value?.chat ?: listOf(), "hikey")
    }


    // 데이터베이스에서 가져온 하이키 메세지를 ChatBotUiState에 추가해주는 함수
    private fun createHikeyConversationUiState(conversationRecords: List<HikeyData>): ChatUiState {
        val conversation: ArrayList<Message> = arrayListOf()

        // 데이터베이스에서 가져온 메세지들을 히키랑 봉봉이 메세지로 나누는 부분
        conversationRecords.forEach { record ->
            conversation.add(Message(record.role, record.content))
        }

        return ChatUiState(conversation.toList(), "hikey")
    }

    // 데이터베이스에서 가져온 봉봉이 메세지를 ChatBotUiState에 추가해주는 함수
    private fun createBongbongConversationUiState(conversationRecords: List<BongbongData>): ChatUiState {
        val conversation: ArrayList<Message> = arrayListOf()

        // 데이터베이스에서 가져온 메세지들을 히키랑 봉봉이 메세지로 나누는 부분
        conversationRecords.forEach { record ->
            conversation.add(Message(record.role, record.content))
        }
        return ChatUiState(conversation.toList(), "bongbong")
    }


    // 스피너 설정에 따라서 메세지 화면에 들어갈 대화 목록을 바꾸어주는 함수
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

    // 대화 목록을 처음 띄울때 초반에 대화를 세팅해주는 함수
    private fun setInitConversationUiState(uiState: ChatUiState?) =
        uiState?.let {
            conversationUiState.value?.copy(
                chat = it.chat,
                position = it.position
            )
        }


    // DB가 텅 비어있을 때 대화를 세팅해주는 함수
    private fun conversationWhenDbNull(aiType: String) =
        conversationUiState.value?.copy(
            chat = listOf(),
            position = aiType
        )


    // 사용자가 작성한 메세지를 DB에 저장해주는 함수
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

    // ai의 답변을 받아오고 그 값을 DB에 넘겨주는 함수
    private fun getReplyFromChatGpt(text: String, input: String) {

        viewModelScope.launch {
            runCatching {

                addMessageToDB("user", text)

                when (conversationUiState.value?.position) {
                    "hikey" -> addMessageToDB(
                        "assistant",
                        aiRepo.hikeyChatCompletion(text).choices.first().message.content
                    )

                    "bongbong" -> addMessageToDB(
                        "assistant",
                        aiRepo.bongbongChatCompletion(text).choices.first().message.content
                    )
                }




            }.onSuccess {
                Log.d("Connect ChatGPT", "^^Successful!")

            }.onFailure {
                Log.d("Connect ChatGPT", "^^Failed!")

            }
        }

    }

    // chatgpt에 검색을 할 떄 F처럼 T처럼 대답하게 하기위해 설정해주는 함수
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


    // DB에 메세지를 지우는 함수
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