package com.ing.offroader.ui.fragment.mydetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.ing.offroader.data.repository.UserRepository
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

class MyDetailViewModel : ViewModel() {
    private var _myDetailUiState = MutableLiveData<MyDetailUiState>()

    val myDetailUiState : LiveData<MyDetailUiState> = _myDetailUiState





    // ----------------------------- 업적 레벨 관련 기능들 --------------------------------------



    private val _userData : MutableLiveData<String> = MutableLiveData()
    val userData = _userData

    private val userRepository : UserRepository = UserRepository()

    fun getUserData(userUID: String) {
        var test: UserRepository.UserData ?= null
        viewModelScope.launch {
            val timeCheck = measureTimeMillis {
                test = userRepository.getUserData(userUID = userUID)
            }
            //Log.d("민용뷰모델", "name: " + test?.achievements?.sanId?.climb)
            Log.d("민용타임", "time: $timeCheck")
        }
    }
}