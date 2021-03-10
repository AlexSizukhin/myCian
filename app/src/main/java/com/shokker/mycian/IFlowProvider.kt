package com.shokker.mycian

import android.location.Location
import com.google.android.gms.maps.model.LatLngBounds
import com.shokker.mycian.Model.ClusterMark
import com.shokker.mycian.Model.FilterState
import com.shokker.mycian.Model.LocalStorage
import com.shokker.mycian.Model.ResultFlat
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable


interface IFlowProvider {
    fun currentLocation(currentActivity: MainContract.IMyMapActivity):Flowable<Location>
    fun mapLocationBox(currentActivity: MainContract.IMyMapActivity):Flowable<LatLngBounds>

    fun filter(flatFilter : MainContract.IFlatFilter):Flowable<FilterState>
    fun cluster(filter:Flowable<FilterState>,locationBox: Flowable<Pair<CianCoords,CianCoords>>):Flowable<MapResponse>
    fun selectedCluster(mapActivity: MainContract.IMyMapActivity):Flowable<ClusterMark>
    fun selectedFlats(mapActivity: MainContract.IMyMapActivity,cluster:Flowable<ClusterMark>): Flowable<ResultFlat>

    fun toLocalStorage(filterState: Flowable<FilterState>):Disposable
    fun fromLocalStorage():Flowable<FilterState>


}