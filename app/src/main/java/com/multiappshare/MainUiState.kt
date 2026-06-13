package com.multiappshare

import com.multiappshare.model.AppGroup
import com.multiappshare.model.AppInfo
import com.multiappshare.model.HistoryItem

sealed class MainUiState {
    data object Loading : MainUiState()
    data class Success(
        val groups: List<AppGroup>,
        val allApps: List<AppInfo>,
        val history: List<HistoryItem>,
    ) : MainUiState()
}
