package com.multiappshare.core.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun ShareSuccessAnimation(
    modifier: Modifier = Modifier,
    skipBurst: Boolean = false,
    onAnimationEnd: () -> Unit = {},
) {
    val scale = remember { Animatable(0f) }
    val checkmarkProgress = remember { Animatable(0f) }
    val fill = MaterialTheme.colorScheme.primary
    val check = MaterialTheme.colorScheme.onPrimary

    LaunchedEffect(skipBurst) {
        if (skipBurst) {
            onAnimationEnd()
            return@LaunchedEffect
        }
        scale.animateTo(1f, tween(150, easing = FastOutSlowInEasing))
        checkmarkProgress.animateTo(1f, tween(100, easing = LinearOutSlowInEasing))
        delay(50)
        onAnimationEnd()
    }

    if (skipBurst) return

    Canvas(modifier = modifier.size(48.dp)) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = (size.minDimension / 2) * scale.value
        drawCircle(color = fill, radius = radius, center = center)
        if (checkmarkProgress.value > 0f) {
            val path = Path().apply {
                val startX = center.x - radius * 0.4f
                val startY = center.y
                val midX = center.x - radius * 0.1f
                val midY = center.y + radius * 0.3f
                val endX = center.x + radius * 0.4f
                val endY = center.y - radius * 0.3f
                moveTo(startX, startY)
                if (checkmarkProgress.value <= 0.4f) {
                    val p = checkmarkProgress.value / 0.4f
                    lineTo(startX + (midX - startX) * p, startY + (midY - startY) * p)
                } else {
                    lineTo(midX, midY)
                    val p = (checkmarkProgress.value - 0.4f) / 0.6f
                    lineTo(midX + (endX - midX) * p, midY + (endY - midY) * p)
                }
            }
            drawPath(
                path = path,
                color = check,
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round),
            )
        }
    }
}
