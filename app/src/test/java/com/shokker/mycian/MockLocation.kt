package com.shokker.mycian

import android.location.Location
import com.google.android.gms.maps.model.LatLng

class MockLocation(): Location("null")
{
    constructor(lat: Double, lng: Double) : this() { latLng = LatLng(lat,lng) }
    //constructor(latLngStr:String) : this() {  }
    private var latLng = LatLng(55.7558,37.6173)
    override fun getLatitude(): Double {
        return latLng.latitude
    }

    override fun setLatitude(latitude: Double) {
        latLng =  LatLng(latitude,latLng.longitude)
    }

    override fun getLongitude(): Double {
        return latLng.longitude
    }

    override fun setLongitude(longitude: Double) {
        latLng =  LatLng(latLng.latitude,longitude)
    }

    override fun toString(): String {
        return "[gps ${latLng.latitude},${latLng.longitude}]"
    }
}