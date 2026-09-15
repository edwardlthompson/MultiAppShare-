package com.multiappshare

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.multiappshare.ui.share.SharePayloadSheetContent
import com.multiappshare.ui.theme.MultiAppShareTheme
import org.junit.Rule
import org.junit.Test

class SharePayloadSheetPaparazziTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5,
        theme = "android:Theme.Material.Light.NoActionBar",
    )

    @Test
    fun textSnippet_noAttachments_light() {
        paparazzi.snapshot {
            MultiAppShareTheme(darkTheme = false, dynamicColor = false) {
                val items = remember { mutableStateListOf<Uri>() }
                SharePayloadSheetContent(
                    mime = "text/plain",
                    text = "Hello from the share sheet",
                    items = items,
                    onConfirm = {},
                    onDismiss = {},
                )
            }
        }
    }
}
