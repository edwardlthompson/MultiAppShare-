package com.multiappshare.domain

data class MenuItemSpec(
    val id: String,
    val iconKey: String,
)

enum class OverflowDestination {
    SORT_GROUPS,
    AUTO_GROUP,
    SHARE_CLIPBOARD,
    HISTORY,
    SETTINGS,
}

data class OverflowItemSpec(
    val destination: OverflowDestination,
    val iconKey: String,
)

object OverflowMenuPolicy {
    const val MAX_ITEMS = 5

    val items = listOf(
        OverflowItemSpec(OverflowDestination.SORT_GROUPS, "list"),
        OverflowItemSpec(OverflowDestination.AUTO_GROUP, "auto_awesome"),
        OverflowItemSpec(OverflowDestination.SHARE_CLIPBOARD, "content_paste"),
        OverflowItemSpec(OverflowDestination.HISTORY, "history"),
        OverflowItemSpec(OverflowDestination.SETTINGS, "settings"),
    )

    fun withinLimit(): Boolean = items.size <= MAX_ITEMS

    fun iconKeysAreUnique(): Boolean = items.map { it.iconKey }.toSet().size == items.size

    fun includesDonate(): Boolean =
        items.any { it.iconKey == "favorite" || it.destination.name.contains("DONATE") }
}

object SettingsHubPolicy {
    val items = listOf(
        MenuItemSpec("language", "translate"),
        MenuItemSpec("theme", "palette"),
        MenuItemSpec("sharing_delay", "timer"),
        MenuItemSpec("export", "upload"),
        MenuItemSpec("import", "download"),
        MenuItemSpec("about", "info"),
    )

    fun iconKeysAreUnique(): Boolean = items.map { it.iconKey }.toSet().size == items.size

    fun includesDonate(): Boolean = items.any { it.id == "donate" }
}
