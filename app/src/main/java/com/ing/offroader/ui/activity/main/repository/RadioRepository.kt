package com.ing.offroader.ui.activity.main.repository

import com.ing.offroader.ui.activity.main.adapters.HttpItem
import com.ing.offroader.ui.activity.main.models.HttpNetWork
import com.ing.offroader.ui.activity.main.models.HttpTestInterface

class RadioRepository(private val data: HttpItem, inter: HttpTestInterface) {

    private var listener : HttpTestInterface = inter

    suspend fun initURL() {
       // val test =
        HttpNetWork().httpNetWork(data, listener)
    }
}