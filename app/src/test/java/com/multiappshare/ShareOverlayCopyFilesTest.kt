package com.multiappshare

import com.multiappshare.domain.AppLanguageTags
import com.multiappshare.domain.ShareOverlayCopy
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class ShareOverlayCopyFilesTest {

    @Test
    fun overlaySubtitlesStayOnThisShare() {
        AppLanguageTags.pickerOrder.forEach { tag ->
            val folder = if (tag == "en") "values" else "values-$tag"
            val xml = File("src/main/res/$folder/strings.xml").readText()
            val match = Regex("""<string name="share_overlay_subtitle">([^<]+)</string>""").find(xml)
            val text = match?.groupValues?.get(1).orEmpty()
            assertTrue("$tag missing subtitle", text.isNotBlank())
            assertTrue("$tag $text", ShareOverlayCopy.isSessionScoped(text.replace("\\'", "'")))
        }
    }
}
