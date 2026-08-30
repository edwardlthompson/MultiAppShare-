package com.multiappshare.domain

enum class WindowSizeClass {
    COMPACT,
    MEDIUM,
    EXPANDED,
}

object DesktopWindowingHelper {
    fun classifyWindowSize(widthDp: Int): WindowSizeClass {
        return when {
            widthDp < 600 -> WindowSizeClass.COMPACT
            widthDp < 840 -> WindowSizeClass.MEDIUM
            else -> WindowSizeClass.EXPANDED
        }
    }

    fun overlayMaxContentWidthDp(sizeClass: WindowSizeClass): Int {
        return when (sizeClass) {
            WindowSizeClass.COMPACT -> 400
            WindowSizeClass.MEDIUM -> 560
            WindowSizeClass.EXPANDED -> 720
        }
    }
}
