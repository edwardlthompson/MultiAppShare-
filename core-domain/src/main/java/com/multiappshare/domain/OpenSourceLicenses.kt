package com.multiappshare.domain

data class OpenSourceLicense(
    val name: String,
    val author: String,
    val licenseName: String,
    val url: String,
)

object OpenSourceLicenseCatalog {
    val LICENSES = listOf(
        OpenSourceLicense(
            name = "AndroidX Jetpack",
            author = "Google LLC",
            licenseName = "Apache 2.0",
            url = "https://developer.android.com/jetpack",
        ),
        OpenSourceLicense(
            name = "Kotlin / Coroutines / Serialization",
            author = "JetBrains s.r.o.",
            licenseName = "Apache 2.0",
            url = "https://github.com/JetBrains/kotlin",
        ),
        OpenSourceLicense(
            name = "Coil",
            author = "Coil Contributors",
            licenseName = "Apache 2.0",
            url = "https://github.com/coil-kt/coil",
        ),
        OpenSourceLicense(
            name = "Hilt",
            author = "Google LLC",
            licenseName = "Apache 2.0",
            url = "https://dagger.dev/hilt/",
        ),
        OpenSourceLicense(
            name = "Timber",
            author = "Jake Wharton",
            licenseName = "Apache 2.0",
            url = "https://github.com/JakeWharton/timber",
        ),
    )
}
