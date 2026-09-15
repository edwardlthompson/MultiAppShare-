package com.multiappshare.domain

import com.multiappshare.model.AppGroup

object DeepLinkGroupLookup {
    fun existingName(groups: List<AppGroup>, id: String?, name: String?): String? {
        val trimmedId = id?.trim().orEmpty()
        val trimmedName = name?.let { GroupNameHelper.normalize(it) }.orEmpty()
        return groups.find { group ->
            trimmedId.isNotEmpty() && group.id == trimmedId ||
                trimmedId.isEmpty() && trimmedName.isNotEmpty() &&
                group.name.equals(trimmedName, ignoreCase = true)
        }?.name
    }
}
