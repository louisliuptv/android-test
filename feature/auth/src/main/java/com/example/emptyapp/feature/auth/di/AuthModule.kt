package com.example.emptyapp.feature.auth.di

import com.example.emptyapp.core.network.token.TokenProvider
import com.example.emptyapp.feature.auth.data.remote.AuthApiService
import com.example.emptyapp.feature.auth.data.repository.AuthRepositoryImpl
import com.example.emptyapp.feature.auth.data.token.AndroidKeystoreTokenCipher
import com.example.emptyapp.feature.auth.data.token.EncryptedTokenDataSource
import com.example.emptyapp.feature.auth.data.token.TokenCipher
import com.example.emptyapp.feature.auth.data.token.TokenLocalDataSource
import com.example.emptyapp.feature.auth.data.token.TokenStore
import com.example.emptyapp.feature.auth.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindTokenProvider(impl: TokenStore): TokenProvider

    @Binds
    @Singleton
    abstract fun bindTokenCipher(impl: AndroidKeystoreTokenCipher): TokenCipher

    @Binds
    @Singleton
    abstract fun bindTokenLocalDataSource(impl: EncryptedTokenDataSource): TokenLocalDataSource

    companion object {
        @Provides
        @Singleton
        fun provideAuthApiService(retrofit: Retrofit): AuthApiService =
            retrofit.create(AuthApiService::class.java)
    }
}
