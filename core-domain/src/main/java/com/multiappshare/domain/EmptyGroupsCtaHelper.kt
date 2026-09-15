package com.multiappshare.domain

object EmptyGroupsCtaHelper {
    fun shouldShowEmptyCta(groupCount: Int): Boolean = groupCount == 0
}
