package com.shokker.mycian

import android.location.Location
import com.shokker.mycian.DI.FlowProviderImpl
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class FlowProviderMock:FlowProviderImpl() {
    override fun currentLocation(currentActivity: MainContract.IMyMapActivity): Flowable<Location> {
        return Flowable.interval (10,TimeUnit.MILLISECONDS).map {
            val l = MockLocation()
            l.latitude = 55.7558
            l.longitude= 37.6173
            return@map l
        }
        //flatMap { Flowable.create({ Location("55.7558,37.6173")},BackpressureStrategy.LATEST) }
    }
}