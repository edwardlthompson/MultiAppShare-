package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class NotificationChannelLinkTest {

    @Test
    fun buildsNotificationChannelIntentData() {
        val dataWithChannel = NotificationChannelLink.buildChannelSettingsIntentData(
            packageName = "com.multiappshare",
            channelId = "sharing_service_channel_v2",
        )
        assertEquals("com.multiappshare", dataWithChannel[NotificationChannelLink.EXTRA_APP_PACKAGE])
        assertEquals("sharing_service_channel_v2", dataWithChannel[NotificationChannelLink.EXTRA_CHANNEL_ID])

        val dataWithoutChannel = NotificationChannelLink.buildChannelSettingsIntentData(
            packageName = "com.multiappshare",
            channelId = null,
        )
        assertEquals("com.multiappshare", dataWithoutChannel[NotificationChannelLink.EXTRA_APP_PACKAGE])
        assertEquals(null, dataWithoutChannel[NotificationChannelLink.EXTRA_CHANNEL_ID])
    }
}
