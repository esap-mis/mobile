package javavlsu.kb.esap.esapmobile.core.domain.network

import com.russhwolf.settings.PropertiesSettings
import com.russhwolf.settings.Settings
import org.koin.dsl.module
import java.io.File
import java.util.Properties

class DesktopTokenManager : TokenManager {

    private val settings: Settings by lazy { createDesktopSettings() }

    private val tokenManager by lazy { DefaultTokenManager(settings) }

    override fun getToken(): String? = tokenManager.getToken()

    override suspend fun saveToken(token: String) = tokenManager.saveToken(token)

    override suspend fun deleteToken() = tokenManager.deleteToken()

    override fun getRefreshToken(): String? = tokenManager.getRefreshToken()

    override suspend fun saveRefreshToken(token: String) = tokenManager.saveRefreshToken(token)

    override suspend fun deleteRefreshToken() = tokenManager.deleteRefreshToken()

    override fun getRoles(): String? = tokenManager.getRoles()

    override suspend fun saveRoles(roles: String) = tokenManager.saveRoles(roles)

    override suspend fun deleteRoles() = tokenManager.deleteRoles()

    private fun createDesktopSettings(): PropertiesSettings {
        val userHome = System.getProperty("user.home")
        val appDir = File(userHome, ".esap")

        if (!appDir.exists()) {
            appDir.mkdirs()
        }

        val authProperties = File(appDir, "auth.properties")

        val properties = Properties().apply {
            if (authProperties.exists()) {
                authProperties.inputStream().use { load(it) }
            }
        }

        return PropertiesSettings(properties)
    }
}

actual val tokenManagerModule = module {
    single<TokenManager> { DesktopTokenManager() }
}