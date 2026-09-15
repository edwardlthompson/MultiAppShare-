package com.multiappshare.domain

enum class GroupOverflowAction {
    MODIFY,
    REORDER,
    RENAME,
    DUPLICATE,
    MERGE,
    PIN,
    SHORTCUT,
    DELETE,
}

object GroupOverflowPolicy {
    val edit = listOf(
        GroupOverflowAction.MODIFY,
        GroupOverflowAction.REORDER,
        GroupOverflowAction.RENAME,
    )
    val organize = listOf(
        GroupOverflowAction.DUPLICATE,
        GroupOverflowAction.MERGE,
        GroupOverflowAction.PIN,
        GroupOverflowAction.SHORTCUT,
    )
    val danger = listOf(GroupOverflowAction.DELETE)

    fun sections(): List<List<GroupOverflowAction>> = listOf(edit, organize, danger)
}
