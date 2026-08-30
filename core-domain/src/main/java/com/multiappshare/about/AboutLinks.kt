package com.multiappshare.about

import com.multiappshare.updates.ProductUpdate

object AboutLinks {
    const val CHANGELOG = ProductUpdate.RELEASES_PAGE
    const val TELEGRAM = "https://t.me/EdwardLeeThompson"
    const val VENMO = ProductUpdate.VENMO_URL
    const val GITHUB_SPONSORS = "https://github.com/sponsors/edwardlthompson"
    const val LIBERAPAY = "https://liberapay.com/edwardlthompson"
    const val ISSUES_REPO = "edwardlthompson/MultiAppShare-"

    fun isHttpsUrl(url: String): Boolean =
        url.startsWith("https://") && url.length > "https://".length
}
