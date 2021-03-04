package com.shokker.mycian

import android.content.ClipDescription
import io.reactivex.Single
import pl.droidsonroids.jspoon.annotation.Selector
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

class CianObject {
     //@Selector("#title")
    @Selector("title")
     lateinit var title:String
    // <p itemProp="description" class="a10a3f92e9--description-text--3Sal4">
    @Selector("p[itemProp=\"description\"]")
    lateinit var description: String

    @Selector("script[type=\"text/javascript\"]")
    lateinit var scripts : List<String>
}


// https://www.cian.ru/sale/flat/250956111/     https://www.cian.ru/rent/flat/247444328/
interface CianObjectServiceApi
{
    @GET("/{type}/flat/{objectId}/")
    fun getCianOject(@Path("type")type: String ="sale",
                     @Path("objectId") objectID: Int): Single<CianObject>



}