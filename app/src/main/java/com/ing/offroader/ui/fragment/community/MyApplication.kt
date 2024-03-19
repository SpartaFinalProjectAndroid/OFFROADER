package com.ing.offroader.ui.fragment.community

import android.app.Application
import android.content.Context
import com.ing.offroader.data.repository.EventRepository
import com.ing.offroader.data.repository.SanListRepository
import com.ing.offroader.ui.activity.chatbot.database.BongbongDatabase
import com.ing.offroader.ui.activity.chatbot.database.HikeyDatabase
import com.ing.offroader.data.repository.BongbongRepository
import com.ing.offroader.data.repository.HikeyRepository
import com.ing.offroader.data.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob


class MyApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    val hikeyDatabase by lazy { HikeyDatabase.getDatabase(this, applicationScope) }
    val hikeyRepository by lazy { HikeyRepository(hikeyDatabase.getChatBotDao()) }
    val bongbongDatabase by lazy { BongbongDatabase.getDatabase(this, applicationScope) }
    val bongbongRepository by lazy { BongbongRepository(bongbongDatabase.getChatBotDao()) }

    val sanListRepository by lazy { SanListRepository() }
    val authRepository by lazy { AuthRepository() }
    val eventRepository by lazy { EventRepository() }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object {
        var appContext: Context? = null
            private set
    }
}