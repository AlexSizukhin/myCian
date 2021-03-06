package com.shokker.mycian.UI

//import jdk.incubator.jpackage.internal.Arguments.CLIOptions.context
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.shokker.mycian.*
import com.shokker.mycian.Model.ClusterMark
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), MainContract.IMyMapActivity, God, OnMapReadyCallback { //, OnMapReadyCallback {
    ////////////////////////////////////////////////////////////////////////////////////////////
    override fun clearNotUsed() {
        TODO("Not yet implemented")
    }

    override fun allClustersLoaded() {
        TODO("Not yet implemented")
    }

    override fun addCluster(mark: ClusterMark) {
        TODO("Not yet implemented")
    }

    private lateinit var mGoogleMap : GoogleMap
    override val googleMap: GoogleMap
        get() = mGoogleMap


    override val flatResult: MainContract.IFlatResultFragment
        get() = flatListFragment

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

        mapFragment.getMapAsync(this)

    }
    fun onFilterButtonClick(view: View)
    {
        Log.d(TAG, "Button ${view} clicked")
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
        //ft.hide(flatListFragment)

        if(filterFragment.isHidden())
            ft.show(filterFragment)
        else
            ft.hide(filterFragment)
        ft.commit()
    }

    override fun onMapReady(p0: GoogleMap) {
        mGoogleMap = p0
        createAllFlows(flowProvider,this)       // prepare everything
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}