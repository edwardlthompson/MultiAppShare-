package com.multiappshare.domain

import com.multiappshare.model.AppGroup

object GroupNameHelper {
    fun normalize(name: String): String = name.trim()

    fun isDuplicate(name: String, groups: List<AppGroup>): Boolean {
        val normalized = normalize(name)
        if (normalized.isEmpty()) return false
        return groups.any { it.name.equals(normalized, ignoreCase = true) }
    }

    fun isDuplicateExcluding(name: String, groups: List<AppGroup>, excludeName: String): Boolean {
        val normalized = normalize(name)
        if (normalized.isEmpty()) return false
        return groups.any { group ->
            !group.name.equals(excludeName, ignoreCase = true) &&
                group.name.equals(normalized, ignoreCase = true)
        }
    }

    fun findGroupByName(name: String, groups: List<AppGroup>): AppGroup? {
        val normalized = normalize(name)
        return groups.find { it.name.equals(normalized, ignoreCase = true) }
    }

    fun uniqueCopyName(base: String, groups: List<AppGroup>): String {
        val normalized = normalize(base)
        var candidate = "$normalized (copy)"
        var n = 2
        while (isDuplicate(candidate, groups)) {
            candidate = "$normalized (copy $n)"
            n++
        }
        return candidate
    }
}
