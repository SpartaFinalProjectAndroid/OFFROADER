package com.mit.offroader.ui.fragment.chatbot

import androidx.annotation.WorkerThread
import androidx.lifecycle.asLiveData
import com.mit.offroader.ui.fragment.chatbot.database.ChatBotDao
import com.mit.offroader.ui.fragment.chatbot.database.ChatBotDatabase
import com.mit.offroader.ui.fragment.chatbot.database.ConversationRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext


// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class ChatBotRepository(private val chatBotDao: ChatBotDao) {
    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val conversation: Flow<List<ConversationRecord>> = chatBotDao.getConversation()
    @Suppress("RedundantSuspendModifier") // suspend 키워드가 중복되어 사용되었을 때 나타나는 경고를 무시하도록 지정
    @WorkerThread  // 해당 메서드가 백그라운드 스레드에서 실행되어야 함
    suspend fun deleteConversation() {
        chatBotDao.deleteConversation()
    }
    @Suppress("RedundantSuspendModifier") // suspend 키워드가 중복되어 사용되었을 때 나타나는 경고를 무시하도록 지정
    @WorkerThread  // 해당 메서드가 백그라운드 스레드에서 실행되어야 함
    suspend fun insertChat(chat: ConversationRecord) {
        chatBotDao.insertChat(chat)
    }


}