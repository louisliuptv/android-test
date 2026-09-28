package com.example.emptyapp.feature.auth.di

import com.example.emptyapp.core.network.token.SessionManager
import com.example.emptyapp.core.network.token.TokenProvider
import com.example.emptyapp.feature.auth.data.repository.AuthRepositoryImpl
import com.example.emptyapp.feature.auth.data.session.SessionManagerImpl
import com.example.emptyapp.feature.auth.data.token.AndroidKeystoreTokenCipher
import com.example.emptyapp.feature.auth.data.token.EncryptedTokenDataSource
import com.example.emptyapp.feature.auth.data.token.InMemoryTokenCache
import com.example.emptyapp.feature.auth.data.token.TokenCipher
import com.example.emptyapp.feature.auth.data.token.TokenLocalDataSource
import com.example.emptyapp.feature.auth.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthDataModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindTokenProvider(impl: InMemoryTokenCache): TokenProvider

    @Binds
    @Singleton
    abstract fun bindSessionManager(impl: SessionManagerImpl): SessionManager

    @Binds
    @Singleton
    abstract fun bindTokenCipher(impl: AndroidKeystoreTokenCipher): TokenCipher

    @Binds
    @Singleton
    abstract fun bindTokenLocalDataSource(impl: EncryptedTokenDataSource): TokenLocalDataSource
}
