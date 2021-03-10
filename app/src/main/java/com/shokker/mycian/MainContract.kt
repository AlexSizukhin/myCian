package com.shokker.mycian

import android.location.Location
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

        //val googleMap: GoogleMap
        val flatResult: IFlatResult
        val flatFilter: IFlatFilter

        fun setOnCameraIdle(onCameraIdelFunc: (locationBox: LatLngBounds)->Unit)
        fun setOnMarkClicked(onMarkClickedFun:((ClusterMark)->Unit))

        fun moveCamera(targetLocation: Location)

        fun showError(e:Throwable)
        fun showFilterDialog(hideIfVisible: Boolean)
    }

    interface IFlatResult
    {
        fun addFlatResult(resultList: ResultFlat)
        fun clearFlatResult()
        fun flatResultLoaded()

        fun setOnClickFlat(onClickFlatFunc: (clickedFlat:ResultFlat)->Unit)
    }
    interface IFlatFilter
    {
        fun setOnChangeFilter(onChangeFunc: (newState:FilterState)->Unit)
        fun loadFilterState(filterState: FilterState)
        fun showFilterDialog(hideIfVisible: Boolean)
    }
}