package com.ing.offroader.ui.fragment.mydetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.ing.offroader.data.model.userInfo.UserData
import com.ing.offroader.data.repository.UserRepository
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

class MyDetailViewModel : ViewModel() {
    private var _myDetailUiState = MutableLiveData<MyDetailUiState>()

    val myDetailUiState : LiveData<MyDetailUiState> = _myDetailUiState


    // ----------------------------- 업적 레벨 관련 기능들 --------------------------------------


    private val _userData : MutableLiveData<UserData> = MutableLiveData()
    val userData : LiveData<UserData> = _userData

    private val userRepository : UserRepository = UserRepository()

    fun getUserData(userUID: String) {
        viewModelScope.launch {
            val timeCheck = measureTimeMillis {
                _userData.value = userRepository.getUserData(userUID = userUID)
                Log.d("민용뷰모델", "name: ${userData.value}")
            }

            Log.d("민용타임", "time: $timeCheck")
        }
    }
}