package com.multiappshare.sharepause

object SharePause {
    fun nextAllowed(paused: Boolean): Boolean = !paused
    fun replayAllowed(paused: Boolean): Boolean = !paused
}
