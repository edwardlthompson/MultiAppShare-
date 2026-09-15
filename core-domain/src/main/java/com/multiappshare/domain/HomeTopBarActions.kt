package com.multiappshare.domain

object HomeTopBarActions {
    const val REORDER_ICON_KEY = "swap_vert"

    fun showReorder(groupCount: Int): Boolean = groupCount > 1

    fun canReorderWithoutOverflow(groupCount: Int): Boolean = showReorder(groupCount)
}
