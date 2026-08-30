package com.multiappshare

/**
 * FOSS-safe custom URI scheme for opening the app without hosting `assetlinks.json`.
 *
 * Examples:
 * - [SCHEME]://[HOST_OPEN] — normal launcher-style entry (clears in-session share payload).
 * - [SCHEME]://[HOST_GROUP]?[QUERY_GROUP_ID]=uuid&[QUERY_GROUP_NAME]=My%20Group
 */
object DeeplinkContract {
    const val SCHEME = "multiappshare"
    const val HOST_OPEN = "open"
    const val HOST_GROUP = "group"
    const val HOST_SHARE_STEP = "share_step"
    const val QUERY_GROUP_NAME = "name"
    const val QUERY_GROUP_ID = "id"
    const val QUERY_STEP_INDEX = "index"
}
