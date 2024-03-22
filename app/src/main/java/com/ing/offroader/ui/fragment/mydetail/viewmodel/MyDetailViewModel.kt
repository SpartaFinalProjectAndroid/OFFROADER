package com.ing.offroader.ui.fragment.mydetail.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ing.offroader.data.model.userInfo.UserData
import com.ing.offroader.data.repository.SanListRepository
import com.ing.offroader.data.repository.UserRepository
import com.ing.offroader.ui.fragment.mydetail.MyDetailDTO
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

class MyDetailViewModel(sanListRepository: SanListRepository) : ViewModel() {
    private val repo : SanListRepository = sanListRepository

    val myDetailDTO : LiveData<MyDetailDTO> = repo.myInfo


    // ----------------------------- 업적 레벨 관련 기능들 --------------------------------------


    private val _userData : MutableLiveData<UserData> = MutableLiveData()
    val userData : LiveData<UserData> = _userData

    private val userRepository : UserRepository = UserRepository()


    // UID를 받아와서 UserRepository의 getUserData 함수를 호출 하여
    // 파이어스토에서 해당 UID에 맞는 데이터를 가져와서 _userData LiveData에 저장한다.
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