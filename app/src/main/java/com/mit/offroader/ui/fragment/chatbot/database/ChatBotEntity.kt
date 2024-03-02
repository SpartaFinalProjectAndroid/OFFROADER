package com.mit.offroader.ui.fragment.chatbot.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversation_record")
data class ConversationRecord(
    @PrimaryKey
    @ColumnInfo(name = "ai_type")
    val aiType: String,
    @ColumnInfo(name = "role")
    val role: String,
    @ColumnInfo(name = "content")
    val content: String
)


