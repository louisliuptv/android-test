package com.example.emptyapp.feature.gps.di

import com.example.emptyapp.feature.gps.data.remote.GpsApiService
import com.example.emptyapp.feature.gps.data.repository.GpsRepositoryImpl
import com.example.emptyapp.feature.gps.domain.repository.GpsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class GpsModule {

    @Binds
    @Singleton
    abstract fun bindGpsRepository(impl: GpsRepositoryImpl): GpsRepository

    companion object {
        @Provides
        @Singleton
        fun provideGpsApiService(retrofit: Retrofit): GpsApiService =
            retrofit.create(GpsApiService::class.java)
    }
}
