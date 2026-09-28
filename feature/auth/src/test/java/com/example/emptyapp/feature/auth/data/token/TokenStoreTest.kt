package com.example.emptyapp.feature.auth.data.token

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TokenStoreTest {

    private val cipher = FakeTokenCipher()
    private val dataSource = FakeTokenLocalDataSource()
    private val cache = InMemoryTokenCache()
    private val store = TokenStore(cipher = cipher, localDataSource = dataSource, cache = cache)

    @Test
    fun `save keeps plaintext in memory and ciphertext at rest`() {
        store.save("token-123")

        assertEquals("token-123", cache.currentAccessToken())
        assertEquals(EncryptedData(cipherText = "enc:token-123", iv = "iv"), dataSource.stored)
    }

    @Test
    fun `restore decrypts the persisted token into memory`() {
        dataSource.stored = EncryptedData(cipherText = "enc:token-123", iv = "iv")

        assertEquals("token-123", store.restore())
        assertEquals("token-123", cache.currentAccessToken())
    }

    @Test
    fun `restore returns null when nothing is persisted`() {
        assertNull(store.restore())
        assertNull(cache.currentAccessToken())
    }

    @Test
    fun `clear wipes both memory and storage`() {
        store.save("token-123")

        store.clear()

        assertNull(cache.currentAccessToken())
        assertNull(dataSource.stored)
    }

    private class FakeTokenCipher : TokenCipher {
        override fun encrypt(plainText: String) = EncryptedData(cipherText = "enc:$plainText", iv = "iv")
        override fun decrypt(data: EncryptedData) = data.cipherText.removePrefix("enc:")
    }

    private class FakeTokenLocalDataSource : TokenLocalDataSource {
        var stored: EncryptedData? = null
        override fun save(data: EncryptedData) {
            stored = data
        }

        override fun read(): EncryptedData? = stored
        override fun clear() {
            stored = null
        }
    }
}
