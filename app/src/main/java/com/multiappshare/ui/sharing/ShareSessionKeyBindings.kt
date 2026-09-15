package com.multiappshare.ui.sharing

import androidx.compose.foundation.focusable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import com.multiappshare.domain.ShareSessionKeyAction
import com.multiappshare.domain.ShareSessionKeys

@Composable
internal fun shareSessionKeyModifier(
    paused: Boolean,
    onNext: () -> Unit,
    onCancel: () -> Unit,
): Modifier {
    val requester = remember { FocusRequester() }
    LaunchedEffect(Unit) { requester.requestFocus() }
    return Modifier
        .focusRequester(requester)
        .focusable()
        .onKeyEvent { event ->
            if (event.type != KeyEventType.KeyUp) return@onKeyEvent false
            when (ShareSessionKeys.action(shareSessionKeyToken(event.key), paused)) {
                ShareSessionKeyAction.NEXT -> {
                    onNext()
                    true
                }
                ShareSessionKeyAction.CANCEL -> {
                    onCancel()
                    true
                }
                ShareSessionKeyAction.IGNORE -> false
            }
        }
}

internal fun shareSessionKeyToken(key: Key): String = when (key) {
    Key.Enter, Key.NumPadEnter -> "Enter"
    Key.Escape -> "Escape"
    Key.Back -> "Back"
    else -> "Other"
}
