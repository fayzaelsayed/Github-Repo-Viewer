package com.example.gitrepoviewer.di

import com.example.gitrepoviewer.util.DefaultDispatcherProvider
import com.example.gitrepoviewer.util.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {

    @Singleton
    @Provides
    fun provideDispatchersProvider(): DispatcherProvider {
        return DefaultDispatcherProvider()
    }
}