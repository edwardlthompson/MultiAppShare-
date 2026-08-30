package com.multiappshare.sharedefer

data class ShareDeferResult(
    val packages: List<String>,
    val currentIndex: Int,
)

object ShareDefer {
    fun shouldOffer(lastShareFailed: Boolean, remainingAfterCurrent: Int): Boolean =
        lastShareFailed && remainingAfterCurrent > 0

    fun moveCurrentToEnd(packages: List<String>, currentIndex: Int): ShareDeferResult {
        val remainingAfter = packages.size - currentIndex - 1
        if (currentIndex !in packages.indices || remainingAfter <= 0) {
            return ShareDeferResult(packages, currentIndex)
        }
        val current = packages[currentIndex]
        val next = packages.subList(0, currentIndex) +
            packages.subList(currentIndex + 1, packages.size) +
            current
        return ShareDeferResult(next, currentIndex)
    }
}
