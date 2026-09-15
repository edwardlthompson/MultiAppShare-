package com.multiappshare.domain

object FeatureSpecHonesty {
    enum class Layer { DOMAIN, USER_VISIBLE }

    data class Lock(val slug: String, val layer: Layer, val composableFile: String?)

    val REVIEW_LOCKS = listOf(
        Lock("pin-favorite-groups", Layer.USER_VISIBLE, "GroupItemOverflowMenu.kt"),
        Lock("group-notes", Layer.USER_VISIBLE, "RenameGroupDialog.kt"),
        Lock("group-color-label", Layer.USER_VISIBLE, "RenameGroupDialog.kt"),
        Lock("home-widget", Layer.USER_VISIBLE, "HomeShareWidget.kt"),
        Lock("mime-mismatch-sheet", Layer.USER_VISIBLE, "MainScreenShareHelpers.kt"),
        Lock("settings-screen", Layer.USER_VISIBLE, "SettingsScreen.kt"),
        Lock("settings-command-search", Layer.USER_VISIBLE, "SettingsScreen.kt"),
    )

    fun userVisibleHasComposable(lock: Lock): Boolean =
        lock.layer != Layer.USER_VISIBLE || !lock.composableFile.isNullOrBlank()
}
