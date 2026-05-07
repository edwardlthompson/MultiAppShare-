package com.multiappshare

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.multiappshare.ui.theme.MultiAppShareTheme
import org.junit.Rule
import org.junit.Test

class EmptyGroupsPlaceholderPaparazziTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5,
        theme = "android:Theme.Material.Light.NoActionBar",
    )

    @Test
    fun emptyGroupsPlaceholder_light() {
        paparazzi.snapshot {
            MultiAppShareTheme(darkTheme = false, dynamicColor = false) {
                EmptyGroupsPlaceholder()
            }
        }
    }

    @Test
    fun emptyGroupsPlaceholder_dark() {
        paparazzi.snapshot {
            MultiAppShareTheme(darkTheme = true, dynamicColor = false) {
                EmptyGroupsPlaceholder()
            }
        }
    }
}
