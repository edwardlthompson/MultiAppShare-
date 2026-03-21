package com.multiappshare.domain

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import com.multiappshare.SharingService
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.ArrayList
import javax.inject.Inject

class ExecuteSequentialShareUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke(uris: List<Uri>?, text: String?, mime: String, components: List<String>, index: Int) {
        val serviceIntent = Intent(context, SharingService::class.java).apply {
            action = SharingService.ACTION_START_SHARING
            type = mime
            if (uris != null) putParcelableArrayListExtra(SharingService.EXTRA_IMAGE_URIS, ArrayList(uris))
            putExtra(Intent.EXTRA_TEXT, text)
            putStringArrayListExtra(SharingService.EXTRA_APP_COMPONENTS, ArrayList(components))
            putExtra(SharingService.EXTRA_CURRENT_INDEX, index)
            if (uris != null) {
                for (uri in uris) {
                    val clipDataItem = ClipData.Item(uri)
                    if (clipData == null) {
                        clipData = ClipData(null, arrayOf(mime), clipDataItem)
                    } else {
                        clipData?.addItem(clipDataItem)
                    }
                }
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }
        ContextCompat.startForegroundService(context, serviceIntent)
    }
}
