package com.multiappshare

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.multiappshare.ui.onboarding.OnboardingDialog
import com.multiappshare.ui.theme.MultiAppShareTheme
import org.junit.Rule
import org.junit.Test

class OnboardingDialogPaparazziTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5,
        theme = "android:Theme.Material.Light.NoActionBar",
    )

    @Test
    fun onboardingPage1_skipVisible_light() {
        paparazzi.snapshot {
            MultiAppShareTheme(darkTheme = false, dynamicColor = false) {
                OnboardingDialog(onAutofill = {}, onManual = {}, onSkip = {})
            }
        }
    }
}
