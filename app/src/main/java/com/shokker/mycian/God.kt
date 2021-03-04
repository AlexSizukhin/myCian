package com.shokker.mycian

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class God
    @Inject constructor(flowProvider: IFlowProvider)
{             // Presenter or Flow dispatcher
    init{
        // do magic! create the world
        flowProvider.apply {
            activityReady().subscribe{ mainActivity->
                val currectLocation = currentLocation()
                currectLocation.subscribe({
                    if (it != null)
                        mainActivity.googleMap.moveCamera(CameraUpdateFactory.newLatLng(
                                LatLng(it.latitude, it.longitude)))         // todo - mapping
                })
                val locationBox = mapLocationBox(mainActivity.googleMap)
                val filter = filter()

                val clusters = cluster(filter, locationBox)                  // subscribe to MapActivity
                clusters.subscribe ({ mainActivity.addCluster() } ,
                        {TODO("Parse erorr")},
                        {mainActivity.allClustersLoaded()})

                val selectedCluster = selectedCluster(mainActivity.googleMap)

                val selectedFlates = selectedFlats(selectedCluster)     // subscribe to FlatListFragment
                selectedFlates.subscribe( { mainActivity.flatResult.addFlatResult(it) },
                        {TODO("Parse erorr")},
                        {mainActivity.flatResult.flatResultLoaded()}
                )
            }

        }
    }

}