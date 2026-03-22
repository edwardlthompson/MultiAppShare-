import sys

filepath = r"c:\Users\edwar\MultiAppShare-\feature-dashboard\src\main\java\com\multiappshare\ui\DashboardViewModel.kt"

with open(filepath, 'r', encoding='utf-8') as f:
    text = f.read()

package_str = "package com.multiappshare.ui\n"
imports_to_add = """
import com.multiappshare.domain.GroupsRepository
import com.multiappshare.domain.HistoryRepository
import com.multiappshare.domain.SettingsRepository
"""

if package_str in text:
    text = text.replace(package_str, package_str + imports_to_add)
else:
    print("Package string did not match!")
    sys.exit(1)

text = text.replace("MainUiState", "DashboardUiState")

state_definition = """

sealed class DashboardUiState {
    data object Loading : DashboardUiState()
    data class Success(val groups: List<AppGroup>, val allApps: List<AppInfo>, val history: List<HistoryItem>) : DashboardUiState()
}
"""

text = text.rstrip() + state_definition + "\n"

with open(filepath, 'w', encoding='utf-8') as f:
    f.write(text)

print("DashboardViewModel.kt fixed successfully.")
