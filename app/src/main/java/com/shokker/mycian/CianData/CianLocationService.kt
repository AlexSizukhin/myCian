package com.shokker.mycian

import android.util.Log
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.shokker.mycian.Model.ClusterMark
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface CianLocationServiceApi {
    @POST("./search-engine/v1/get-clusters-for-map/")
    @Headers("Content-Type: application/json",
        "User-Agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:85.0) Gecko/20100101 Firefox/85.0",
//    "Cookie: _CIAN_GK=4aa1eab9-1a47-41d9-bb13-8c9f77acb3e8; hide_onboarding=1; seen_pins_compressed=IwisGYDYDooFgDQHYKTtAHHOAGP+dgg; offerCardCountCallNowPopupShowed=0%3A1606313553941; cookie_agreement_accepted=true; countCallNowPopupShowed=0%3A1609182360308; sopr_utm=%7B%22utm_source%22%3A+%22direct%22%2C+%22utm_medium%22%3A+%22None%22%7D; __cfduid=d92d2c7518435058b4bc4987f34fce6a81613575449; _gcl_au=1.1.1203171510.1612448159; uxfb_usertype=searcher; uxs_uid=816c3310-66f3-11eb-8ee2-e7832afb4695; uxs_mig=1; tmr_reqNum=1163; tmr_lvid=8340150284361038e0ff4ac20c58e7d9; tmr_lvidTS=1517216437802; _ga=GA1.2.1419451531.1612448160; serp_stalker_banner=1; did_see_mobile_map_pins=1; login_mro_popup=meow; mapOffersHintVisible=0; nlbi_2094920=ywvABl4TdFz8aWaaSOXHzwAAAAAoQvVJoVK3q9kqlSSsH4bG; first_visit_time=1580150252827; session_region_id=1; session_main_town_region_id=1; activeAddForm53126834=1592584460790; activeAddForm42928=1599666219002; incap_ses_585_2094920=rRrEW+Me8SU1fsjHklYeCCDxWF8AAAAAv+1hb1Z4rGUAigS28wGOIQ==; fingerprint=2a74f8982c63224d7ce4bd90e1cc4732; __cf_bm=77bf7c4e9ca3f6885523cd0e94b2c9965df64625-1613582407-1800-ARHlZNC1V/6CmBVMwJN8ps6VHoNzmxPE9c0kGSTcgYA0JS+GXIje3wLVEOUUCa7pxNsIYXlR/uFs7T8769FHvac=; sopr_session=cc6323a3c0944502",
//    "Referer: https://www.cian.ru/map/?center=55.794692382637116%2C37.71940447692868&deal_type=sale&engine_version=2&offer_type=flat&region=1&room1=1&room2=1&zoom=14",
    "Content-Type: text/plain;charset=UTF-8")
    fun getClusters(@Body body: RequestBody): Observable<MapResponse> //Call<CianFiltered>//

    @POST("search-engine/v1/get-clusters-for-map/")
    @Headers("Content-Type: application/json",
        "User-Agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:85.0) Gecko/20100101 Firefox/85.0")
    fun getClusters():Observable<MapResponse>

    @POST("search-engine/v1/get-clusters-for-map/")
    @Headers("Content-Type: application/json",
        "User-Agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:85.0) Gecko/20100101 Firefox/85.0")
    fun getClusters(@Body body:GetClusterReq):Observable<MapResponse>


}
data class GetClusterReq(
//    val bbox:BBox,
    //val bbox: JsonArray,
/*    val bbox: Map<String, CianCoords> = mapOf("bottomRight" to ,
                                                "topLeft" to ),*/
    val bbox : List<BBox> = listOf(BBox(CianCoords(55.844079851698844,37.73803710937499),CianCoords(55.85644056569717,37.694091796874986))),
    val extended: Boolean = true,
    val subdomain: String = "www",
    val zoom: Int = 15,
    val jsonQuery: JsonObject
)
data class BBox(val bottomRight: CianCoords,
                val topLeft:CianCoords)

data class MapResponse(val filtered:List<CianFiltered>) {
}
data class CianFiltered(
    val clusterOfferIds: List<Long>,
    val maxPrice: Double,
    val minPrice: Double,
    val coordinates: CianCoords,
    val geohash: String
    ){
    fun convertToClusterMark():ClusterMark{ TODO() }
}

data class CianCoords(
    val lat: Double,
    val lng: Double
)


