package com.ing.offroader.ui.activity.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ing.offroader.ui.activity.main.adapters.HttpItem
import com.ing.offroader.ui.activity.main.repository.RadioRepository
import com.ing.offroader.ui.activity.sandetail.MyLikedSan


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var _mainUiState = MutableLiveData<MainUiState>()

    val mainUiState : LiveData<MainUiState> = _mainUiState

    // <--------------------------- 좋아요 관련 구현들 ---------------------------------->
    private val _sanLikedList : MutableLiveData<MutableList<MyLikedSan>> = MutableLiveData()
    private var sanLikedCopyList : MutableList<MyLikedSan> = mutableListOf()
    val sanLikedList : LiveData<MutableList<MyLikedSan>> = _sanLikedList

    fun addSanLikedList(data: MyLikedSan) {
        sanLikedCopyList.add(data)
        _sanLikedList.value = sanLikedCopyList
//        Log.d(TAG, "addSanLikedList = ${data}")
    }

    fun removeSanLikedList(data: MyLikedSan) {
        sanLikedCopyList.remove(data)
        _sanLikedList.value = sanLikedCopyList
//        Log.d(TAG, "removeSanLikedList = ${data}")
    }

    fun loadSanLikedList(data: MutableList<MyLikedSan>) {
        sanLikedCopyList = data
        _sanLikedList.value = sanLikedCopyList
    }

    // <--------------------------- 라디오 관련 구현들 ---------------------------------->

    private var _radioLikeList : MutableLiveData<MutableList<String>> = MutableLiveData()
    private var copyList : MutableList<String> = mutableListOf()
    val radioLikeList : LiveData<MutableList<String>> = _radioLikeList

    private var _whoPlay : MutableLiveData<String?> = MutableLiveData()
    val whoPlay : MutableLiveData<String?> = _whoPlay
    private var whoPlayTest : String ?= null

    private var _isPlaying : MutableLiveData<Boolean> = MutableLiveData(false)
    val isPlaying : LiveData<Boolean> = _isPlaying

    private var _channelUrl : MutableLiveData<String?> = MutableLiveData()
    var channelUrl : MutableLiveData<String?> = _channelUrl

    private var _httpItem : MutableLiveData<HttpItem> = MutableLiveData()
    val httpItem : LiveData<HttpItem> = _httpItem

    fun addHttpItem(item: HttpItem) {
        _httpItem.value = item
    }
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

    fun checkIsPlaying(isPlay: Boolean) {
        _isPlaying.value = isPlay
    }
}