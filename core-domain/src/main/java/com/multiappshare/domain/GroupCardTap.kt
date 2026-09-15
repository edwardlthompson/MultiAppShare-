package com.multiappshare.domain

object GroupCardTap {
    enum class Action { SHARE, SELECT, TOGGLE_EXPAND }

    fun action(inShareMode: Boolean, hasSelectHandler: Boolean): Action = when {
        inShareMode -> Action.SHARE
        hasSelectHandler -> Action.SELECT
        else -> Action.TOGGLE_EXPAND
    }
}
