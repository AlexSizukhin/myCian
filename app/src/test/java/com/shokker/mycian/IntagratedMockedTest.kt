package com.shokker.mycian

import android.location.Location
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.shokker.mycian.Model.ClusterMark
import com.shokker.mycian.mocksandutils.FlowProviderMock
import com.shokker.mycian.mocksandutils.MockLocation
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.concurrent.Callable


@RunWith(JUnit4::class)
class IntagratedMockedTest {
//. @get:Rule var rule: TestRule = InstantTaskExecutorRule()
    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler: Callable<Scheduler?>? -> Schedulers.trampoline() }
    }

    @After
    fun tearDown(){

    }

    @Test
    fun testThatMockLocationWorks()
    {
        val m = MockLocation()//.apply {  latitude = 55.7558;  longitude= 37.6173}

        val activityMock = ActivityMock()
        val flowProviderMock = FlowProviderMock()

        val x = flowProviderMock.currentLocation(activityMock)
        var cnt = 0
        x.subscribe { println(it.toString())
                    Assert.assertEquals(it, m)
                    cnt++
                    }               // todo test values , test counter,
        Thread.sleep(100)
        Assert.assertTrue(cnt in 9..11)

    }

    @Test
    fun testCameraMovingOnCoordinates()
    {
        val activityMock = ActivityMock()
        val flowProviderMock = FlowProviderMock()
        val reactiveModel = ReactiveModelForTests()

        flowProviderMock.currentLocation(activityMock)
        val x = flowProviderMock.mapLocationBox(activityMock)
        x.subscribe{

        }
        Thread.sleep(100)
    }
}



class ReactiveModelForTests
    : IReactiveModel
{
    private val pCompositeDisposable = CompositeDisposable()
    override val compositeDisposable: CompositeDisposable
        get() = pCompositeDisposable
}
class ActivityMock:MainContract.IMyMapActivity
{
    override fun updateMarks(marks: List<ClusterMark>) {
        TODO("Not yet implemented")
    }

    override fun getSelectedClusterMark(googleMapMarker: Marker): ClusterMark {
        TODO("Not yet implemented")
    }

    override val flatResult: MainContract.IFlatResult
        get() = TODO("Not yet implemented")
    override val flatFilter: MainContract.IFlatFilter
        get() = TODO("Not yet implemented")

    override fun setOnCameraIdle(onCameraIdelFunc: (locationBox: LatLngBounds) -> Unit) {
//        TODO("Not yet implemented")
    }

    override fun setOnMarkClicked(onMarkClickedFun: (ClusterMark) -> Unit) {
        TODO("Not yet implemented")
    }

    var _targetLocation:Location? = null
    override fun moveCamera(targetLocation: Location) {
        println("camera moved to ${targetLocation}")
        _targetLocation = targetLocation
    }

    override fun showError(e: Throwable) {
        TODO("Not yet implemented")
    }

    override fun showFilterDialog(hideIfVisible: Boolean) {
        TODO("Not yet implemented")
    }

}

//////////////////////////////////////////////////////////
// test fixures (mocks)
