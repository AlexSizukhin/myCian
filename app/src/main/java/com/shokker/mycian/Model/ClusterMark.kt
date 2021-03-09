package com.shokker.mycian.Model

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class ClusterMark(val location: LatLng)
    {
        var visible: Boolean = true
        var linkedMarker : Marker? = null

    companion object {
        fun getMark(id:Int):ClusterMark{
            return list[id]!!
        }
        fun getMark(mark:Marker):ClusterMark{
            return list.values.findLast { it?.linkedMarker == mark}!!
        }
        fun addMark(mark:ClusterMark):Int{
            list[currentId++] = mark
            return  currentId
        }
        fun hasMark(mark: ClusterMark):Boolean
        {
            return list.containsValue(mark)
        }
        fun getMark(mark: ClusterMark): ClusterMark?
        {
            return list.values.find { it.location == mark.location }
        }
        fun getAllList() : List<ClusterMark>{
            return list.values.toList()
        }
        private var currentId = 1
        private val list :  MutableMap<Int,ClusterMark> = mutableMapOf()
    }
}