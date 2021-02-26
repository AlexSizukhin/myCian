package com.shokker.mycian

//import jdk.incubator.jpackage.internal.Arguments.CLIOptions.context
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.gson.JsonObject
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var myWebView:WebView
    @Inject
    public lateinit var cianLocationServiceApi: CianLocationServiceApi

    private val compositeDisposable = CompositeDisposable()
    private val TAG = "Activity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        myWebView = findViewById(R.id.webView)
        myWebView.settings.javaScriptEnabled = true
        myWebView.settings.setSupportMultipleWindows(false)
        myWebView.settings.setSupportZoom(false)
        myWebView.setWebViewClient(WebViewClient())
      //  myWebView.loadUrl("http://www.cian.ru")
      //  myWebView.loadUrl("https://www.cian.ru/sale/flat/249506286/")

        doSmthWithMap()

    }

    @SuppressLint("MissingPermission")
    private fun doSmthWithMap()
    {
        val alpha = cianLocationServiceApi.getClusters(prepareGetCluster())
            //val alpha = cianServiceApi.getClusters(prepareRequest())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    {
                        Log.d(TAG, "onNextCalled")
                        it.filtered.forEach {
                            Log.d(TAG, it.coordinates.toString())
                            val marker = MarkerOptions().position(LatLng(it.coordinates.lat, it.coordinates.lng))
                                    .title(it.minPrice.toString())
                                    .snippet(it.clusterOfferIds.first().toString())

                            val m = mMap.addMarker(marker)

                        }
                    },
                    {
                        Log.e(TAG, "${it.message}")
                    })

        compositeDisposable.add(alpha)

/*
        val rxLocation = RxLocation(this.applicationContext)

        val locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000)

        rxLocation.location().updates(locationRequest)
                .flatMap { location -> rxLocation.geocoding().fromLocation(location).toObservable() }
                .subscribe { address ->{
                    Log.d(TAG,address.toString())
                } }
  */
   /*     val rep = LocationRepository()
        rep.emitCurrentLocation()
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({

                })
*/

    }
    private fun loadCianObject(objectId: Int):CianObject
    {
        TODO()
    }
    private fun prepareGetCluster():GetClusterReq
    {
        val jsonSubObject ="{\"region\":{\"type\":\"terms\",\"value\":[1]},\"_type\":\"flatsale\",\"engine_version\":{\"type\":\"term\",\"value\":2},\"room\":{\"type\":\"terms\",\"value\":[1,2]}}"
        val convertedObject: JsonObject = Gson().fromJson(jsonSubObject, JsonObject::class.java)
        return GetClusterReq(jsonQuery = convertedObject)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        getLocationPermission()
        if(mLocationPermissionGranted)
            getDeviceLocation()
  /*      val location = getLastKnownLocation()
        if(location!=null)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(location.latitude, location.longitude)))

*/
        // Add a marker in Sydney and move the camera
        /*val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
*/
        mMap.uiSettings.isScrollGesturesEnabled = false
        mMap.uiSettings.isRotateGesturesEnabled = false
        mMap.setOnMarkerClickListener { Log.d(TAG, it.toString()) ;
            myWebView.loadUrl("https://www.cian.ru/sale/flat/${it.snippet}/")
            true}
    }

    val LOCATION_PERMISSION_REQUEST_CODE = 5322
    var mLocationPermissionGranted : Boolean = false

/////////////////////////////////////////////////////////////////////////////
    private fun getLocationPermission()
    {
        Log.d(TAG, "getLocation permissions: getting location permissions")
        val permissions:Array<String> = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)

        if(ContextCompat.checkSelfPermission(this.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            if(ContextCompat.checkSelfPermission(this.applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED)
            {
                mLocationPermissionGranted  = true
                Log.d(TAG, "Have a location permission")
            } else{
                ActivityCompat.requestPermissions(this, permissions,
                        LOCATION_PERMISSION_REQUEST_CODE)
            }

        }
    }

    private lateinit var fuseLocationProviderClient : FusedLocationProviderClient
    private var locationReq = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(10 * 1000)        // 10 seconds, in milliseconds
            .setFastestInterval(1 * 1000); // 1 second, in milliseconds
    @SuppressLint("MissingPermission")
    private fun getDeviceLocation()
    {
        Log.d(TAG, "Getting device location")

        fuseLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        //fuseLocationProviderClient.requestLocationUpdates(locationReq, mLocationCallback, null);
        fuseLocationProviderClient.requestLocationUpdates(locationReq,
                object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        if (locationResult == null) {
                            Log.d(TAG, "Location result it NULL")
                            return
                        }
                        for (location in locationResult.locations) {
                            if (location != null) {

                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        LatLng(location.latitude, location.longitude), 15.0f
                                ))

                                Log.d(TAG, "${location.latitude} x ${location.longitude}")

                                val curScreen: LatLngBounds = mMap.getProjection()
                                        .getVisibleRegion().latLngBounds
                                val ul = curScreen.northeast
                                val br = curScreen.southwest
                            }
                        }
                    }
                }, null);
        try {
            if(mLocationPermissionGranted)
            {
                val location = fuseLocationProviderClient.lastLocation
                location.addOnCompleteListener({
                    if (it.isSuccessful && it.isComplete && it.result != null) {
                        Log.d(TAG, "onComplete found location ${it.result}")

                    } else Log.d(TAG, "onComplete but NULL or ...")
                })
            }
        }catch (e: Exception)
        {
            Log.e(TAG, "getDeviceLocation error ${e.message}")
        }
    }
    var mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            if (locationResult == null) {
                Log.d(TAG, "Location result it NULL")
                return
            }
            for (location in locationResult.locations) {
                if (location != null) {
                   Log.d(TAG, "${location.latitude} x ${location.longitude}")
                }
            }
        }
    }
}