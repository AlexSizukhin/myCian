package com.shokker.mycian.DI

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.shokker.mycian.*
import com.shokker.mycian.Model.ClusterMark
import com.shokker.mycian.Model.FilterState
import com.shokker.mycian.Model.LocalStorage
import com.shokker.mycian.Model.ResultFlat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.reactivex.*
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class FlowProviderModule //@Inject constructor(var clusterApi : FlowProviderImpl)
{
    @Provides
    fun provideFlowProvider(@ApplicationContext appContext: Context):IFlowProvider
    {
        //return FlowProviderImpl(appContext)
        val x = FlowProviderImpl()
        x.clusterApi = CianServiceModule().provideCianServiceApi()          // fuck them all. DAGGER | DI!!! todo (change to correct)
        x.localStorage = LocalStorageModule().provideLocalStorage(appContext)         // fuck them all. DAGGER | DI!!! todo (change to correct)
        return x

    }
}

open class FlowProviderImpl
    //@Inject constructor(@ApplicationContext val appContext: Context)
    :IFlowProvider
{
    private val TAG = "FlowProviderImpl"

    @Inject
    @Singleton
    lateinit var clusterApi : CianLocationServiceApi

    @Inject
    @Singleton
    lateinit var localStorage: LocalStorage

    @SuppressLint("MissingPermission")
    override fun currentLocation(currentActivity: MainContract.IMyMapActivity): Flowable<Location> {
        val locationReq = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        val fuseLocationProviderClient = LocationServices.getFusedLocationProviderClient(currentActivity as Activity )

        return Flowable.create(object : FlowableOnSubscribe<Location>
        {
            override fun subscribe(emitter: FlowableEmitter<Location>) {
                fuseLocationProviderClient.requestLocationUpdates(locationReq,
                   object :LocationCallback(){
                       override fun onLocationResult(locResult: LocationResult) {
                           emitter.onNext(locResult.lastLocation)
                       } }
                , null)
            }
        },BackpressureStrategy.LATEST)
    }

    override fun mapLocationBox(currentActivity: MainContract.IMyMapActivity): Flowable<LatLngBounds> {
        Log.d(TAG,"Location Box flow creating")
        Log.d(TAG,"Current thread ${Thread.currentThread()}")
        return Flowable.create(object: FlowableOnSubscribe<LatLngBounds>
        {
            override fun subscribe(emitter: FlowableEmitter<LatLngBounds>) {
                Log.d(TAG, "Setting OnCameraIdleListener with ${emitter}")
                Log.d(TAG,"Current thread ${Thread.currentThread()}")
/*                map.setOnCameraIdleListener { object : GoogleMap.OnCameraIdleListener{
                    override fun onCameraIdle() {
                        emitter.onNext(map.projection.visibleRegion.latLngBounds)
                        Log.d(TAG,"On camera idle at ${map.projection.visibleRegion.latLngBounds}")
                        Log.d(TAG,"Current thread ${Thread.currentThread()}")
                    }*/             // this fucken part don't works! (((
                currentActivity.setOnCameraIdle { locationBox -> emitter.onNext(locationBox); Log.d(TAG,"new loc box is ${locationBox}") }
            }


        },BackpressureStrategy.LATEST)
    }

    override fun filter(flatFilter : MainContract.IFlatFilter): Flowable<FilterState> {

//        val filterFragment = pActivity as MainContract.IFlatFilterFragment
        return Flowable.create(object : FlowableOnSubscribe<FilterState> {
            override fun subscribe(emitter: FlowableEmitter<FilterState>) {
                Log.d(TAG, "Setting setOnChangeFilter with ${emitter}")
                flatFilter.setOnChangeFilter({newState ->
                    emitter.onNext(newState)
                    Log.d(TAG,"current thread ${Thread.currentThread()}")
                    Log.d(TAG,"Filter ${newState}")
                })
            }
        },BackpressureStrategy.LATEST)
    }


    override fun cluster(filter: Flowable<FilterState>, locationBox: Flowable<Pair<CianCoords, CianCoords>>): Flowable<MapResponse> {
        return Flowable.combineLatest(filter,locationBox,::prepareGetClusterReqFunction)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .flatMap (::mapResponseFlowFlatMap )

    }

    override fun selectedCluster(mapActivity: MainContract.IMyMapActivity): Flowable<ClusterMark> {
        return Flowable.create(object : FlowableOnSubscribe<ClusterMark> {
            override fun subscribe(emitter: FlowableEmitter<ClusterMark>) {
               mapActivity.setOnMarkClicked { mark-> emitter.onNext(mark) }
            }
        },BackpressureStrategy.LATEST)
    }

    override fun selectedFlats(mapActivity: MainContract.IMyMapActivity, cluster: Flowable<ClusterMark>): Flowable<ResultFlat> {
        return Flowable.create(object : FlowableOnSubscribe<ResultFlat> {
            override fun subscribe(emitter: FlowableEmitter<ResultFlat>) {

                mapActivity.flatResult.setOnClickFlat({flatClicked ->
                    emitter.onNext(flatClicked)
                })
            }
        },BackpressureStrategy.LATEST)
    }

    override fun fromLocalStorage(): Flowable<FilterState> {
        return Flowable.create(FlowableOnSubscribe { emitter: FlowableEmitter<FilterState> ->  emitter.onNext(localStorage.loadStoredFilter()!!) }
                ,BackpressureStrategy.LATEST)
    }

    override fun toLocalStorage(filterState: Flowable<FilterState>):Disposable {
        return filterState.observeOn(Schedulers.io()).subscribeOn(Schedulers.io()).subscribe({
            Log.d(TAG,"Saving ${it} to db")
            localStorage.saveFilter(it)        }

        )
    }

    private fun prepareGetClusterReqFunction(filter:FilterState,locationBox : Pair<CianCoords, CianCoords>):GetClusterReq {

        val jsonSubObject ="{\"region\":{\"type\":\"terms\",\"value\":[1]},\"_type\":\"flatsale\",\"engine_version\":{\"type\":\"term\",\"value\":2},\"room\":{\"type\":\"terms\",\"value\":[1,2]}}"
        val convertedObject: JsonObject = Gson().fromJson(jsonSubObject, JsonObject::class.java)
        return GetClusterReq(bbox = listOf(BBox(locationBox.first,locationBox.second))
                ,jsonQuery = convertedObject)
    }
    private fun mapResponseFlowFlatMap(request: GetClusterReq)
        = clusterApi.getClusters(request).toFlowable(BackpressureStrategy.LATEST)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())

}