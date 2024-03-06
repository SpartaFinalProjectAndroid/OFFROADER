package com.mit.offroader.ui.activity.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData

class MainViewModel : ViewModel() {

    private var _mainUiState = MutableLiveData<MainUiState>()

    val mainUiState : LiveData<MainUiState> = _mainUiState


    // <--------------------------- 라디오 관련 구현들 ---------------------------------->

    private var _radioLikeList : MutableLiveData<MutableList<String>> = MutableLiveData()
    private var copyList : MutableList<String> = mutableListOf()
    val radioLikeList : LiveData<MutableList<String>> = _radioLikeList

    private var _whoPlay : MutableLiveData<String?> = MutableLiveData()
    var whoPlay : MutableLiveData<String?> = _whoPlay
    private var whoPlayTest : String ?= null

    fun addWhoPlay(key: String) {
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