package com.shokker.mycian

import android.location.Location
import com.google.android.gms.maps.GoogleMap
import com.shokker.mycian.Model.ClusterMark
import com.shokker.mycian.Model.FilterState
import com.shokker.mycian.Model.ResultFlat
import io.reactivex.Flowable

interface IFlowProvider {
    fun currentLocation():Flowable<Location>
    fun mapLocationBox(map: GoogleMap):Flowable<Pair<Location,Location>>

    fun filter():Flowable<FilterState>
    fun cluster(filter:Flowable<FilterState>,locationBox: Flowable<Pair<Location,Location>>):Flowable<MapResponse>
    fun selectedCluster(map: GoogleMap):Flowable<ClusterMark>
    fun selectedFlats(cluster:Flowable<ClusterMark>): Flowable<ResultFlat>

    fun activityReady():Flowable<MainContract.IMyMapActivity>
    fun activityDestroy():Flowable<MainContract.IMyMapActivity>
}