package com.shokker.mycian

import android.location.Location
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.scopes.ActivityScoped
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Singleton

@ActivityScoped
//class God
interface God
    //@Inject constructor(flowProvider: IFlowProvider)
{             // Presenter or Flow dispatcher
    // do magic! create the world
    fun createAllFlows(flowProvider: IFlowProvider, mainActivity: MainContract.IMyMapActivity)
    {
        Log.d("GOD","God object started initialisation")
        flowProvider.apply {
            val currentLocation = flowProvider.currentLocation()
            compositeDisposable.add( currentLocation.map(::mapCooridnates).subscribe({
                if (it != null)
                    mainActivity.googleMap.moveCamera(CameraUpdateFactory.newLatLng(it))
            }))
            val locationBox = flowProvider.mapLocationBox(mainActivity.googleMap)
            val filter = filter()

            val clusters = cluster(filter, locationBox)

            // map clusterMark from Responce
            compositeDisposable.add(
            clusters.flatMap(::flatMaptoCianFilter).map(::cianFilteredToClusterMarks)
                    .subscribe({ mainActivity.addCluster(it) },
                            { TODO("Parse error") },
                            { mainActivity.allClustersLoaded() }))

            val selectedCluster = selectedCluster(mainActivity.googleMap)

            val selectedFlates = selectedFlats(selectedCluster)
            compositeDisposable.addAll(
            selectedFlates.subscribe({ mainActivity.flatResult.addFlatResult(it) },
                    { TODO("Parse error") },
                    { mainActivity.flatResult.flatResultLoaded() }
            ))
        }
        Log.d("GOD","God object created")
    }

    val compositeDisposable:CompositeDisposable ;// = CompositeDisposable()
    fun clearFlows(){
        compositeDisposable.clear()
    }

    private fun mapCooridnates(location:Location)= LatLng(location.latitude, location.longitude)
    private fun flatMaptoCianFilter(cianResponse: MapResponse) = Flowable.fromIterable(cianResponse.filtered)
    private fun cianFilteredToClusterMarks(cianFiltered: CianFiltered) = cianFiltered.convertToClusterMark()



}