package com.multiappshare.domain

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NotificationPermissionPolicyTest {

    @Test
    fun neverPromptsOnColdStart() {
        assertFalse(NotificationPermissionPolicy.PROMPT_ON_COLD_START)
    }

    @Test
    fun promptsForShareOnlyWhenDeniedOnApi33Plus() {
        assertTrue(NotificationPermissionPolicy.shouldPromptForShare(33, permissionGranted = false))
        assertFalse(NotificationPermissionPolicy.shouldPromptForShare(33, permissionGranted = true))
        assertFalse(NotificationPermissionPolicy.shouldPromptForShare(32, permissionGranted = false))
    }

    @Test
    fun bannerOnlyDuringShareWhenDenied() {
        assertTrue(
            NotificationPermissionPolicy.shouldShowSettingsBanner(
                sdkInt = 33,
                permissionGranted = false,
                inShareSession = true,
            ),
        )
        assertFalse(
            NotificationPermissionPolicy.shouldShowSettingsBanner(
                sdkInt = 33,
                permissionGranted = false,
                inShareSession = false,
            ),
        )
        assertFalse(
            NotificationPermissionPolicy.shouldShowSettingsBanner(
                sdkInt = 33,
                permissionGranted = true,
                inShareSession = true,
            ),
        )
    }
}
