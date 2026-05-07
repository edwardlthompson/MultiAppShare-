package com.multiappshare.baselineprofile

import android.content.ComponentName
import android.content.Intent
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val APPLICATION_ID = "com.edwardlthompson.multiappshare"
private const val LAUNCHER_ACTIVITY = "com.multiappshare.MainActivity"

/** See `docs/BASELINE_PROFILE.md` — run `./gradlew :app:generateBaselineProfile` (device API 28+). */
@RunWith(AndroidJUnit4::class)
@LargeTest
class BaselineProfileGenerator {

    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun startupAndMainFrame() {
        rule.collect(
            packageName = APPLICATION_ID,
            includeInStartupProfile = true,
        ) {
            pressHome()
            device.waitForIdle()
            val intent = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
                component = ComponentName(APPLICATION_ID, LAUNCHER_ACTIVITY)
            }
            startActivityAndWait(intent)
        }
    }
}
