package com.mit.offroader.ui.fragment.chatbot

import android.app.Application
import com.mit.offroader.ui.fragment.chatbot.database.ChatBotDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ChatBotApplication: Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { ChatBotDatabase.getDatabase(this, applicationScope)}
    val repository by lazy { ChatBotRepository(database.getChatBotDao())}
}