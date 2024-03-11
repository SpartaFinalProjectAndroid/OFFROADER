package com.mit.offroader.ui.activity.main.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.mit.offroader.ui.activity.main.adapters.HttpItem
import com.mit.offroader.ui.activity.main.models.HttpNetWork
import com.mit.offroader.ui.activity.main.models.HttpTestInterface

class RadioRepository(private val data: HttpItem, inter: HttpTestInterface) {

    private var listener : HttpTestInterface = inter

    suspend fun initURL() {
       // val test =
        HttpNetWork().httpNetWork(data, listener)
    }
}