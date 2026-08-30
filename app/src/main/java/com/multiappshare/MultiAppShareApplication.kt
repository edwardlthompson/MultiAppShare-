package com.multiappshare

import android.app.Application
import android.os.StrictMode
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.multiappshare.crashcapture.CrashCaptureInstaller
import com.multiappshare.crashcapture.CrashStore
import com.multiappshare.domain.SettingsRepository
import com.multiappshare.locale.AppLanguage
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class MultiAppShareApplication : Application(), ImageLoaderFactory {

    @Inject lateinit var settingsRepository: SettingsRepository

    override fun onCreate() {
        super.onCreate()
        CrashCaptureInstaller.install(this)
        if (::settingsRepository.isInitialized) {
            runBlocking {
                AppLanguage.apply(settingsRepository.appLanguage.first())
                CrashStore.writeFlag(this@MultiAppShareApplication, settingsRepository.isCrashCaptureEnabled.first())
            }
        }
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyLog()
                    .build()
            )
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .build()
            )
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .components {
                add(AppIconFetcher.Factory(packageManager))
            }
            .build()
    }
}
