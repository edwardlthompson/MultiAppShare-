package com.multiappshare.domain

object ImportConfirmationHelper {
    fun shouldPromptConfirmation(
        existingGroupCount: Int,
        isReplaceStrategy: Boolean,
    ): Boolean {
        return isReplaceStrategy && existingGroupCount > 0
    }

    fun formatWarningMessage(existingGroupCount: Int): String {
        return if (existingGroupCount == 1) {
            "Importing in Replace mode will overwrite 1 existing group."
        } else {
            "Importing in Replace mode will overwrite $existingGroupCount existing groups."
        }
    }
}
