package com.mit.offroader.activity.intro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class IntroSplashViewModel: ViewModel() {
    private var _introSplashUiState = MutableLiveData<IntroSplashUiState>()

    val introSplashUiState : LiveData<IntroSplashUiState> = _introSplashUiState
}