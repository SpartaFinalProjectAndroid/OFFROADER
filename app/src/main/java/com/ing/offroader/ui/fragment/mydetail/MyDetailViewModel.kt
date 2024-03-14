package com.ing.offroader.ui.fragment.mydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.ing.offroader.data.repository.UserRepository

class MyDetailViewModel : ViewModel() {
    private var _myDetailUiState = MutableLiveData<MyDetailUiState>()

    val myDetailUiState : LiveData<MyDetailUiState> = _myDetailUiState





    // ----------------------------- 업적 레벨 관련 기능들 --------------------------------------



    private val _userData : MutableLiveData<String> = MutableLiveData()
    val userData = _userData

    private val userRepository : UserRepository = UserRepository()

    fun getUserData(userID: String) {
        userRepository.getUserData(userID = userID)
    }
}