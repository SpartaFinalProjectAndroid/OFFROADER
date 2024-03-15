package com.ing.offroader.ui.activity.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ing.offroader.ui.activity.main.adapters.HttpItem
import com.ing.offroader.ui.activity.main.repository.RadioRepository


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var _mainUiState = MutableLiveData<MainUiState>()

    val mainUiState : LiveData<MainUiState> = _mainUiState


    // <--------------------------- 라디오 관련 구현들 ---------------------------------->

    private var _radioLikeList : MutableLiveData<MutableList<String>> = MutableLiveData()
    private var copyList : MutableList<String> = mutableListOf()
    val radioLikeList : LiveData<MutableList<String>> = _radioLikeList

    private var _whoPlay : MutableLiveData<String?> = MutableLiveData()
    var whoPlay : MutableLiveData<String?> = _whoPlay
    private var whoPlayTest : String ?= null

    private var _channelUrl : MutableLiveData<String?> = MutableLiveData()
    var channelUrl : MutableLiveData<String?> = _channelUrl

    fun getHttpNetWork(item: HttpItem) : String {
        return RadioRepository().initURL(item)
    }

    fun addChannelUrl(url: String) {
        _channelUrl.value = url
    }

    fun addWhoPlay(key: String?) {
        whoPlayTest = key
        _whoPlay.value = whoPlayTest
    }

    fun addList(key : String) {
        copyList.add(key)
        _radioLikeList.value = copyList
    }

    fun removeList(key : String) {
        copyList.remove(key)
        _radioLikeList.value = copyList
    }

    fun loadRadioData(list : MutableList<String>) {
        copyList = list
        _radioLikeList.value = copyList
    }
}