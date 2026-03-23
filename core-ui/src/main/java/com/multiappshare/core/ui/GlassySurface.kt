package com.multiappshare.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * A container that uses a blurred, semi-transparent backdrop to achieve a 
 * "Frosted Glass" aesthetic.
 */
@Composable
fun GlassySurface(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        // Blur background scrim
        Box(
            modifier = Modifier
                .matchParentSize()
                .blur(20.dp)
                .background(Color.White.copy(alpha = 0.4f))
        )
        // Foreground Content
        content()
    }
}
