package com.shokker.mycian

import android.util.Log
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.shokker.mycian.DI.FlowProviderModule
import com.shokker.mycian.UI.MapsActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject


@HiltAndroidTest
@UninstallModules(FlowProviderModule::class)
//@RunWith(AndroidJUnit4::class)
//@LargeTest
class TestSkeleton {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    @get: Rule
    val activityRule: ActivityScenarioRule<MapsActivity> = ActivityScenarioRule(MapsActivity::class.java)

    @Inject
    lateinit var flowProviderSimpleImpl: IFlowProvider

    @Before
    fun prepare()
    {
        hiltRule.inject()
    //    Log.d("Test", "On @Before injected ${fakeSettings}")
    //    Log.d("Test", "On @Before injected ${fakeGenerator}")
    }

    @Test
    fun test1()
    {
        Thread.sleep(10000)
    }
}