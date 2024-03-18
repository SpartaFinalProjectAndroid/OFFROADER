package com.ing.offroader.ui.activity.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthCredential

private const val TAG = "태그 : LoginViewModel"

class LoginViewModel(private val authRepository: AuthRepository) :
    ViewModel() {

    private var _authenticateUserLiveData: MutableLiveData<ResponseState<LoginUser>> =
        MutableLiveData()
    val authenticateUserLiveData: LiveData<ResponseState<LoginUser>> = _authenticateUserLiveData

    fun signInWithGoogle(googleAuthCredential: AuthCredential) {
        val new = authRepository.firebaseSignInWithGoogle(googleAuthCredential)
        _authenticateUserLiveData.value = new.value
        Log.d(TAG, "signInWithGoogle: Got into ViewModel ${new.value}")

    }


}