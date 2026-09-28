package com.example.emptyapp.feature.auth.data.token

import com.example.emptyapp.core.network.token.TokenProvider
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Owns the access token using just two tiers:
 * 1. [AndroidKeystoreTokenCipher] encrypts/decrypts with a hardware-backed key.
 * 2. [InMemoryTokenCache] holds the plaintext token for the current session.
 *
 * The ciphertext is persisted via [TokenLocalDataSource] and restored into the
 * cache on creation. There is no separate session state: the token either
 * exists or it does not.
 */
@Singleton
class TokenStore @Inject constructor(
    private val cipher: TokenCipher,
    private val localDataSource: TokenLocalDataSource,
    private val cache: InMemoryTokenCache,
) : TokenProvider {

    init {
        restore()
    }

    override fun currentAccessToken(): String? = cache.currentAccessToken()

    fun isSignedIn(): Boolean = !currentAccessToken().isNullOrBlank()

    fun save(accessToken: String) {
        require(accessToken.isNotBlank()) { "Access token must not be blank" }
        cache.update(accessToken)
        localDataSource.save(cipher.encrypt(accessToken))
    }

    /** Loads the persisted token into memory; returns null when absent/corrupt. */
    fun restore(): String? {
        val token = localDataSource.read()?.let(cipher::decrypt)?.takeIf { it.isNotBlank() }
        cache.update(token)
        return token
    }

    fun clear() {
        cache.update(null)
        localDataSource.clear()
    }
}
