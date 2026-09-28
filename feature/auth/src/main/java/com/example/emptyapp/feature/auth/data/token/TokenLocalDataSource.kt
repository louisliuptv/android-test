package com.example.emptyapp.feature.auth.data.token

/**
 * Persistence boundary for the encrypted token. Stored in encrypted shared
 * preferences so the ciphertext and IV are themselves protected at rest.
 */
interface TokenLocalDataSource {
    fun save(data: EncryptedData)
    fun read(): EncryptedData?
    fun clear()
}
