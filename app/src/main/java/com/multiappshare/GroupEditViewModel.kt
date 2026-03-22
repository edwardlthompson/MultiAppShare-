package com.multiappshare

import com.multiappshare.domain.GroupsRepository

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GroupEditViewModel @Inject constructor(
    private val groupsRepository: GroupsRepository
) : ViewModel() {
    // Placeholder for fine-grained group editing logic if separated from DashboardViewModel
}
