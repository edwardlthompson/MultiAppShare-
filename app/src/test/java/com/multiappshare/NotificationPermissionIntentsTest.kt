package com.multiappshare

import com.multiappshare.domain.NotificationChannelLink
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class NotificationPermissionIntentsTest {

    @Test
    fun settingsIntentUsesChannelLinkExtras() {
        val context = RuntimeEnvironment.getApplication()
        val intent = NotificationPermissionIntents.appSettings(context)
        assertEquals(NotificationChannelLink.ACTION_APP_NOTIFICATION_SETTINGS, intent.action)
        assertEquals(
            context.packageName,
            intent.getStringExtra(NotificationChannelLink.EXTRA_APP_PACKAGE),
        )
        assertEquals(
            SharingNotification.CHANNEL_ID,
            intent.getStringExtra(NotificationChannelLink.EXTRA_CHANNEL_ID),
        )
    }
}
