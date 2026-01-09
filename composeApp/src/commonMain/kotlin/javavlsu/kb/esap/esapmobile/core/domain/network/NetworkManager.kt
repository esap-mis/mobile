package javavlsu.kb.esap.esapmobile.core.domain.network

import com.russhwolf.settings.Settings
import io.github.oshai.kotlinlogging.KotlinLogging
import org.koin.core.module.Module

interface NetworkManager {
    suspend fun getBaseUrl(): String
    suspend fun setBaseUrl(newBaseUrl: String)

    companion object {
        const val BASE_URL_KEY = "base_url"
        const val DEFAULT_BASE_URL = "http://192.168.0.105:8080"
    }
}

class DefaultNetworkManager(
    private val settings: Settings,
    private val defaultBaseUrl: String = NetworkManager.DEFAULT_BASE_URL
) : NetworkManager {

    companion object {
        private val logger = KotlinLogging.logger {}
    }

    override suspend fun getBaseUrl(): String {
        val baseUrl = settings.getStringOrNull(NetworkManager.BASE_URL_KEY)
            ?: initializeDefaultBaseUrl()
        logger.info { "Get base_url: $baseUrl" }
        return baseUrl
    }

    override suspend fun setBaseUrl(newBaseUrl: String) {
        logger.info { "Saving base_url: $newBaseUrl" }
        settings.putString(NetworkManager.BASE_URL_KEY, newBaseUrl)
    }

    private suspend fun initializeDefaultBaseUrl(): String {
        setBaseUrl(defaultBaseUrl)
        return defaultBaseUrl
    }
}

expect val networkManagerModule: Module