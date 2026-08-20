package com.multiappshare.share

import android.content.Intent
import android.net.Uri
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PersistableShareUrisTest {

    @Test
    fun persistableReadFlags_requiresBothFlags() {
        assertNull(PersistableShareUris.persistableReadFlags(0))
        assertNull(PersistableShareUris.persistableReadFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION))
        assertNull(PersistableShareUris.persistableReadFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION))
        assertEquals(
            Intent.FLAG_GRANT_READ_URI_PERMISSION,
            PersistableShareUris.persistableReadFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION,
            ),
        )
    }

    @Test
    fun isContentUri_filtersSchemes() {
        assertTrue(PersistableShareUris.isContentUri(Uri.parse("content://media/1")))
        assertFalse(PersistableShareUris.isContentUri(Uri.parse("https://example.com/a")))
        assertFalse(PersistableShareUris.isContentUri(Uri.parse("file:///tmp/a")))
    }
}
