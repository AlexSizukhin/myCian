package com.shokker.mycian

import com.shokker.mycian.Model.ClusterMark
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito

@RunWith(JUnit4::class)
class MockExamples {
    @Test
    fun test1()
    {
        val mockMapActivity = Mockito.mock(MainContract.IMyMapActivity::class.java)
        val mockFlatResult = Mockito.mock(MainContract.IFlatResult::class.java)
        val mockFlatFilter = Mockito.mock(MainContract.IFlatFilter::class.java)
        //mockMapActivity. flatResult = mockFlatResult
        Mockito.`when`(mockMapActivity.flatResult).thenReturn(mockFlatResult)
        //Mockito.whenever()
        Mockito.`when`(mockMapActivity.flatFilter).thenReturn(mockFlatFilter)

        println(mockMapActivity.flatResult)
        println(mockMapActivity.flatFilter)

        val x: (ClusterMark)->Unit =  { }

        mockMapActivity.setOnMarkClicked(x)
        Mockito.verify(mockMapActivity).setOnMarkClicked(x)
    }
}