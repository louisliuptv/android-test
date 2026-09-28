package com.example.emptyapp.feature.auth.data.token

import javax.inject.Inject

/**
 * Coordinates the three token tiers:
 * 1. [AndroidKeystoreTokenCipher] encrypts/decrypts with a hardware-backed key.
 * 2. [TokenLocalDataSource] persists the ciphertext + IV in encrypted prefs.
 * 3. [InMemoryTokenCache] holds the plaintext token for the current session.
 */
class TokenStore @Inject constructor(
    private val cipher: TokenCipher,
    private val localDataSource: TokenLocalDataSource,
    private val cache: InMemoryTokenCache,
) {

    fun save(accessToken: String) {
        cache.update(accessToken)
        localDataSource.save(cipher.encrypt(accessToken))
    }

    /** Loads the persisted token into memory; returns null when absent/corrupt. */
    fun restore(): String? {
        val token = localDataSource.read()?.let(cipher::decrypt)
        cache.update(token)
        return token
    }

    fun clear() {
        cache.update(null)
        localDataSource.clear()
    }
}
