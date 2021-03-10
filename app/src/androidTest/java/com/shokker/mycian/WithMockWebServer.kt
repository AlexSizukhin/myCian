package com.shokker.mycian

import android.util.Log
import com.google.common.io.Resources.getResource
import dagger.hilt.android.testing.HiltAndroidTest
import okhttp3.*
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import okio.buffer
import okio.source
import org.json.JSONObject
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import java.io.IOException
import java.lang.Exception
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@HiltAndroidTest
class WithMockWebServer {
    var mockWebServer = MockWebServer()
    var baseUrl: HttpUrl = mockWebServer.url("/v1/chat/")
    @Before
    fun startUp()
    {
        try {
            mockWebServer.start()
        }catch (e:Exception ){}
    }
    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
    @Test
    fun testMockServer() {
        var server = MockWebServer()

        server.enqueue(MockResponse().setBody("hello, world!"))
        server.enqueue(MockResponse().setBody("sup, bra?"))
        server.enqueue(MockResponse().setBody("yo dog"))
        // Start the server.
        // Start the server.
        server.start()

        // Ask the server for its URL. You'll need this to make HTTP requests.

        // Ask the server for its URL. You'll need this to make HTTP requests.
        val baseUrl: HttpUrl = server.url("/v1/chat/")

        // Exercise your application code, which should make those HTTP requests.
        // Responses are returned in the same order that they are enqueued.

        // Exercise your application code, which should make those HTTP requests.
        // Responses are returned in the same order that they are enqueued.
        val chat = Chat(baseUrl)

        chat.loadMore()
        assertEquals("hello, world!", chat.messages())

        chat.loadMore()
        chat.loadMore()
        assertEquals(
            """
          hello, world!
          sup, bra?
          yo dog
          """.trimIndent(), chat.messages()
        )

        // Optional: confirm that your app made the HTTP requests you were expecting.

        // Optional: confirm that your app made the HTTP requests you were expecting.
        val request1: RecordedRequest = server.takeRequest()
        assertEquals("/v1/chat/messages/", request1.path)
        assertNotNull(request1.getHeader("Authorization"))

        val request2: RecordedRequest = server.takeRequest()
        assertEquals("/v1/chat/messages/2", request2.path)

        val request3: RecordedRequest = server.takeRequest()
        assertEquals("/v1/chat/messages/3", request3.path)

        // Shut down the server. Instances cannot be reused.

        // Shut down the server. Instances cannot be reused.
        server.shutdown()
    }
    @Test
    fun testRequestLoading()
    {
        mockWebServer.enqueueResponse("small.json1")
        val chat = Chat(baseUrl)

        chat.loadMore()

    }

    @Test
    fun myTest() {
        //mockWebServer.enqueue(MockResponse().setBody())
         val x = getResource("small.json").readText()

        mockWebServer.enqueueResponse("cluster_responce.json")

        val client =  OkHttpClient()
        val formBody: RequestBody = FormBody.Builder()
            .add("username", "test")
            .add("password", "test")
            .build()
        val req = Request.Builder().url(baseUrl).post(formBody).build()

        val t = client.newCall(req).execute().body?.string()
        Log.d("Test", t.toString())
        println(t)
    }



    internal fun MockWebServer.enqueueResponse(fileName: String, code: Int = 200) {
        val inputStream = javaClass.classLoader?.getResourceAsStream("$fileName")

        val source = inputStream?.let { inputStream.source().buffer() }
        source?.let {
            enqueue(
                MockResponse()
                    .setResponseCode(code)
                    .setBody(source.readString(StandardCharsets.UTF_8))
            )
        }
    }

    class Chat(val url: HttpUrl)
    {
        private val strings =  LinkedList<String>()
        fun loadMore(reqBody: RequestBody)
        {
            val client =  OkHttpClient()
            val req = Request.Builder().url(url).method("POST", reqBody) .build()

            strings.push(client.newCall(req).execute().body?.string())
        }
        fun loadMore()
        {
            val client = OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS).build()

            val req = Request.Builder().url(url).build()

            strings.push(client.newCall(req).execute().body?.string())
        }
        fun messages():String
        {
            return strings.toString()
        }
    }
}