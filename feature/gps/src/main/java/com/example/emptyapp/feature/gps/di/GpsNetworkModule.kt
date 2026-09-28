package com.example.emptyapp.feature.gps.di

import com.example.emptyapp.feature.gps.data.remote.GpsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object GpsNetworkModule {

    @Provides
    @Singleton
    fun provideGpsApiService(retrofit: Retrofit): GpsApiService =
        retrofit.create(GpsApiService::class.java)
}
