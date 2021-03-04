package com.shokker.mycian.DI

import com.shokker.mycian.IFlowProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class FlowProviderModule
{
    @Provides
    fun provideFlowProvider():IFlowProvider
    {
        TODO()
    }
}

abstract class FlowProviderImpl:IFlowProvider
{

}