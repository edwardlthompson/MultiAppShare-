package com.multiappshare.domain

object ListFabPadding {
    const val SINGLE_FAB_DP = 88

    fun bottomDp(inShareMode: Boolean): Int = if (inShareMode) 0 else SINGLE_FAB_DP
}
