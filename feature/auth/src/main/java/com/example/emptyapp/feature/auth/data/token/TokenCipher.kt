package com.example.emptyapp.feature.auth.data.token

/** A ciphertext plus the initialization vector needed to decrypt it. */
data class EncryptedData(
    val cipherText: String,
    val iv: String,
)

/** Encrypts/decrypts the access token using a hardware-backed key. */
interface TokenCipher {
    fun encrypt(plainText: String): EncryptedData
    fun decrypt(data: EncryptedData): String?
}
