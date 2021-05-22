package com.shokker.mycian

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import com.shokker.mycian.DI.FlowProviderImpl
import com.shokker.mycian.DI.FlowProviderModule
import com.shokker.mycian.UI.MapsActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import java.util.concurrent.Callable
import javax.inject.Singleton

@HiltAndroidTest
@UninstallModules(FlowProviderModule::class)        // will be replaced by ...
class FlatResultFragmentTests {
/*
    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    @get: Rule
    val activityRule: ActivityScenarioRule<MapsActivity> = ActivityScenarioRule(MapsActivity::class.java)

    @Before
    fun startUp()
    {

    }
    @After
    fun tearDown()
    {

    }

    private fun getCurrentActivity(): Activity? {
        var activity: Activity? = null
        activityRule.scenario.onActivity {
            activity = it
        }
        return activity
    }

    @Test
    fun test1()
    {
        Thread.sleep(1000)
        val currentActivity = getCurrentActivity()
        currentActivity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        Thread.sleep(1000)
    }

    //var mock = Mockito.mock(IFlowProvider::class.java)
    //var mock = Mockito.mock(FlowProviderImpl::class.java)
    var mock = FlowProviderImpl()
    @Module
    @InstallIn(SingletonComponent::class)
    inner class MockProvider{
        @Singleton
        @Provides
        fun provideFlowProvider():IFlowProvider = mock//   Mockito.mock(IFlowProvider::class.java)
    }*/
}