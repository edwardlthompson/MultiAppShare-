package com.multiappshare.domain

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FirstRunDialogQueueTest {

    @Test
    fun crashWaitsForOnboarding() {
        assertFalse(FirstRunDialogQueue.allowCrashReview(onboardingVisible = true))
        assertTrue(FirstRunDialogQueue.allowCrashReview(onboardingVisible = false))
    }

    @Test
    fun launchPromptNeverDuringOnboardingOrShare() {
        assertFalse(
            FirstRunDialogQueue.allowLaunchPrompt(
                onboardingVisible = true,
                inShareMode = false,
                crashReviewVisible = false,
            ),
        )
        assertFalse(
            FirstRunDialogQueue.allowLaunchPrompt(
                onboardingVisible = false,
                inShareMode = true,
                crashReviewVisible = false,
            ),
        )
        assertFalse(
            FirstRunDialogQueue.allowLaunchPrompt(
                onboardingVisible = false,
                inShareMode = false,
                crashReviewVisible = true,
            ),
        )
        assertTrue(
            FirstRunDialogQueue.allowLaunchPrompt(
                onboardingVisible = false,
                inShareMode = false,
                crashReviewVisible = false,
            ),
        )
        assertFalse(
            FirstRunDialogQueue.allowLaunchPrompt(
                onboardingVisible = false,
                inShareMode = false,
                crashReviewVisible = false,
                whatsNewVisible = true,
            ),
        )
    }
}
