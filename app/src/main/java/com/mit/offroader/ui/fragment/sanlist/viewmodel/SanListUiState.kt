package com.mit.offroader.ui.fragment.sanlist.viewmodel

import com.mit.offroader.ui.fragment.sanlist.model.SanDTO
import com.mit.offroader.ui.fragment.sanlist.model.SanListRepository
import com.mit.offroader.utils.Application

data class SanListUiState(
    var selectedItem: SanDTO?
) {
    companion object {
        fun init() = SanListUiState(
            selectedItem = null
        )
    }
}
