package javavlsu.kb.esap.esapmobile.core.domain.network

import com.russhwolf.settings.Settings
import io.github.oshai.kotlinlogging.KotlinLogging
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
        const val TOKEN_KEY = "jwt_token"
        const val REFRESH_TOKEN_KEY = "refresh_token"
        const val ROLES_KEY = "roles"
    }
}

class DefaultTokenManager(
    private val settings: Settings,
) : TokenManager {

    companion object {
        private val logger = KotlinLogging.logger {}
    }

    override fun getToken(): String? {
        return settings.getStringOrNull(TokenManager.TOKEN_KEY)
    }

    override suspend fun saveToken(token: String) {
        logger.info { "Saving token: $token" }
        settings.putString(TokenManager.TOKEN_KEY, token)
    }

    override suspend fun deleteToken() {
        logger.info { "Deleting token" }
        settings.remove(TokenManager.TOKEN_KEY)
    }

    override fun getRefreshToken(): String? {
        return settings.getStringOrNull(TokenManager.REFRESH_TOKEN_KEY)
    }

    override suspend fun saveRefreshToken(token: String) {
        logger.info { "Saving refresh token: $token" }
        settings.putString(TokenManager.REFRESH_TOKEN_KEY, token)
    }

    override suspend fun deleteRefreshToken() {
        logger.info { "Deleting refresh token" }
        settings.remove(TokenManager.REFRESH_TOKEN_KEY)
    }

    override fun getRoles(): String? {
        return settings.getStringOrNull(TokenManager.ROLES_KEY)
    }

    override suspend fun saveRoles(roles: String) {
        logger.info { "Saving roles: $roles" }
        settings.putString(TokenManager.ROLES_KEY, roles)
    }

    override suspend fun deleteRoles() {
        logger.info { "Deleting roles" }
        settings.remove(TokenManager.ROLES_KEY)
    }
}

expect val tokenManagerModule: Module