package com.shokker.mycian

import android.location.Location
import com.google.android.gms.maps.GoogleMap
import com.shokker.mycian.Model.FilterState
import com.shokker.mycian.Model.ResultFlat
import com.shokker.mycian.UI.MapsActivity
import io.reactivex.Flowable
import io.reactivex.Single

interface IFlowProvider {
    fun currentLocation():Flowable<Location>
    fun mapLocationBox(map: GoogleMap):Flowable<Pair<Location,Location>>

    fun filter():Flowable<FilterState>
    fun cluster(filter:Flowable<FilterState>,locationBox: Flowable<Pair<Location,Location>>):Flowable<GetClusterReq>
    fun selectedCluster(map: GoogleMap):Flowable<GetClusterReq>
    fun selectedFlats(cluster:Flowable<GetClusterReq>): Flowable<ResultFlat>

    fun activityReady():Flowable<MainContract.MyMapActivity>
}