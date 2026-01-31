package javavlsu.kb.esap.esapmobile.core.domain.network

import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.StateFlow
import org.koin.dsl.module
import java.io.File
import java.util.Properties
import kotlin.getValue

class DesktopNetworkManager : NetworkManager {
    private val settings: Settings by lazy { createDesktopSettings() }
    private val networkManager by lazy { DefaultNetworkManager(settings) }

    override suspend fun getBaseUrl(): String = networkManager.getBaseUrl()

    override suspend fun setBaseUrl(newBaseUrl: String) = networkManager.setBaseUrl(newBaseUrl)

    override suspend fun getIsDarkMode(): Boolean? = networkManager.getIsDarkMode()

    override suspend fun setIsDarkMode(isDarkMode: Boolean?) = networkManager.setIsDarkMode(isDarkMode)

    override val isDarkModeFlow: StateFlow<Boolean?>
        get() = networkManager.isDarkModeFlow

    private fun createDesktopSettings(): Settings {
        val userHome = System.getProperty("user.home")
        val appDir = File(userHome, ".esap")

        if (!appDir.exists()) {
            appDir.mkdirs()
        }

        val settingsFile = File(appDir, "network.properties")

        val properties = Properties().apply {
            if (settingsFile.exists()) {
                settingsFile.inputStream().use { load(it) }
            }

            if (!containsKey(NetworkManager.BASE_URL_KEY)) {
                setProperty(NetworkManager.BASE_URL_KEY, NetworkManager.DEFAULT_BASE_URL)
                settingsFile.outputStream().use { store(it, "ESAP Application Settings") }
            }
        }

        return AutoSaveFilePropertiesSettings(properties, settingsFile)
    }
}

actual val networkManagerModule = module {
    single<NetworkManager> { DesktopNetworkManager() }
}