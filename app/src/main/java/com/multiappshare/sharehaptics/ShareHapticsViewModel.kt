package com.multiappshare.sharehaptics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multiappshare.domain.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShareHapticsViewModel @Inject constructor(
    private val settings: SettingsRepository,
) : ViewModel() {
    val enabled = settings.isShareHapticsEnabled

    fun setEnabled(value: Boolean) {
        viewModelScope.launch { settings.setShareHapticsEnabled(value) }
    }
}
