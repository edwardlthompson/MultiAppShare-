package com.multiappshare.domain

object DynamicColorPolicy {
    const val ANDROID_12_SDK = 31
    const val DEFAULT_ENABLED = true

    fun useWallpaperColors(sdkInt: Int, userEnabled: Boolean): Boolean =
        userEnabled && sdkInt >= ANDROID_12_SDK
}
