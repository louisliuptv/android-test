package com.example.emptyapp.core.designsystem.di

import com.example.emptyapp.core.designsystem.text.DefaultErrorMessageProvider
import com.example.emptyapp.core.designsystem.text.ErrorMessageProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DesignSystemModule {

    @Provides
    @Singleton
    fun provideErrorMessageProvider(): ErrorMessageProvider = DefaultErrorMessageProvider
}
