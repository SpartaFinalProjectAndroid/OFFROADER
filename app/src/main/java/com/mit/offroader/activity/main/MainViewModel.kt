package com.mit.offroader.activity.main

import android.icu.util.TimeUnit.values
import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mit.offroader.R
import com.mit.offroader.fragment.chatbot.ChatBotFragment
import com.mit.offroader.fragment.home.HomeFragment
import com.mit.offroader.fragment.map.MapFragment
import com.mit.offroader.fragment.mydetail.MyDetailFragment
import com.mit.offroader.fragment.sanlist.SanListFragment

class MainViewModel : ViewModel() {


    private var _mainUiState = MutableLiveData<MainUiState>()

    val mainUiState : LiveData<MainUiState> = _mainUiState

    // bottom Navigation View 셋팅 (디폴트 프래그먼트: 홈 프래그먼트)
//    fun onBottomItemChanged(id: Int) {
//        when (id) {
//            MainFragmentId.HOME. -> {
//
//            }
//        }
//    }

    
}