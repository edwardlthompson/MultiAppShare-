package com.multiappshare.domain

object GroupNotesHelper {
    const val MAX_NOTE_LENGTH = 140

    fun sanitize(notes: String?): String {
        if (notes.isNullOrBlank()) return ""
        val trimmed = notes.trim()
        return if (trimmed.length > MAX_NOTE_LENGTH) {
            trimmed.substring(0, MAX_NOTE_LENGTH)
        } else {
            trimmed
        }
    }

    fun hasNotes(notes: String?): Boolean = !notes.isNullOrBlank()
}
