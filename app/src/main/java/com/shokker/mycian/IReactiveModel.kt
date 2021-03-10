package com.shokker.mycian

import android.location.Location
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.shokker.mycian.Model.ClusterMark
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.exceptions.CompositeException
import io.reactivex.schedulers.Schedulers


interface IReactiveModel
{             // Presenter or Flow dispatcher
    // do magic! create the world
    fun createAllFlows(flowProvider: IFlowProvider, mainActivity: MainContract.IMyMapActivity)
    {
        val TAG = "ReactiveModelInit"
        Log.d(TAG,"God object started initialisation")
        flowProvider.apply {
            val currentLocation = flowProvider.currentLocation(mainActivity)
            compositeDisposable.add( currentLocation.subscribeOn(AndroidSchedulers.mainThread()).subscribe {
                if (it != null)
                    mainActivity.moveCamera(it)

            })
            Log.d(TAG,"Current thread ${Thread.currentThread()}")
            val locationBox = mapLocationBox(mainActivity).distinctUntilChanged() //.map(::latLngBoundsToPait)
            val filter = filter(mainActivity.flatFilter)

            compositeDisposable.add(fromLocalStorage().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).
            subscribe({
                        Log.d(TAG,"Loading filter state from thread ${Thread.currentThread()}")
                        mainActivity.flatFilter.loadFilterState(it)
                      },
                 {
                     if(it is NullPointerException) {
                         Log.d(TAG, "Error ${it.message} filter state from thread ${Thread.currentThread()} record not found")
                         mainActivity.showError(it)
                         mainActivity.showFilterDialog(false)
                     }else  throw it
                     }
            ))

            compositeDisposable.add(toLocalStorage(filter))
/*            compositeDisposable.add(filter.subscribeOn(Schedulers.io()).subscribe({ Log.d(ReactiveModelInit,"fiter temp subscription ${it}")},{Log.e(ReactiveModelInit,"${it.message}")}))
            compositeDisposable.add(locationBox.subscribe({ Log.d(ReactiveModelInit,"box temp subscription ${it}")},{Log.e(ReactiveModelInit,"${it.message}")}))
*/

           val clusters = cluster(filter, locationBox.map(::latLngBoundsToPair))
                   .observeOn(Schedulers.io())
                   .subscribeOn(Schedulers.io())
    /*               .subscribe(
                            { Log.d(ReactiveModelInit,"clusters ${it}")},
                            { ParseCompositeExceptions(it); throw it },
                            { mainActivity.allClustersLoaded();  Log.d(ReactiveModelInit,"all cluster added") })*/
            // map clusterMark from Responce
            compositeDisposable.add(
            clusters.map(::cianFilteredToClusterMarks)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe({ mainActivity.updateMarks(it); Log.d("God","adding cluster ${it}") },
                            { ParseCompositeExceptions(it); throw it },
                            { Log.d(TAG,"all cluster added") }))

            val selectedCluster = selectedCluster(mainActivity)
            selectedCluster.subscribe { mainActivity.flatResult.clearFlatResult()            }     // check: must be before loading list

            val selectedFlates = selectedFlats(mainActivity, selectedCluster)
/*            compositeDisposable.addAll(
            selectedFlates.subscribe({ mainActivity.flatResult.addFlatResult(it); Log.d(ReactiveModelInit,"adding result to fragment ${it}") },
                    { TODO("Parse error") },
                    { mainActivity.flatResult.flatResultLoaded(); Log.d(ReactiveModelInit,"all flated added to result ") }
            ))*/


        }
        Log.d(TAG,"God object created")
    }

    val compositeDisposable:CompositeDisposable ;// = CompositeDisposable()
    fun clearFlows(){
        compositeDisposable.clear()
    }

    // simple mappers
    private fun mapCooridnates(location:Location)= LatLng(location.latitude, location.longitude)
    private fun cianFilteredToClusterMarks(mapResponse: MapResponse): List<ClusterMark>
    {
        val clusterList:MutableList<ClusterMark> = mutableListOf()
        mapResponse.filtered.forEach({
            clusterList.add(ClusterMark(LatLng(it.coordinates.lat,it.coordinates.lng)))
        })
        return clusterList
    }
    private fun latLngBoundsToPair(latLang: LatLngBounds):Pair<CianCoords,CianCoords>
            {
                val p = Pair(CianCoords(latLang.southwest.latitude,latLang.northeast.longitude),
                        CianCoords(latLang.northeast.latitude,latLang.southwest.longitude))
                return p
            }

    // composite error handling
    private fun ParseCompositeExceptions(e:Throwable)
    {
        if(e is CompositeException)
            e.exceptions.forEach({ ParseCompositeExceptions(it) })
        else
            Log.e("ReactiveModelInit","${e.message} ... ${e.printStackTrace()}")
    }

}