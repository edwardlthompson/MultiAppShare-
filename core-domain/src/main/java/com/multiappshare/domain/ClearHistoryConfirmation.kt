package com.multiappshare.domain

object ClearHistoryConfirmation {
    fun shouldShowDialog(historyCount: Int): Boolean = historyCount > 0

    fun formatConfirmationPrompt(historyCount: Int): String {
        return if (historyCount == 1) {
            "Are you sure you want to delete 1 history record?"
        } else {
            "Are you sure you want to delete all $historyCount history records?"
        }
    }
}
