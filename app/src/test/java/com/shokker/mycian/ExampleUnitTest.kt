package com.shokker.mycian


import android.content.Context
import android.content.res.Resources
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.shokker.mycian.UI.MyNumberController
import okhttp3.*
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.json.JSONObject
import org.json.JSONStringer
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import java.lang.Exception
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(JUnit4::class)
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Mock
    var fakeContext: Context? = null

    @Test
    fun testFormating()
    {
        val mockContext = mock<Context>{
            on { theme } doReturn mock {
                //on { obtainStyledAttributes(*,*,*) } doReturn Unit
            }
        }

        val mnc=  MyNumberController(mockContext)           // not works!!!
        mnc.apply {
            val f1 = 100.0f
            assertEquals("100", f1.toString(0))
            val f2 = 100.1f
            assertEquals("100.1", f2.toString(1))
            val f3 = 101.1f
            assertEquals("100", f3.toString(-1))
        }
    }

    @Test
    fun jsonComparation()
    {
        val fileString =  javaClass.classLoader!!.getResource("small.json")!!.readText()
        println(fileString)

        val j1: JsonObject = Gson().fromJson(fileString, JsonObject::class.java)
        val j2: JsonObject = Gson().fromJson(" "+fileString+" ", JsonObject::class.java)

        println(j1.hashCode())
        println(j2.hashCode())
        Assert.assertEquals(j1,j2)
    }

    @Test
    fun compareJsonsWithMockServer()
    {
        val mockServer = MockWebServer()
        mockServer.start()
        val baseUrl = mockServer.url("/v1/chat/")

        val fileString =  javaClass.classLoader!!.getResource("cluster_responce.json")!!.readText()
        val j1: JsonObject = Gson().fromJson(fileString, JsonObject::class.java)

        val mockedResponse = MockResponse()
        mockedResponse.setResponseCode(200)
        mockedResponse.setBody(fileString) // sample JSON

        mockServer.enqueue(mockedResponse)

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.SECONDS) // For testing purposes
            .readTimeout(2, TimeUnit.SECONDS) // For testing purposes
            .writeTimeout(2, TimeUnit.SECONDS)
            .build()

        val req = Request.Builder().url(baseUrl).get().build()

        val response = okHttpClient.newCall(req).execute()
        val j2: JsonObject = Gson().fromJson(response.body!!.string(), JsonObject::class.java)
        Assert.assertEquals(j1,j2)

        println(j1)
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
/*
    @Test
    fun doAction_doesSomething(){
        /* Given */
        val mock = mock<Context> {
            on { getText() } doReturn "text"
        }
        val classUnderTest = ClassUnderTest(mock)

        /* When */
        classUnderTest.doAction()

        /* Then */
        verify(mock).doSomething(any())
    }*/
}