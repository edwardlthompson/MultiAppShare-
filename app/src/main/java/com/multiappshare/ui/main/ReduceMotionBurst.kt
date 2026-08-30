package com.multiappshare.ui.main

import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.multiappshare.reducemotion.ReduceMotion

@Composable
internal fun rememberSkipSuccessBurst(): Boolean {
    val resolver = LocalContext.current.contentResolver
    return remember {
        ReduceMotion.skipBurst(
            Settings.Global.getFloat(resolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f),
            Settings.Global.getFloat(resolver, Settings.Global.TRANSITION_ANIMATION_SCALE, 1f),
        )
    }
}
