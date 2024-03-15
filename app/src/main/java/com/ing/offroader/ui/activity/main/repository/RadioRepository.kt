package com.ing.offroader.ui.activity.main.repository

import com.ing.offroader.ui.activity.main.adapters.HttpItem
import com.ing.offroader.ui.activity.main.models.HttpNetWork

class RadioRepository() {

    fun initURL(item: HttpItem) : String {
        return HttpNetWork().httpNetWork(item)
    }
}