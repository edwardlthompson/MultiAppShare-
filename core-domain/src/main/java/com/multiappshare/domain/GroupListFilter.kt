package com.multiappshare.domain

object GroupListFilter {
    fun showClear(query: String): Boolean = query.isNotEmpty()

    fun matches(name: String, query: String): Boolean {
        val q = query.trim()
        return q.isEmpty() || name.contains(q, ignoreCase = true)
    }
}
