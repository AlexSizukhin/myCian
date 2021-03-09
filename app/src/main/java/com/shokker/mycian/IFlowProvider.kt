package com.shokker.mycian

import android.app.Activity
import android.location.Location
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.shokker.mycian.Model.ClusterMark
import com.shokker.mycian.Model.FilterState
import com.shokker.mycian.Model.LocalStorage
import com.shokker.mycian.Model.ResultFlat
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable


interface IFlowProvider {
    fun currentLocation():Flowable<Location>
    fun mapLocationBox(map: GoogleMap):Flowable<LatLngBounds>

    fun filter():Flowable<FilterState>
    fun cluster(filter:Flowable<FilterState>,locationBox: Flowable<Pair<CianCoords,CianCoords>>):Flowable<MapResponse>
    fun selectedCluster(map: GoogleMap):Flowable<ClusterMark>
    fun selectedFlats(cluster:Flowable<ClusterMark>): Flowable<ResultFlat>
    fun saveToLocalStorageFlow(filterState: Flowable<FilterState>):Disposable
    fun loadFromLocalStorage():Flowable<FilterState>

    var currectActivity : Activity

}