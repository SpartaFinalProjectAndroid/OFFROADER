package com.mit.offroader.ui.fragment.chatbot

import android.app.Application
import android.content.Context
import com.mit.offroader.ui.fragment.chatbot.database.BongbongDatabase
import com.mit.offroader.ui.fragment.chatbot.database.HikeyDatabase
import com.mit.offroader.ui.fragment.chatbot.viewmodel.BongbongRepository
import com.mit.offroader.ui.fragment.chatbot.viewmodel.HikeyRepository
import com.mit.offroader.ui.fragment.home.HomeDataRepository
import com.mit.offroader.ui.fragment.sanlist.model.SanListRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob


class MyApplication: Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    val hikeyDatabase by lazy { HikeyDatabase.getDatabase(this, applicationScope) }
    val hikeyRepository by lazy { HikeyRepository(hikeyDatabase.getChatBotDao()) }
    val bongbongDatabase by lazy { BongbongDatabase.getDatabase(this, applicationScope) }
    val bongbongRepository by lazy { BongbongRepository(bongbongDatabase.getChatBotDao()) }

    val sanListDTO by lazy {}
    val sanListRepository by lazy { SanListRepository() }
    val homeDataRepository by lazy { HomeDataRepository() }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object {
        var appContext: Context? = null
            private set
    }
}