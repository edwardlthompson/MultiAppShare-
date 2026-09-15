package com.multiappshare.domain

import org.junit.Assert.assertTrue
import org.junit.Test

class HomeSaveableStateTest {

    @Test
    fun filterAndDialogFlagsSurviveRecreation() {
        assertTrue(HomeSaveableState.FILTER_QUERY)
        assertTrue(HomeSaveableState.DIALOG_FLAGS)
    }
}
