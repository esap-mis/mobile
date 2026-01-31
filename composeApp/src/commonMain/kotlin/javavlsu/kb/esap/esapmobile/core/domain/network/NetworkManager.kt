package javavlsu.kb.esap.esapmobile.core.domain.network

import com.russhwolf.settings.Settings
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.module.Module

interface NetworkManager {
    val isDarkModeFlow: StateFlow<Boolean?>

    suspend fun getBaseUrl(): String
    suspend fun setBaseUrl(newBaseUrl: String)
    suspend fun getIsDarkMode(): Boolean?
    suspend fun setIsDarkMode(isDarkMode: Boolean?)

    companion object {
        const val BASE_URL_KEY = "base_url"
        const val DARK_MODE_KEY = "dark_mode"
        const val DEFAULT_BASE_URL = "http://192.168.0.105:8080"
    }
}

class DefaultNetworkManager(
    private val settings: Settings,
    private val defaultBaseUrl: String = NetworkManager.DEFAULT_BASE_URL
) : NetworkManager {

    private val _isDarkModeFlow = MutableStateFlow<Boolean?>(null)
    override val isDarkModeFlow: StateFlow<Boolean?> = _isDarkModeFlow.asStateFlow()

    init {
        _isDarkModeFlow.value = settings.getBooleanOrNull(NetworkManager.DARK_MODE_KEY)
    }

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

    override suspend fun getIsDarkMode(): Boolean? {
        return settings.getBooleanOrNull(NetworkManager.DARK_MODE_KEY)
    }

    override suspend fun setIsDarkMode(isDarkMode: Boolean?) {
        _isDarkModeFlow.value = isDarkMode
        if (isDarkMode == null) {
            settings.remove(NetworkManager.DARK_MODE_KEY)
        } else {
            settings.putBoolean(NetworkManager.DARK_MODE_KEY, isDarkMode)
        }
    }

    private suspend fun initializeDefaultBaseUrl(): String {
        setBaseUrl(defaultBaseUrl)
        return defaultBaseUrl
    }
}

expect val networkManagerModule: Module