package com.multiappshare.domain

import com.multiappshare.model.AppGroup
import java.util.UUID

object GroupIds {
    fun newId(): String = UUID.randomUUID().toString()

    fun ensure(group: AppGroup): AppGroup =
        if (group.id.isBlank()) group.copy(id = newId()) else group
}
