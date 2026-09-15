package com.multiappshare.domain

object DestructiveActions {
    const val PANE_DELETE_USES_ERROR_COLOR = true

    fun looksLikePrimarySave(isDelete: Boolean, usesErrorColor: Boolean): Boolean =
        isDelete && !usesErrorColor
}
