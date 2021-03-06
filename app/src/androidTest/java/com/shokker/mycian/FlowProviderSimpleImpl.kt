package com.shokker.mycian

import android.location.Location
import android.util.Log
import com.google.android.gms.maps.GoogleMap
import com.shokker.mycian.DI.FlowProviderImpl
import com.shokker.mycian.DI.FlowProviderModule
import com.shokker.mycian.Model.ClusterMark
import com.shokker.mycian.Model.FilterState
import com.shokker.mycian.Model.ResultFlat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import io.reactivex.Flowable
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FlowForTestImplModule{
    @Singleton
    @Provides
    fun provideFlowProvider():IFlowProvider = FlowProviderSimpleImpl()
}

class FlowProviderSimpleImpl: FlowProviderImpl() {
    init {
        Log.d("T","SimpleFlowProvider created")
    }
    override fun currentLocation(): Flowable<Location> {
        return Flowable.just(Location("37.622504,55.7532"))
    }

    override fun mapLocationBox(map: GoogleMap): Flowable<Pair<Location, Location>> {
        return Flowable.just(Pair(Location("37.0,55.7532"),Location("37.622504,55.0")))
    }

    override fun filter(): Flowable<FilterState> {
        return Flowable.just(FilterState())
    }

    override fun cluster(
        filter: Flowable<FilterState>,
        locationBox: Flowable<Pair<Location, Location>>
    ): Flowable<MapResponse> {
        TODO()
    }

    override fun selectedCluster(map: GoogleMap): Flowable<ClusterMark> {
        return Flowable.just(ClusterMark())
    }

    override fun selectedFlats(cluster: Flowable<ClusterMark>): Flowable<ResultFlat> {
        return Flowable.just(ResultFlat())
    }

    override fun activityReady(): Flowable<MainContract.IMyMapActivity> {
        return Flowable.just(this.activity)
    }

    override fun activityDestroy(): Flowable<MainContract.IMyMapActivity> {
        TODO("Not yet implemented")
    }
}