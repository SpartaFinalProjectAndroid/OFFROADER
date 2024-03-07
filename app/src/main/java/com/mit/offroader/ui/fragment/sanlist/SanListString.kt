package com.mit.offroader.ui.fragment.sanlist

import androidx.annotation.StringRes
import com.mit.offroader.R

enum class SanListString(
    @StringRes val string: Int
) {
    EASY(R.string.san_list_easy),
    INTERMEDIATE(R.string.san_list_intermediate),
    HARD(R.string.san_list_hard),
    DIVIDER(R.string.san_list_divder)

    ;
}