package com.ing.offroader.ui.activity.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ing.offroader.ui.activity.main.adapters.HttpItem
import com.ing.offroader.ui.activity.main.models.HttpTestInterface
import com.ing.offroader.ui.activity.main.repository.RadioRepository
import kotlinx.coroutines.launch


class MainViewModel(application: Application) : AndroidViewModel(application), HttpTestInterface {

    private var _mainUiState = MutableLiveData<MainUiState>()

    val mainUiState : LiveData<MainUiState> = _mainUiState


    // <--------------------------- 라디오 관련 구현들 ---------------------------------->

    private var _radioLikeList : MutableLiveData<MutableList<String>> = MutableLiveData()
    private var copyList : MutableList<String> = mutableListOf()
    val radioLikeList : LiveData<MutableList<String>> = _radioLikeList

    private var _whoPlay : MutableLiveData<String?> = MutableLiveData()
    var whoPlay : MutableLiveData<String?> = _whoPlay
    private var whoPlayTest : String ?= null

//    private var _channelURL : MutableLiveData<String?> = MutableLiveData()
//    val channelURL : LiveData<String?> = _channelURL

    private var radioRepository : RadioRepository?= null
    private var _channelURL : MutableLiveData<String> = MutableLiveData()
    val channelURL : LiveData<String> = _channelURL


    fun getHttpNetWork(item: HttpItem) {
        viewModelScope.launch {
            radioRepository = RadioRepository(item, object : HttpTestInterface {
                override fun onReceive(item: String) {
                    //_channelURL.postValue(item)
                    _channelURL.value = item
                }
            })
            radioRepository?.initURL()
        }
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

    override fun onReceive(item: String) {
        Log.i("Minyong Main", item)
    }
}