package com.shokker.mycian.UI

//import jdk.incubator.jpackage.internal.Arguments.CLIOptions.context
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.shokker.mycian.*
import com.shokker.mycian.R
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
class MapsActivity : AppCompatActivity() { //, OnMapReadyCallback {

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
        //mapFragment.getMapAsync(this)
        val mapObservable: Single<GoogleMap> = Single.create(
                object : SingleOnSubscribe<GoogleMap> {
                    override fun subscribe(emitter: SingleEmitter<GoogleMap>) {
                        mapFragment.getMapAsync(object : OnMapReadyCallback {
                            override fun onMapReady(p0: GoogleMap?) {
                                emitter.onSuccess(p0!!)
                            }
                        })
                    }
                }
        )
        compositeDisposable.add(mapObservable.subscribe(Consumer { mMap = it; Log.d(TAG, "Map loaded") }))


        myWebView = findViewById(R.id.webView)
        myWebView.settings.javaScriptEnabled = true
        myWebView.settings.setSupportMultipleWindows(false)
        myWebView.settings.setSupportZoom(true)

        val newUA = "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0"
        myWebView.settings.userAgentString = newUA
        //myWebView.loadUrl("http://www.cian.ru")
        //myWebView.setWebViewClient(MyWebViewClient())
        myWebView.setWebChromeClient(MyWebChromeClient())

        //myWebView.loadUrl("https://www.cian.ru/sale/flat/249506286/")
        myWebView.loadUrl("https://ya.ru")
        myWebView.loadUrl("javascript:alert('aa');")
        myWebView.loadUrl(
                "javascript:(function f() { " +
                        "var element = document.getElementsByClassName('search2__button');"
                        + "element.parentNode.removeChild(element);"
                        + "alert('aaa');"
                        +"})()")


        doSmthWithMap()

    }


   /* private class WebviewJSInterface {
        @JavascriptInterface
        fun processHTML(output: String) {
            Log.d("log", "hello: $output")
        }
    }

    fun onPageFinished(view: WebView?, url: String?) {
        view?.loadUrl("javascript:window. " + JS_INTERFACE.toString() + ".processHTML(document.getElementsByTagName('body')[0].innerHTML);")
        super.onPageFinished(view, url)
    }*/

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

       /* val gamma = rep.emitCurrentLocation()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it != null) {
                        Log.d(TAG,"Pure RX Current location is ${it.longitude} x ${it.latitude} ")
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            LatLng(it.latitude, it.longitude), 15.0f
                        ))
                        }else Log.d(TAG,"it is null")
                    },{
                    Log.e(TAG,"Pure RX Current location ERROR ${it.message}")
                }
                )

        compositeDisposable.add(gamma)*/
    }
    @Inject
    lateinit var rep : LocationRepository
    private fun loadCianObject(objectId: Int): CianObject
    {
        TODO()
    }
    private fun prepareGetCluster(): GetClusterReq
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
    /*override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        getLocationPermission()

        mMap.uiSettings.isScrollGesturesEnabled = false
        mMap.uiSettings.isRotateGesturesEnabled = false
        mMap.setOnMarkerClickListener { Log.d(TAG, it.toString()) ;
            myWebView.loadUrl("https://www.cian.ru/sale/flat/${it.snippet}/")
            true}
    }*/

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




}