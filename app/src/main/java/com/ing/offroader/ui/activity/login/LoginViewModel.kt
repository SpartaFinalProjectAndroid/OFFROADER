package com.ing.offroader.ui.activity.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthCredential
import com.ing.offroader.data.repository.AuthRepository

private const val TAG = "태그 : LoginViewModel"

class LoginViewModel(private val authRepository: AuthRepository) :
    ViewModel() {


    val authenticateUserLiveData: LiveData<ResponseState<LoginUser>> = authRepository.authenticatedUserMutableLiveData

    fun signInWithGoogle(googleAuthCredential: AuthCredential) {
        authRepository.firebaseSignInWithGoogle(googleAuthCredential)
//        _authenticateUserLiveData.value = new.value
        Log.d(TAG, "signInWithGoogle: Got into ViewModel ")

    }


}