package com.multiappshare

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.multiappshare.ui.groups.CreateGroupDialog
import com.multiappshare.ui.theme.MultiAppShareTheme
import org.junit.Rule
import org.junit.Test

/** Snapshot coverage for stable share/group entry surfaces (Milestone R.2.8). */
class ShareOverlayAccessibilityTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5,
        theme = "android:Theme.Material.Light.NoActionBar",
    )

    @Test
    fun createGroupDialog_light() {
        paparazzi.snapshot {
            MultiAppShareTheme(darkTheme = false, dynamicColor = false) {
                CreateGroupDialog(onDismiss = {}, onCreateGroup = {})
            }
        }
    }

    @Test
    fun emptyGroupsPlaceholder_createAction_light() {
        paparazzi.snapshot {
            MultiAppShareTheme(darkTheme = false, dynamicColor = false) {
                EmptyGroupsPlaceholder(onAddGroup = {}, onAutoGroup = {})
            }
        }
    }
}
