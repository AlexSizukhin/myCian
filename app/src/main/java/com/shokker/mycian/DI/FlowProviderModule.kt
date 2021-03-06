package com.shokker.mycian.DI

import android.app.Application
import android.location.Location
import com.google.android.gms.maps.GoogleMap
import com.shokker.mycian.IFlowProvider
import com.shokker.mycian.MainContract
import com.shokker.mycian.MapResponse
import com.shokker.mycian.Model.ClusterMark
import com.shokker.mycian.Model.FilterState
import com.shokker.mycian.Model.ResultFlat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.Flowable


@InstallIn(SingletonComponent::class)
@Module
class FlowProviderModule
{
    @Provides
    fun provideFlowProvider():IFlowProvider
    {
        return FlowProviderImpl()
    }
}

open class FlowProviderImpl:IFlowProvider
{
    override fun currentLocation(): Flowable<Location> {
        TODO("Not yet implemented")
    }

    override fun mapLocationBox(map: GoogleMap): Flowable<Pair<Location, Location>> {
        TODO("Not yet implemented")
    }

    override fun filter(): Flowable<FilterState> {
        TODO("Not yet implemented")
    }

    override fun cluster(filter: Flowable<FilterState>, locationBox: Flowable<Pair<Location, Location>>): Flowable<MapResponse> {
        TODO("Not yet implemented")
    }

    override fun selectedCluster(map: GoogleMap): Flowable<ClusterMark> {
        TODO("Not yet implemented")
    }

    override fun selectedFlats(cluster: Flowable<ClusterMark>): Flowable<ResultFlat> {
        TODO("Not yet implemented")
    }

    override fun activityReady(): Flowable<MainContract.IMyMapActivity> {
        TODO("Not yet implemented")
    }

    override fun activityDestroy(): Flowable<MainContract.IMyMapActivity> {
        TODO("Not yet implemented")

    }

    protected var activity: MainContract.IMyMapActivity? = null
}