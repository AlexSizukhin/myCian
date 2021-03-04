package com.shokker.mycian

import com.google.android.gms.maps.GoogleMap
import com.shokker.mycian.Model.ResultFlat

interface MainContract {
    interface MyMapActivity
    {
        fun updateClustersOnMap(list : List<Any>) // todo  CHANGE TYPE
        fun clearNotUsed()
        fun allClustersLoaded()
        fun addCluster()


        val googleMap: GoogleMap
        val flatResult: FlatResult
    }

    interface FlatResult
    {
        fun addFlatResult(resultList: ResultFlat)
        fun clearFlatResult()
        fun flatResultLoaded()
    }
}