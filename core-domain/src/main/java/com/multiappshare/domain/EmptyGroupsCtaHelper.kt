package com.multiappshare.domain

enum class EmptyGroupsAction {
    AUTOFILL_DEFAULT_GROUPS,
    CREATE_EMPTY_GROUP,
}

object EmptyGroupsCtaHelper {
    fun shouldShowEmptyCta(groupCount: Int): Boolean = groupCount == 0

    fun resolveAction(action: EmptyGroupsAction): String {
        return when (action) {
            EmptyGroupsAction.AUTOFILL_DEFAULT_GROUPS -> "Autofill Groups"
            EmptyGroupsAction.CREATE_EMPTY_GROUP -> "Create New Group"
        }
    }
}
