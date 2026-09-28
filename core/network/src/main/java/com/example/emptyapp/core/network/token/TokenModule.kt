package com.example.emptyapp.core.network.token

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Placeholder token bindings. `:feature:auth` replaces these with the secure
 * Keystore-backed implementations in Phase 5.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class TokenModule {

    @Binds
    @Singleton
    abstract fun bindTokenProvider(impl: PlaceholderTokenProvider): TokenProvider

    @Binds
    @Singleton
    abstract fun bindSessionManager(impl: PlaceholderSessionManager): SessionManager
}
