package com.example.apollographqlplayground

import com.example.apollographqlplayground.apollo.SeriesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// 3/7/2022

@Module
@InstallIn(SingletonComponent::class)
object RickModule {

    @Provides
    @Singleton
    fun provideWebServices() = SeriesApi()

    @Provides
    @Singleton
    fun provideApolloClient(api: SeriesApi) = api.getApolloClient()
}