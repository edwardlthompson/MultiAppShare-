package com.multiappshare

import androidx.lifecycle.ViewModel
import com.multiappshare.domain.ExecuteSequentialShareUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShareFlowViewModel @Inject constructor(
    private val executeSequentialShareUseCase: ExecuteSequentialShareUseCase
) : ViewModel() {
    // Placeholder for fine-grained sharing sequence control if separated from MainViewModel
}
