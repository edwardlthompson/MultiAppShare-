package com.multiappshare.domain

/** One blocking first-run surface at a time. System permission sheets are outside this queue. */
object FirstRunDialogQueue {
    fun allowCrashReview(onboardingVisible: Boolean): Boolean = !onboardingVisible

    fun allowLaunchPrompt(
        onboardingVisible: Boolean,
        inShareMode: Boolean,
        crashReviewVisible: Boolean,
        whatsNewVisible: Boolean = false,
    ): Boolean = !onboardingVisible && !inShareMode && !crashReviewVisible && !whatsNewVisible
}
