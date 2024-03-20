package com.ing.offroader.ui.fragment.chatbot

import android.app.Application
import android.content.Context
import com.ing.offroader.data.repository.BongbongRepository
import com.ing.offroader.data.repository.EventRepository
import com.ing.offroader.data.repository.HikeyRepository
import com.ing.offroader.data.repository.SanListRepository
import com.ing.offroader.ui.activity.chatbot.database.BongbongDatabase
import com.ing.offroader.ui.activity.chatbot.database.HikeyDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class MyApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    val hikeyDatabase by lazy { HikeyDatabase.getDatabase(this, applicationScope) }
    val hikeyRepository by lazy { HikeyRepository(hikeyDatabase.getChatBotDao()) }
    val bongbongDatabase by lazy { BongbongDatabase.getDatabase(this, applicationScope) }
    val bongbongRepository by lazy { BongbongRepository(bongbongDatabase.getChatBotDao()) }

    val sanListRepository by lazy { SanListRepository() }
    val eventRepository by lazy { EventRepository() }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        
//        SanListRepository().setSanDetail("dlfma")

        CoroutineScope(Dispatchers.IO).launch {
            SanListRepository().loadAllSanList()
        }

    }

    companion object {
        var appContext: Context? = null
            private set
    }
}