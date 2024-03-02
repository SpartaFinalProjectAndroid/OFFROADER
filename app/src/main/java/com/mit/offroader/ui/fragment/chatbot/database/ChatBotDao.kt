package com.mit.offroader.ui.fragment.chatbot.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatBotDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE) // INSERT, key 충돌이 나도 무시하고 뒤에 추가해주도록 함.
    suspend fun insertChat(chat:ConversationRecord)
    @Query("SELECT * FROM conversation_record")
    fun getConversation(): Flow<List<ConversationRecord>>

    @Query("DELETE FROM conversation_record")
    suspend fun deleteConversation()

}