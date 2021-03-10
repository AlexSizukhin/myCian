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
/*
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



    override fun filter(): Flowable<FilterState> {
        TODO()
    }



    override fun selectedCluster(map: GoogleMap): Flowable<ClusterMark> {
        TODO()
    }

    override fun selectedFlats(cluster: Flowable<ClusterMark>): Flowable<ResultFlat> {
        return Flowable.just(ResultFlat())
    }


}*/