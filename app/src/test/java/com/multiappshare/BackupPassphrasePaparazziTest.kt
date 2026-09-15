package com.multiappshare

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.multiappshare.ui.theme.MultiAppShareTheme
import org.junit.Rule
import org.junit.Test

class BackupPassphrasePaparazziTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5,
        theme = "android:Theme.Material.Light.NoActionBar",
    )

    @Test
    fun exportFields_weakAndMismatch_light() {
        paparazzi.snapshot {
            MultiAppShareTheme(darkTheme = false, dynamicColor = false) {
                BackupExportPassphraseFields(
                    pass = "aaaaaaaaaa",
                    onPassChange = {},
                    confirm = "bbbbbbbbbb",
                    onConfirmChange = {},
                )
            }
        }
    }
}
