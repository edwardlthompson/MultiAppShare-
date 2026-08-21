package com.multiappshare

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.multiappshare.domain.HistoryPayload
import com.multiappshare.domain.ShareSessionSnapshot
import com.multiappshare.model.HistoryItem
import com.multiappshare.ui.dashboard.DashboardHistoryDialog
import com.multiappshare.ui.dashboard.HistoryDialogLabels
import com.multiappshare.ui.main.GroupFilterField
import com.multiappshare.ui.theme.MultiAppShareTheme
import org.junit.Rule
import org.junit.Test

class MilestoneAbPaparazziTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5,
        theme = "android:Theme.Material.Light.NoActionBar",
    )

    @Test
    fun groupFilterField_light() {
        paparazzi.snapshot {
            MultiAppShareTheme(darkTheme = false, dynamicColor = false) {
                GroupFilterField(query = "So", onChange = {}, contentDescription = "Filter groups")
            }
        }
    }

    @Test
    fun historyDialog_rowReshare_light() {
        val payload = HistoryPayload.encode(ShareSessionSnapshot(text = "hello", mimeType = "text/plain"))
        paparazzi.snapshot {
            MultiAppShareTheme(darkTheme = false, dynamicColor = false) {
                DashboardHistoryDialog(
                    history = listOf(
                        HistoryItem(
                            id = 1,
                            timestamp = 1_700_000_000_000L,
                            groupName = "Social",
                            contentDescription = "Text",
                            status = "Started sharing to 2 apps",
                            payloadJson = payload,
                        ),
                    ),
                    labels = HistoryDialogLabels(
                        title = "History",
                        empty = "Empty",
                        sharedPrefix = "Shared: %1\$s",
                        close = "Close",
                        reshare = "Re-share last",
                        reshareRow = "Re-share",
                    ),
                    onDismiss = {},
                    onReshareItem = {},
                )
            }
        }
    }
}
