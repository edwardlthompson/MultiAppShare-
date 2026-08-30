package com.multiappshare.githubfeedback

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class IssueFormUrlTest {

    @Test
    fun buildsHttpsIssueForm() {
        val built = IssueFormUrl.build("edwardlthompson/MultiAppShare-", "Feedback", "hello")
        assertTrue(built.url.startsWith("https://github.com/edwardlthompson/MultiAppShare-/issues/new"))
        assertTrue(built.url.contains("title="))
        assertFalse(built.bodyTooLarge)
    }

    @Test
    fun placeholderRepoYieldsEmpty() {
        assertTrue(IssueFormUrl.build("OWNER/REPO", "t", "b").url.isEmpty())
        assertTrue(IssueFormUrl.isPlaceholderRepo(""))
    }

    @Test
    fun dropsBodyWhenQueryExceedsCap() {
        val huge = "x".repeat(4000)
        val built = IssueFormUrl.build("edwardlthompson/MultiAppShare-", "Feedback", huge)
        assertTrue(built.bodyTooLarge)
        assertTrue(built.url.contains("title="))
        assertFalse(built.url.contains("body="))
        assertTrue(built.url.length <= IssueFormUrl.MAX_QUERY_CHARS)
    }
}
