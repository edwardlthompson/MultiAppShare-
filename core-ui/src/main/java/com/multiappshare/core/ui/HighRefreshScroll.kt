package com.multiappshare.core.ui

import androidx.compose.ui.FrameRateCategory
import androidx.compose.ui.Modifier
import androidx.compose.ui.preferredFrameRate

/**
 * Marks a scroll surface for high refresh so adaptive panels can ramp during flings.
 */
fun Modifier.highRefreshScroll(): Modifier =
    this.then(Modifier.preferredFrameRate(FrameRateCategory.High))
