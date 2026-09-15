package com.multiappshare

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.multiappshare.ui.groups.CreateGroupDialog
import com.multiappshare.ui.theme.MultiAppShareTheme
import org.junit.Rule
import org.junit.Test

class CreateGroupDialogPaparazziTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5,
        theme = "android:Theme.Material.Light.NoActionBar",
    )

    @Test
    fun createGroup_blankNameDisablesPrimary_light() {
        paparazzi.snapshot {
            MultiAppShareTheme(darkTheme = false, dynamicColor = false) {
                CreateGroupDialog(onDismiss = {}, onCreateGroup = {})
            }
        }
    }
}
