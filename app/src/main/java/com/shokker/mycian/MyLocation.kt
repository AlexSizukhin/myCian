package com.shokker.mycian

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.SystemClock
import android.util.Log
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.*
import java.util.concurrent.Callable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepository @Inject constructor(@ApplicationContext private val context:Context) {
    private val TAG = "LocationRepository"

 /*   @SuppressLint("MissingPermission")
    fun emitCurrentLocation():Flowable<Location>
    {
   //     prepareLocation()
//        val o: Flowable<Location> = Observable.fromCallable(callable).toFlowable(BackpressureStrategy.DROP)
        Log.d(TAG, "Getting device location for RX")
        val o:LocationCallback =  object:LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
            if (locationResult == null) {
                Log.d(TAG, "Location result is NULL")
                return
            }else
            {
                Log.d(TAG, "Location result is NOT NULL ${locationResult}")
                curLocation =  locationResult.lastLocation
                callable.call()
            }}
    }

        fuseLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        //fuseLocationProviderClient.requestLocationUpdates(locationReq, mLocationCallback, null);
        fuseLocationProviderClient.requestLocationUpdates(locationReq,
            o ,null )
        //fuseLocationProviderClient.re
        val mapObservable : Observable<GoogleMap> = Observable.create{ object: ObservableOnSubscribe<GoogleMap>{
            override fun subscribe(emitter: ObservableEmitter<GoogleMap>) {

            }

        } }

        Observable.fromCallable(o::onLocationResult)
        return mapObservable //Observable.fromCallable { o::onLocationResult }.toFlowable()

    }*/
    val callable:Callable<Location> = Callable {
//        Thread.sleep(10000)
        Log.d(TAG,"callable called")
        return@Callable curLocation
    }
    fun callback1():Location{
        Log.d(TAG,"callback called")
        return curLocation!!
    }
    var curLocation : Location? = Location("0,0")

    @SuppressLint("MissingPermission")
    fun prepareLocation()
    {
        Log.d(TAG, "Getting device location for RX")

        fuseLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        //fuseLocationProviderClient.requestLocationUpdates(locationReq, mLocationCallback, null);
        fuseLocationProviderClient.requestLocationUpdates(locationReq,
                object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        if (locationResult == null) {
                            Log.d(TAG, "Location result is NULL")
                            return
                        }else
                        {
                            Log.d(TAG, "Location result is NOT NULL ${locationResult}")
                            curLocation =  locationResult.lastLocation
                            callable.call()
                        }}
                                            }  ,null )
    }
    private lateinit var fuseLocationProviderClient : FusedLocationProviderClient
    private var locationReq = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(10 * 1000)        // 10 seconds, in milliseconds
            .setFastestInterval(1 * 1000); // 1 second, in milliseconds



    val serverDownloadObservable = Observable.create { emitter: ObservableEmitter<Int?> ->
        Thread.sleep(10000) // simulate delay
        emitter.onNext(5)
        emitter.onComplete()
    }


}

class MO: Observable<Location>()
{
    override fun subscribeActual(observer: Observer<in Location>?) {
        TODO("Not yet implemented")
    }

}