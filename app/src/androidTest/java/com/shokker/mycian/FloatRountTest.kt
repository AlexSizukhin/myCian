package com.shokker.mycian

import com.shokker.mycian.UI.MyNumberController
import org.junit.Test
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert

class FloatRountTest {
    val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun testFormating() {
        val mnc = MyNumberController(context)
        mnc.apply {
            val f1 = 100.0f
            Assert.assertEquals("100", f1.toString(0))
            val f10 = 100.9f
            Assert.assertEquals("101", f10.toString(0))
            val f11 = 99.9f
            Assert.assertEquals("100", f11.toString(0))

            val f2 = 100.1f
            Assert.assertEquals("100.1", f2.toString(1))
            val f20 = 100.11f
            Assert.assertEquals("100.1", f20.toString(1))
            val f21 = 100.05f
            Assert.assertEquals( "100.1", f21.toString(1))

            val f3 = 101.1f
            Assert.assertEquals("100", f3.toString(-1))
            val f4 = 107.1f
            Assert.assertEquals("110", f4.toString(-1))
            val f5 = 107.15f
            Assert.assertEquals("107.2", f5.toString(1))
/*          // todo add group testing
            val f6 = 1023.12f
            Assert.assertEquals("1 023",f6.toString(0))*/
            // todo test negative
        }
    }
}