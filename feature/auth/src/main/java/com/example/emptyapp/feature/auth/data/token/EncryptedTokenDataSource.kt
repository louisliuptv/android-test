package com.example.emptyapp.feature.auth.data.token

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class EncryptedTokenDataSource @Inject constructor(
    @ApplicationContext context: Context,
) : TokenLocalDataSource {

    private val preferences: SharedPreferences by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        EncryptedSharedPreferences.create(
            context,
            FILE_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    override fun save(data: EncryptedData) {
        preferences.edit()
            .putString(KEY_CIPHER_TEXT, data.cipherText)
            .putString(KEY_IV, data.iv)
            .apply()
    }

    override fun read(): EncryptedData? {
        val cipherText = preferences.getString(KEY_CIPHER_TEXT, null) ?: return null
        val iv = preferences.getString(KEY_IV, null) ?: return null
        return EncryptedData(cipherText = cipherText, iv = iv)
    }

    override fun clear() {
        preferences.edit().clear().apply()
    }

    private companion object {
        const val FILE_NAME = "emptyapp_secure_tokens"
        const val KEY_CIPHER_TEXT = "access_token_cipher"
        const val KEY_IV = "access_token_iv"
    }
}
