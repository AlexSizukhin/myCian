package com.shokker.mycian

import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.shokker.mycian.Model.ClusterMark
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.junit.After
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
        val activityMock = ActivityMock()
        val flowProviderMock = FlowProviderMock()
        val reactiveModel = ReactiveModelForTests()
        //reactiveModel.createAllFlows(flowProviderMock, activityMock)
        val x = flowProviderMock.currentLocation(activityMock)
        x.subscribe { println(it.toString())}               // todo test values , test counter,
        Thread.sleep(100)

    }

    @Test
    fun testCameraMovingOnCoordinates()
    {
        val activityMock = ActivityMock()
        val flowProviderMock = FlowProviderMock()
        val reactiveModel = ReactiveModelForTests()
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
        TODO("Not yet implemented")
    }

    override fun setOnMarkClicked(onMarkClickedFun: (ClusterMark) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun moveCamera(targetLocation: Location) {
        println("camera moved to ${targetLocation}")
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
