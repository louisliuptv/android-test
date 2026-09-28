package com.example.emptyapp.core.common.ui

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ErrorMessageModule {

    @Provides
    @Singleton
    fun provideErrorMessageProvider(): ErrorMessageProvider = DefaultErrorMessageProvider
}
