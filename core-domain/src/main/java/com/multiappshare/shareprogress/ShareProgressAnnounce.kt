package com.multiappshare.shareprogress

data class ShareProgressSnapshot(
    val step: Int,
    val total: Int,
    val target: String,
)

object ShareProgressAnnounce {
    fun snapshot(currentIndex: Int, total: Int, targetLabel: String): ShareProgressSnapshot? {
        if (total <= 0 || currentIndex !in 0 until total) return null
        return ShareProgressSnapshot(currentIndex + 1, total, targetLabel)
    }
}
