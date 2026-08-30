package com.multiappshare.domain

object PackageChangeFilter {
    const val ACTION_PACKAGE_ADDED = "android.intent.action.PACKAGE_ADDED"
    const val ACTION_PACKAGE_REMOVED = "android.intent.action.PACKAGE_REMOVED"
    const val ACTION_PACKAGE_REPLACED = "android.intent.action.PACKAGE_REPLACED"
    const val ACTION_PACKAGE_CHANGED = "android.intent.action.PACKAGE_CHANGED"

    private val RELEVANT_ACTIONS = setOf(
        ACTION_PACKAGE_ADDED,
        ACTION_PACKAGE_REMOVED,
        ACTION_PACKAGE_REPLACED,
        ACTION_PACKAGE_CHANGED,
    )

    fun isRelevantAction(action: String?): Boolean {
        if (action == null) return false
        return action in RELEVANT_ACTIONS
    }
}
