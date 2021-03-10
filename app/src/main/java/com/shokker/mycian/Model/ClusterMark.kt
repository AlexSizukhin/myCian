package com.shokker.mycian.Model

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class ClusterMark(val location: LatLng)
    {
        var visible: Boolean = true
        var linkedMarker : Marker? = null

}