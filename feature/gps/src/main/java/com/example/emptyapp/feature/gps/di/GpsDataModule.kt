package com.example.emptyapp.feature.gps.di

import com.example.emptyapp.feature.gps.data.repository.GpsRepositoryImpl
import com.example.emptyapp.feature.gps.domain.repository.GpsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GpsDataModule {

    @Binds
    @Singleton
    abstract fun bindGpsRepository(impl: GpsRepositoryImpl): GpsRepository
}
