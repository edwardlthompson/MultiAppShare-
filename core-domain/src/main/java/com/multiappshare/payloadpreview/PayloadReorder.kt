package com.multiappshare.payloadpreview

object PayloadReorder {
    fun shouldOffer(uriCount: Int): Boolean = uriCount > 1

    fun <T> move(items: List<T>, from: Int, to: Int): List<T> {
        if (from !in items.indices || to !in items.indices || from == to) return items
        val next = items.toMutableList()
        val item = next.removeAt(from)
        next.add(to, item)
        return next
    }
}
