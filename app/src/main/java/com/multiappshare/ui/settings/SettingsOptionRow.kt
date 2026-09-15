package com.multiappshare.ui.settings

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import com.multiappshare.ui.theme.Dimens

fun Modifier.settingsOptionRow(): Modifier =
    fillMaxWidth().defaultMinSize(minHeight = Dimens.TouchTarget)
