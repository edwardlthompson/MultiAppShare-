package com.multiappshare

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import com.multiappshare.core.ui.ExternalIntent
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExternalIntentTest {

    @Test
    fun tryViewUrl_returnsFalseWhenNoHandler() {
        val context = mockk<Context>(relaxed = false)
        every { context.startActivity(any()) } throws ActivityNotFoundException()
        assertFalse(ExternalIntent.tryViewUrl(context, "https://example.com"))
    }

    @Test
    fun tryViewUrl_startsViewIntent() {
        val context = mockk<Context>(relaxed = true)
        every { context.startActivity(any()) } returns Unit
        assertTrue(ExternalIntent.tryViewUrl(context, "https://example.com"))
        verify {
            context.startActivity(
                match { intent ->
                    intent.action == Intent.ACTION_VIEW &&
                        intent.dataString == "https://example.com"
                },
            )
        }
    }
}
