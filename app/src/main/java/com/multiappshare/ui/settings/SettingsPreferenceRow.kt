package com.multiappshare.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.BatterySaver
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import com.multiappshare.ui.theme.Dimens

@Composable
internal fun SettingsPreferenceRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    checked: Boolean? = null,
    onCheckedChange: ((Boolean) -> Unit)? = null,
) {
    val isToggle = checked != null && onCheckedChange != null
    val rowModifier = if (isToggle) {
        Modifier
            .defaultMinSize(minHeight = Dimens.TouchTarget)
            .toggleable(
                value = checked,
                onValueChange = onCheckedChange,
                role = Role.Switch,
            )
    } else {
        Modifier
            .defaultMinSize(minHeight = Dimens.TouchTarget)
            .clickable(onClick = onClick)
    }
    ListItem(
        headlineContent = {
            Text(title, style = MaterialTheme.typography.bodyLarge)
        },
        supportingContent = {
            Text(
                subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        leadingContent = {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.defaultMinSize(minWidth = Dimens.Icon, minHeight = Dimens.Icon),
            )
        },
        trailingContent = {
            if (isToggle) {
                Switch(checked = checked, onCheckedChange = null)
            } else {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        modifier = rowModifier,
        colors = ListItemDefaults.colors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceContainerLowest
            },
        ),
    )
}

internal fun settingsEntryIcon(id: String): ImageVector =
    SETTINGS_ENTRY_ICONS[id] ?: Icons.Filled.Info

private val SETTINGS_ENTRY_ICONS: Map<String, ImageVector> = mapOf(
    "theme" to Icons.Filled.Brightness6,
    "dynamic_color" to Icons.Filled.Palette,
    "language" to Icons.Filled.Language,
    "refresh" to Icons.Filled.Speed,
    "delay" to Icons.Filled.Timer,
    "haptics" to Icons.Filled.Vibration,
    "battery" to Icons.Filled.BatterySaver,
    "backup" to Icons.Filled.Backup,
    "history" to Icons.Filled.History,
    "about" to Icons.Filled.Info,
    "privacy" to Icons.Filled.PrivacyTip,
    "licenses" to Icons.Filled.Description,
    "whats_new" to Icons.Filled.NewReleases,
    "feedback" to Icons.Filled.Feedback,
    "crash" to Icons.Filled.BugReport,
)
