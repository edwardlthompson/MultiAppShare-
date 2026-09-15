package com.multiappshare.domain

object InlineFieldError {
    fun of(success: Boolean, message: String): String? = if (success) null else message
}
