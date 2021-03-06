package com.shokker.mycian

import com.google.android.gms.maps.GoogleMap
import com.shokker.mycian.Model.ClusterMark
import com.shokker.mycian.Model.ResultFlat

interface MainContract {
    interface IMyMapActivity
    {
        fun clearNotUsed()
        fun allClustersLoaded()
        fun addCluster(mark:ClusterMark)


        val googleMap: GoogleMap
        val flatResult: IFlatResultFragment
    }

    interface IFlatResultFragment
    {
        fun addFlatResult(resultList: ResultFlat)
        fun clearFlatResult()
        fun flatResultLoaded()
    }
}