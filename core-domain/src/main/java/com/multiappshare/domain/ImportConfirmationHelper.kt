package com.multiappshare.domain

object ImportConfirmationHelper {
    fun shouldPromptConfirmation(
        existingGroupCount: Int,
        isReplaceStrategy: Boolean,
    ): Boolean {
        return isReplaceStrategy && existingGroupCount > 0
    }
}
