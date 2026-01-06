package javavlsu.kb.esap.esapmobile.core.domain.network

import com.russhwolf.settings.Settings
import javavlsu.kb.esap.esapmobile.core.domain.util.AppLogger
import org.koin.core.module.Module

interface TokenManager {
    fun getToken(): String?
    suspend fun saveToken(token: String)
    suspend fun deleteToken()

    fun getRefreshToken(): String?
    suspend fun saveRefreshToken(token: String)
    suspend fun deleteRefreshToken()

    fun getRoles(): String?
    suspend fun saveRoles(roles: String)
    suspend fun deleteRoles()

    companion object {
        const val TAG = "TokenManager"
        const val TOKEN_KEY = "jwt_token"
        const val REFRESH_TOKEN_KEY = "refresh_token"
        const val ROLES_KEY = "roles"
    }
}

class DefaultTokenManager(
    private val settings: Settings,
    private val appLogger: AppLogger
) : TokenManager {

    override fun getToken(): String? {
        return settings.getStringOrNull(TokenManager.TOKEN_KEY)
    }

    override suspend fun saveToken(token: String) {
        appLogger.i(TokenManager.TAG, "Saving token: $token")
        settings.putString(TokenManager.TOKEN_KEY, token)
    }

    override suspend fun deleteToken() {
        appLogger.i(TokenManager.TAG, "Deleting token")
        settings.remove(TokenManager.TOKEN_KEY)
    }

    override fun getRefreshToken(): String? {
        return settings.getStringOrNull(TokenManager.REFRESH_TOKEN_KEY)
    }

    override suspend fun saveRefreshToken(token: String) {
        appLogger.i(TokenManager.TAG,"Saving refresh token: $token")
        settings.putString(TokenManager.REFRESH_TOKEN_KEY, token)
    }

    override suspend fun deleteRefreshToken() {
        appLogger.i(TokenManager.TAG, "Deleting refresh token")
        settings.remove(TokenManager.REFRESH_TOKEN_KEY)
    }

    override fun getRoles(): String? {
        return settings.getStringOrNull(TokenManager.ROLES_KEY)
    }

    override suspend fun saveRoles(roles: String) {
        appLogger.i(TokenManager.TAG, "Saving roles: $roles")
        settings.putString(TokenManager.ROLES_KEY, roles)
    }

    override suspend fun deleteRoles() {
        appLogger.i(TokenManager.TAG, "Deleting roles")
        settings.remove(TokenManager.ROLES_KEY)
    }
}

expect val tokenManagerModule: Module