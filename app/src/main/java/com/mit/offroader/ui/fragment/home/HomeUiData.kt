package com.mit.offroader.ui.fragment.home

sealed class HomeUiData{

//    data class Profile(
//        val name: String?,
//        val image: String?
//    ): HomeUiData()

    data object First : HomeUiData()
    data object Second : HomeUiData()
    data object Third : HomeUiData()

}
