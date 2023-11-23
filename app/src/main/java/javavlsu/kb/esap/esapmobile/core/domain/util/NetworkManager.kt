package javavlsu.kb.esap.esapmobile.core.domain.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import javavlsu.kb.esap.esapmobile.core.config.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NetworkManager(
    private val context: Context
) {
    companion object {
        private val BASE_URL_KEY = stringPreferencesKey("base_url")
    }

    fun getBaseUrl(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[BASE_URL_KEY] ?: initializeDefaultBaseUrl()
        }
    }

    suspend fun setBaseUrl(newBaseUrl: String) {
        context.dataStore.edit { preferences ->
            preferences[BASE_URL_KEY] = newBaseUrl
        }
    }

    private suspend fun initializeDefaultBaseUrl(): String {
        val defaultBaseUrl = "http://10.0.2.2:8080/"
        setBaseUrl(defaultBaseUrl)
        return defaultBaseUrl
    }
}
