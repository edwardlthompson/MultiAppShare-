package com.multiappshare

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import com.multiappshare.model.AppGroup
import com.multiappshare.model.HistoryItem
import com.multiappshare.share.PersistableShareUris
import com.multiappshare.share.ShareNotificationIntents

internal class MainActivitySharing(
    private val activity: MainActivity,
    private val viewModel: MainViewModel,
    private val steps: MainActivityShareStep,
) {
    fun handleIntent(intent: Intent?, restoreIfColdStart: Boolean) {
        if (intent == null) {
            if (restoreIfColdStart) viewModel.restoreInflightIfFresh()
            return
        }
        if (applyShareCommand(intent)) return
        var consumedShare = false
        when {
            intent.action == Intent.ACTION_SEND || intent.action == Intent.ACTION_SEND_MULTIPLE -> {
                clearSessionShareState()
                applySendIntent(intent)
                consumedShare = true
            }
            intent.action == Intent.ACTION_VIEW && intent.data?.scheme == DeeplinkContract.SCHEME -> {
                applyDeepLink(intent.data!!)
            }
            !intent.getStringExtra("GROUP_NAME").isNullOrBlank() -> {
                viewModel.expandGroupByNameIfPresent(intent.getStringExtra("GROUP_NAME")!!)
                clearSessionShareState()
            }
        }
        if (restoreIfColdStart && !consumedShare) viewModel.restoreInflightIfFresh()
    }

    fun startSharingForGroup(group: AppGroup) {
        val session = viewModel.shareSession
        val mime = session.mimeType ?: "*/*"
        val compatible = steps.compatiblePackages(session.uris, mime, group)
        val contentDesc = steps.contentDescription(mime, session.text, session.uris)
        if (compatible.isEmpty()) {
            viewModel.addHistoryItem(
                HistoryItem(
                    timestamp = System.currentTimeMillis(),
                    groupName = group.name,
                    contentDescription = contentDesc,
                    status = activity.getString(R.string.history_failed_no_compatible),
                    isError = true,
                ),
            )
            Toast.makeText(
                activity,
                activity.getString(R.string.toast_no_apps_for_group, group.name),
                Toast.LENGTH_LONG,
            ).show()
            return
        }
        viewModel.updateShareSession { copy(appPackages = compatible, currentIndex = 0, sharingStarted = true) }
        steps.shareStep(session.uris, session.text, mime, compatible, 0)
        viewModel.incrementGroupUsage(group)
        viewModel.addHistoryItem(
            HistoryItem(
                timestamp = System.currentTimeMillis(),
                groupName = group.name,
                contentDescription = contentDesc,
                status = activity.getString(R.string.history_started_sharing_n, compatible.size),
            ),
        )
    }

    fun replayShareStep() {
        val session = viewModel.shareSession
        val packages = session.appPackages ?: return
        steps.shareStep(session.uris, session.text, session.mimeType ?: "*/*", packages, session.currentIndex)
    }

    fun previousShareStep() {
        val session = viewModel.shareSession
        val packages = session.appPackages ?: return
        if (session.currentIndex > 0) {
            val prev = session.currentIndex - 1
            viewModel.updateShareSession { copy(currentIndex = prev) }
            steps.shareStep(session.uris, session.text, session.mimeType ?: "*/*", packages, prev)
        }
    }

    fun nextShareStep() = advanceAfterCurrent(null)

    fun skipThisApp() {
        val session = viewModel.shareSession
        if (session.sharingStarted && session.appPackages != null) {
            viewModel.addHistoryItem(
                HistoryItem(
                    timestamp = System.currentTimeMillis(),
                    groupName = "",
                    contentDescription = steps.contentDescription(session.mimeType, session.text, session.uris),
                    status = activity.getString(R.string.history_skipped_app),
                ),
            )
        }
        advanceAfterCurrent(null)
    }

    fun finishEarly() {
        val session = viewModel.shareSession
        val packages = session.appPackages
        val remaining = if (packages == null) 0 else (packages.size - session.currentIndex).coerceAtLeast(0)
        if (packages != null && session.sharingStarted) {
            viewModel.addHistoryItem(
                HistoryItem(
                    timestamp = System.currentTimeMillis(),
                    groupName = "",
                    contentDescription = steps.contentDescription(session.mimeType, session.text, session.uris),
                    status = activity.getString(R.string.history_finished_early, remaining),
                ),
            )
        }
        completeSharing(R.string.toast_sharing_finished_early)
    }

    fun cancelShareOverlay() = clearSessionShareState()

    fun onShareFailedAdvance() = advanceAfterCurrent(null)

    private fun applyShareCommand(intent: Intent): Boolean {
        if (!viewModel.shareSession.sharingStarted) return false
        return when (ShareNotificationIntents.commandOf(intent)) {
            ShareNotificationIntents.CMD_NEXT -> {
                nextShareStep()
                true
            }
            ShareNotificationIntents.CMD_SKIP_ONE -> {
                skipThisApp()
                true
            }
            ShareNotificationIntents.CMD_CANCEL -> {
                finishEarly()
                true
            }
            else -> false
        }
    }

    private fun advanceAfterCurrent(@Suppress("UNUSED_PARAMETER") historyRes: Int?) {
        val session = viewModel.shareSession
        val packages = session.appPackages ?: return
        val next = session.currentIndex + 1
        if (next < packages.size) {
            viewModel.updateShareSession { copy(currentIndex = next) }
            steps.shareStep(session.uris, session.text, session.mimeType ?: "*/*", packages, next)
        } else {
            completeSharing(R.string.toast_sharing_complete)
        }
    }

    private fun completeSharing(toastRes: Int) {
        viewModel.finishShareSession()
        steps.stopSharingService()
        Toast.makeText(activity, activity.getString(toastRes), Toast.LENGTH_SHORT).show()
    }

    private fun applySendIntent(intent: Intent) {
        val isMultiple = intent.action == Intent.ACTION_SEND_MULTIPLE
        val uris: List<Uri>? = if (isMultiple) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM, Uri::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM)
            }
        } else {
            val uri: Uri? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra(Intent.EXTRA_STREAM) as? Uri
            }
            if (uri != null) listOf(uri) else null
        }
        PersistableShareUris.take(activity.contentResolver, uris, intent.flags)
        viewModel.updateShareSession {
            copy(
                uris = uris,
                text = intent.getStringExtra(Intent.EXTRA_TEXT),
                mimeType = intent.type ?: "*/*",
                sharingStarted = false,
            )
        }
    }

    private fun applyDeepLink(uri: Uri) {
        clearSessionShareState()
        if (uri.host == DeeplinkContract.HOST_GROUP) {
            val raw = uri.getQueryParameter(DeeplinkContract.QUERY_GROUP_NAME)?.trim().orEmpty()
            if (raw.isNotEmpty()) viewModel.expandGroupByNameIfPresent(raw)
        }
    }

    private fun clearSessionShareState() {
        PersistableShareUris.release(activity.contentResolver, viewModel.shareSession.uris)
        viewModel.clearShareSession()
        steps.stopSharingService()
    }
}
