package javavlsu.kb.esap.esapmobile.core.domain.network

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import kotlinx.coroutines.flow.StateFlow
import org.koin.dsl.module

class AndroidNetworkManager(
    private val context: Context
) : NetworkManager {

    private val settings: Settings by lazy {
        SharedPreferencesSettings(
            context.getSharedPreferences("network_preferences", Context.MODE_PRIVATE)
        )
    }

    private val networkManager by lazy { DefaultNetworkManager(settings) }

    override suspend fun getBaseUrl(): String = networkManager.getBaseUrl()

    override suspend fun setBaseUrl(newBaseUrl: String) = networkManager.setBaseUrl(newBaseUrl)

    override suspend fun getIsDarkMode(): Boolean? = networkManager.getIsDarkMode()

    override suspend fun setIsDarkMode(isDarkMode: Boolean?) = networkManager.setIsDarkMode(isDarkMode)

    override val isDarkModeFlow: StateFlow<Boolean?>
        get() = networkManager.isDarkModeFlow
}

actual val networkManagerModule = module {
    single<NetworkManager> { AndroidNetworkManager(get()) }
}