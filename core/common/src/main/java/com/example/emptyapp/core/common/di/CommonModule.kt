package com.example.emptyapp.core.common.di

import com.example.emptyapp.core.common.dispatcher.DefaultDispatcherProvider
import com.example.emptyapp.core.common.dispatcher.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {

    @Provides
    @Singleton
    fun provideDispatcherProvider(impl: DefaultDispatcherProvider): DispatcherProvider = impl
}
