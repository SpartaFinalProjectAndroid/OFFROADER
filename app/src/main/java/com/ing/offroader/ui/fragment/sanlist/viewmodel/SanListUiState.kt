package com.ing.offroader.ui.fragment.sanlist.viewmodel

import com.ing.offroader.ui.fragment.sanlist.model.SanDTO

data class SanListUiState(
    var selectedItem: SanDTO?
) {
    companion object {
        fun init() = SanListUiState(
            selectedItem = null
        )
    }
}
