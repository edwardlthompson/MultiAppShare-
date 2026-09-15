package com.multiappshare.domain

object PinShortcutPolicy {
    fun shouldExplainUnsupported(pinSupported: Boolean): Boolean = !pinSupported
}
