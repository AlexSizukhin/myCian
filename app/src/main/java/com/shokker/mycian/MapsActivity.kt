package com.shokker.mycian

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
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
    public lateinit var cianServiceApi: CianServiceApi

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
    private fun doSmthWithMap()
    {
        val alpha = cianServiceApi.getClusters(prepareGetCluster())
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

        val location = getLastKnownLocation()
        if(location!=null)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(location.latitude, location.longitude)))


        // Add a marker in Sydney and move the camera
        /*val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
*/
        mMap.setOnMarkerClickListener { Log.d(TAG, it.toString()) ;
            myWebView.loadUrl("https://www.cian.ru/sale/flat/${it.snippet}/")
            true}
    }
    private fun getLastKnownLocation(): Location? {
        val mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers: List<String> = mLocationManager.getProviders(true)// mLocationManager.getProviders(true)
        var bestLocation: Location? = null
        for (provider in providers) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                continue
            }

            val l: Location? = mLocationManager.getLastKnownLocation(provider)// .getLastKnownLocation(provider)
            Log.d(TAG,"last known location, provider: ${provider}, location: ${l}")
            if (l == null) {
                Log.d(TAG,"location is NULL")
                continue
            }
            if (bestLocation == null
                    || l.accuracy < bestLocation.accuracy) {
                Log.d(TAG,"found best last known location: ${bestLocation?.accuracy}")
                bestLocation = l
            }
        }
        return bestLocation
    }
    val LOCATION_PERMISSION_REQUEST_CODE = 5322
    var mLocationPermissionGranted : Boolean = false

/////////////////////////////////////////////////////////////////////////////
    private fun getLocationPermission()
    {
        Log.d(TAG,"getLocation permissions: getting location permissions")
        val permissions:Array<String> = arrayOf( Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)

        if(ContextCompat.checkSelfPermission(this.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            if(ContextCompat.checkSelfPermission(this.applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED)
            {
                mLocationPermissionGranted  = true
            } else{
                ActivityCompat.requestPermissions(this,permissions,
                LOCATION_PERMISSION_REQUEST_CODE)
            }

        }
    }
}