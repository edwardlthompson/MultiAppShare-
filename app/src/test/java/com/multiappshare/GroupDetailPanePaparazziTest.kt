package com.multiappshare

import android.content.pm.PackageManager
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.multiappshare.model.AppGroup
import com.multiappshare.model.AppInfo
import com.multiappshare.ui.groups.GroupDetailPane
import com.multiappshare.ui.theme.MultiAppShareTheme
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class GroupDetailPanePaparazziTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5,
        theme = "android:Theme.Material.Light.NoActionBar",
    )

    private val packageManager: PackageManager = mockk(relaxed = true)

    @Test
    fun groupDetailPane_withApps_light() {
        paparazzi.snapshot {
            MultiAppShareTheme(darkTheme = false, dynamicColor = false) {
                GroupDetailPane(
                    group = AppGroup(
                        name = "Social",
                        apps = listOf(
                            AppInfo(appName = "Telegram", packageName = "org.telegram.messenger"),
                            AppInfo(appName = "Signal", packageName = "org.thoughtcrime.securesms"),
                        ),
                    ),
                    lastSharedMillis = null,
                    onModifyClick = {},
                    onDeleteClick = {},
                    packageManager = packageManager,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }

    @Test
    fun groupDetailPane_unselected_light() {
        paparazzi.snapshot {
            MultiAppShareTheme(darkTheme = false, dynamicColor = false) {
                GroupDetailPane(
                    group = null,
                    lastSharedMillis = null,
                    onModifyClick = {},
                    onDeleteClick = {},
                    packageManager = packageManager,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}
