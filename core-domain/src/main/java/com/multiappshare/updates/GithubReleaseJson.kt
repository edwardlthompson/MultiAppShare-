package com.multiappshare.updates

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

data class GithubRelease(
    val htmlUrl: String,
    val assets: List<ProductUpdate.NamedAsset>,
)

object GithubReleaseJson {
    private val json = Json { ignoreUnknownKeys = true }

    fun parse(text: String): GithubRelease? {
        if (text.isBlank()) return null
        return try {
            val dto = json.decodeFromString<GithubReleaseDto>(text)
            val assets = dto.assets.mapNotNull { asset ->
                val name = asset.name?.trim().orEmpty()
                val url = asset.browserDownloadUrl?.trim().orEmpty()
                if (name.isEmpty() || url.isEmpty()) {
                    null
                } else {
                    ProductUpdate.NamedAsset(name, url)
                }
            }
            GithubRelease(htmlUrl = dto.htmlUrl?.trim().orEmpty(), assets = assets)
        } catch (_: SerializationException) {
            null
        } catch (_: IllegalArgumentException) {
            null
        }
    }

    @Serializable
    private data class GithubReleaseDto(
        @SerialName("html_url") val htmlUrl: String? = null,
        val assets: List<GithubAssetDto> = emptyList(),
    )

    @Serializable
    private data class GithubAssetDto(
        val name: String? = null,
        @SerialName("browser_download_url") val browserDownloadUrl: String? = null,
    )
}
