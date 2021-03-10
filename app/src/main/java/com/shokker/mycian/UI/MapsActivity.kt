package com.shokker.mycian.UI

//import jdk.incubator.jpackage.internal.Arguments.CLIOptions.context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
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


@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), MainContract.IMyMapActivity, IReactiveModel, OnMapReadyCallback, GoogleMap.OnCameraIdleListener { //, OnMapReadyCallback {
    ////////////////////////////////////////////////////////////////////////////////////////////

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

    private lateinit var mGoogleMap : GoogleMap
    override val googleMap: GoogleMap
        get() = mGoogleMap


    override val flatResult: MainContract.IFlatResultFragment
        get() = flatListFragment

    override fun setOnChangeFilter(onChangeFunc: (newState: FilterState) -> Unit) {
           onChangeFlatFilter = onChangeFunc
       }
       private var onChangeFlatFilter: ((FilterState) -> Unit)? = null

/*    override val flatFilter: MainContract.IFlatFilterFragment
        get() = filterFragment*/

    ////////////////////////////////////////////////////////////////////////////////////////////
    @Inject
    lateinit var flowProvider: IFlowProvider                    // provides all flows and ovservables for God :)

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

        flowProvider.currectActivity = this
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
        googleMap.addMarker(MarkerOptions().position(LatLng(55.849781977,37.7163779363)).title("AA"))
    }

    override fun onDestroy() {
        clearFlows()
        super.onDestroy()
    }

    private var lastLatLngBounds: LatLngBounds? = null
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
}