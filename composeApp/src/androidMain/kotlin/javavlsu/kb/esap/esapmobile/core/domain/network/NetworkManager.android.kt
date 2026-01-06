package javavlsu.kb.esap.esapmobile.core.domain.network

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
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
}

actual val networkManagerModule = module {
    single<NetworkManager> { AndroidNetworkManager(get()) }
}