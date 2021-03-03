package com.shokker.mycian


import android.content.Context
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.shokker.mycian.UI.MyNumberController
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock

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