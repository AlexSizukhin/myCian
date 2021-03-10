package com.shokker.mycian

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.shokker.mycian.Model.ClusterMark
import com.shokker.mycian.Model.FilterState
import com.shokker.mycian.Model.ResultFlat

interface MainContract {
    interface IMyMapActivity
    {
        fun updateMarks(marks:List<ClusterMark>)
        fun getSelectedClusterMark(googleMapMarker:Marker):ClusterMark

        val googleMap: GoogleMap
        val flatResult: IFlatResultFragment
//        val flatFilter: IFlatFilterFragment

        fun setOnChangeFilter(onChangeFunc: (newState:FilterState)->Unit)
        fun setOnCameraIdle(onCameraIdelFunc: (locationBox: LatLngBounds)->Unit)
        fun loadFilterState(filterState: FilterState)

        fun showError(e:Throwable)
        fun showFilterDialog(hideIfVisible: Boolean)
    }

    interface IFlatResultFragment
    {
        fun addFlatResult(resultList: ResultFlat)
        fun clearFlatResult()
        fun flatResultLoaded()

        fun setOnClickFlat(onClickFlatFunc: (clickedFlat:ResultFlat)->Unit)
    }
/*    interface IFlatFilterFragment
    {
        fun setOnChangeFilter(onChangeFunc: (newState:FilterState)->Unit)
    }*/
}