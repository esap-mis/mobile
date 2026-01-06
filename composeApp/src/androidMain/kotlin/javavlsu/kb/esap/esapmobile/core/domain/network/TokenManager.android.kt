package javavlsu.kb.esap.esapmobile.core.domain.network

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import javavlsu.kb.esap.esapmobile.core.domain.util.AppLogger
import org.koin.dsl.module

class AndroidTokenManager(
    private val context: Context,
    private val appLogger: AppLogger
) : TokenManager {
    private val settings: Settings by lazy {
        SharedPreferencesSettings(
            context.getSharedPreferences("auth_preferences", Context.MODE_PRIVATE)
        )
    }

    private val tokenManager by lazy { DefaultTokenManager(settings, appLogger) }

    override fun getToken(): String? = tokenManager.getToken()

    override suspend fun saveToken(token: String) = tokenManager.saveToken(token)

    override suspend fun deleteToken() = tokenManager.deleteToken()

    override fun getRefreshToken(): String? = tokenManager.getRefreshToken()

    override suspend fun saveRefreshToken(token: String) = tokenManager.saveRefreshToken(token)

    override suspend fun deleteRefreshToken() = tokenManager.deleteRefreshToken()

    override fun getRoles(): String? = tokenManager.getRoles()

    override suspend fun saveRoles(roles: String) = tokenManager.saveRoles(roles)

    override suspend fun deleteRoles() = tokenManager.deleteRoles()
}

actual val tokenManagerModule = module {
    single<TokenManager> {
        AndroidTokenManager(get(), get())
    }
}