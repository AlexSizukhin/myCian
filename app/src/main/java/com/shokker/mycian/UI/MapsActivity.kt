package com.shokker.mycian.UI

//import jdk.incubator.jpackage.internal.Arguments.CLIOptions.context
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.shokker.mycian.*
import com.shokker.mycian.Model.ClusterMark
import com.shokker.mycian.Model.FilterState
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Named


@AndroidEntryPoint
class MapsActivity : AppCompatActivity(),MainContract.IFlatFilter,
                    MainContract.IMyMapActivity, IReactiveModel,
        OnMapReadyCallback, GoogleMap.OnCameraIdleListener, GoogleMap.OnMarkerClickListener
{ //, OnMapReadyCallback {
    private lateinit var mGoogleMap : GoogleMap

    @Inject
    lateinit var flowProvider: IFlowProvider                    // provides all flows and observables for God :)

    private lateinit var filterFragment: FilterFragment
    private lateinit var flatListFragment: FlatListFragment


    override val compositeDisposable = CompositeDisposable()
    private val TAG = "Activity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        filterFragment = supportFragmentManager.findFragmentById(R.id.filterFragment) as FilterFragment
        flatListFragment=supportFragmentManager.findFragmentById(R.id.flatListFragment) as FlatListFragment

        mapFragment.getMapAsync(this)

    }
    fun onFilterButtonClick(view: View)
    {
        Log.d(TAG, "Button ${view} clicked")
        showFilterDialog(true)

        Log.d(TAG,"current thread ${Thread.currentThread()}")
        onChangeFlatFilter?.invoke( filterFragment.getCurrentFilterState() )           // todo Generate FilterState from Fragment
    }

    override fun onMapReady(p0: GoogleMap) {
        mGoogleMap = p0
        mGoogleMap.setMinZoomPreference(15f)
        createAllFlows(flowProvider,this)       // prepare everything

        mGoogleMap.setOnCameraIdleListener (this)
        mGoogleMap.setOnMarkerClickListener(this)
    }

    override fun onDestroy() {
        clearFlows()
        super.onDestroy()
    }
    /////////////////////////////////// marks part    /////////////////////////////////////////
    override fun updateMarks(marks: List<ClusterMark>) {
        marks.forEach{
            if(markList.find { findIt-> it.location==findIt.location  } == null ) {
                val m = mGoogleMap.addMarker( MarkerOptions().position(it.location) )
                it.linkedMarker = m
                markList.add( it )
            }
        }
        markList.forEach{
            if(marks.contains(it))
                it.visible = true
            else it.visible = false
        }

        marks.forEach{
            it.linkedMarker?.isVisible = it.visible
        }
    }

    override fun getSelectedClusterMark(googleMapMarker: Marker):ClusterMark {
        return markList.findLast { it.linkedMarker == googleMapMarker }!!
    }
    private val markList = mutableListOf<ClusterMark>()

    override val flatResult: MainContract.IFlatResult
        get() = flatListFragment

    ////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////  map part ////////////////////////////////////////////
    override fun onCameraIdle() {
//        Log.d(TAG,"OnCamIdle event calling ${onCamIdleEvent}")
//        if(lastLatLngBounds!=mGoogleMap.projection.visibleRegion.latLngBounds)
            onCamIdleEvent?.invoke(mGoogleMap.projection.visibleRegion.latLngBounds)
//        lastLatLngBounds = mGoogleMap.projection.visibleRegion.latLngBounds
    }

    private var onCamIdleEvent: ((LatLngBounds) -> Unit)? = null
    override fun setOnCameraIdle(onCameraIdelFunc: (locationBox: LatLngBounds) -> Unit) {
        onCamIdleEvent = onCameraIdelFunc
        Log.d(TAG,"OnCamIdle set")
    }

    override fun onMarkerClick(gMarker: Marker): Boolean {
        val clusterMark = getSelectedClusterMark(gMarker)
        onMarkClicked?.invoke(clusterMark)
        return true
    }

    private var onMarkClicked: ((ClusterMark)->Unit)? = null
    override fun setOnMarkClicked(onMarkClickedFun:((ClusterMark)->Unit))
    {
        onMarkClicked = onMarkClickedFun
        Log.d(TAG,"onMarkClicked set")
    }

    override fun moveCamera(targetLocation: Location) {
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(targetLocation.latitude,targetLocation.longitude)))
    }
    ///////////////////////////////////// end of map part //////////////////////////////////

    ////////////////////////////////////// filter part ////////////////////////////////////
    override fun loadFilterState(filterState: FilterState) {
        Log.d(TAG,"Setting filter state from DB ${filterState}")
        this.filterFragment.setFilterState(filterState)
    }

    override fun showError(e: Throwable) {
        Log.d(TAG,"${e.message}")
        // todo make toast
    }

    override fun showFilterDialog(hideIfVisible: Boolean) {
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)

        if(filterFragment.isHidden())
            ft.show(filterFragment)
        else if(hideIfVisible)
            ft.hide(filterFragment)
        ft.commit()
    }
    override fun setOnChangeFilter(onChangeFunc: (newState: FilterState) -> Unit) {
        onChangeFlatFilter = onChangeFunc
    }
    private var onChangeFlatFilter: ((FilterState) -> Unit)? = null

    override val flatFilter: MainContract.IFlatFilter
        get() = this
}