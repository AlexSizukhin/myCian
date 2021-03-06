package com.shokker.mycian

import android.app.Application
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import dagger.hilt.android.HiltAndroidApp
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import javax.inject.Inject


@HiltAndroidApp
class MyCianApp:Application() {

    @Inject
    public lateinit var cianLocationServiceApi: CianLocationServiceApi
    @Inject
    public lateinit var cianObjectServiceApi : CianObjectServiceApi

    @Inject
    public lateinit var creator : God

    private val compositeDisposable = CompositeDisposable()
    private val TAG = "Application"

    override fun onCreate() {
        super.onCreate()



        val alpha = cianLocationServiceApi.getClusters(prepareGetCluster())
        //val alpha = cianServiceApi.getClusters(prepareRequest())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.d(TAG, "onNextCalled")
                    it.filtered.forEach {
                        Log.d(TAG, it.coordinates.toString())
                        it.clusterOfferIds.forEach {
                            Log.d(TAG, "         Object id: ${it}")
                        }
                    }
                },
                {
                    Log.e(TAG, "${it.message}")
                })
        compositeDisposable.add(alpha)

        val beta = cianObjectServiceApi.getCianOject(type = "sale",objectID = 239952919)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.d(TAG, "onNextCalled")
                    Log.d(TAG,"#title is: ${it.title}")
                    Log.d(TAG,"#description is: ${it.description}")
                    Log.d(TAG,"total scripts : ${it.scripts.count()}")
                },
                {
                    Log.e(TAG, "${it.message}")
                })

        compositeDisposable.add(beta)
    }
    private fun prepareRequest():RequestBody
    {
        val cBody = "{\"zoom\":15,\"bbox\":[{\"bottomRight\":{\"lat\":55.78840776880526,\"lng\":37.7490234375},\"topLeft\":{\"lat\":55.806974017300476,\"lng\":37.672119140625}}],\"jsonQuery\":{\"region\":{\"type\":\"terms\",\"value\":[1]},\"_type\":\"flatsale\",\"engine_version\":{\"type\":\"term\",\"value\":2},\"room\":{\"type\":\"terms\",\"value\":[1,2]}},\"extended\":true,\"subdomain\":\"www\"}"
        return  RequestBody.create("text/plain".toMediaTypeOrNull(), cBody)
    }
    private fun prepareGetCluster():GetClusterReq
    {
/*        val bbox = BBox(
            CianCoords(55.78840776880526, 37.7490234375), CianCoords(
                55.806974017300476,
                37.672119140625
            )
        )*/
        val bboxString ="[{\"bottomRight\":{\"lat\":55.78840776880526,\"lng\":37.7490234375},\"topLeft\":{\"lat\":55.806974017300476,\"lng\":37.672119140625}}]"
        val bbox: JsonArray = Gson().fromJson(bboxString, JsonArray::class.java)

        val jsonSubObject ="{\"region\":{\"type\":\"terms\",\"value\":[1]},\"_type\":\"flatsale\",\"engine_version\":{\"type\":\"term\",\"value\":2},\"room\":{\"type\":\"terms\",\"value\":[1,2]}}"
        val convertedObject: JsonObject = Gson().fromJson(jsonSubObject, JsonObject::class.java)
        return GetClusterReq(jsonQuery = convertedObject)
//        val cBody = "{\"zoom\":15,\"bbox\":[{\"bottomRight\":{\"lat\":55.78840776880526,\"lng\":37.7490234375},\"topLeft\":{\"lat\":55.806974017300476,\"lng\":37.672119140625}}],\"jsonQuery\":{\"region\":{\"type\":\"terms\",\"value\":[1]},\"_type\":\"flatsale\",\"engine_version\":{\"type\":\"term\",\"value\":2},\"room\":{\"type\":\"terms\",\"value\":[1,2]}},\"extended\":true,\"subdomain\":\"www\"}"
        //val body: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), custerReq)

    }

    override fun onTerminate() {
        compositeDisposable.dispose()
        super.onTerminate()
    }
}