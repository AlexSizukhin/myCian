package com.shokker.mycian

import android.app.Activity
import android.content.pm.ActivityInfo
import android.util.Log
import androidx.test.espresso.Espresso
import androidx.test.espresso.NoActivityResumedException
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.shokker.mycian.UI.MapsActivity

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class SimpleUITests {

    @get: Rule
    val activityRule: ActivityScenarioRule<MapsActivity> = ActivityScenarioRule(MapsActivity::class.java)

    private fun getCurrentActivity(): Activity? {
        var activity: Activity? = null
        activityRule.scenario.onActivity {
            activity = it
        }
        return activity
    }

    @Test
    fun screenRotation()
    {
        Thread.sleep(1000)
        val currentActivity = getCurrentActivity()
        currentActivity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        Thread.sleep(1000)
    }
    @Test
    fun backButtonClicked()
    {
        try {
            Espresso.pressBack()
        }catch (e: NoActivityResumedException) { Log.d("test", "All closed") ; return}
        throw (Exception("test failed. App not closed"))
    }
    @Test
    fun filterFragmentOpensOnEmptySavedSettings()
    {
        TODO()
    }
    @Test
    fun filterFragmentOpensClosesOnClick()
    {
        TODO()
    }
    @Test
    fun flatFragmentOpensOnMapElementClick()
    {
        TODO()
    }
    @Test
    fun flatFragmentHidesOnFilterClick()
    {
        TODO()
    }
    @Test
    fun webViewFragmentOpensOnFlatClick()
    {
        TODO()
    }
}