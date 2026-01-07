package javavlsu.kb.esap.esapmobile.core.domain.network

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import javavlsu.kb.esap.esapmobile.core.domain.model.response.AuthResponse
import javavlsu.kb.esap.esapmobile.core.domain.network.plugin.UserAgentInterceptorPlugin
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit

class NetworkClient(
    private val networkManager: NetworkManager,
    private val tokenManager: TokenManager,
) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }

    fun createAuthHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(json)
            }

            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }

            install(HttpTimeout) {
                connectTimeoutMillis = TimeUnit.SECONDS.toMillis(10)
                requestTimeoutMillis = TimeUnit.SECONDS.toMillis(30)
                socketTimeoutMillis = TimeUnit.SECONDS.toMillis(30)
            }

            install(UserAgentInterceptorPlugin)

            defaultRequest {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                val currentBaseUrl = runBlocking { networkManager.getBaseUrl() }
                url(currentBaseUrl)
            }
        }
    }

    fun createMainHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(json)
            }

            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }

            install(HttpTimeout) {
                connectTimeoutMillis = TimeUnit.SECONDS.toMillis(10)
                requestTimeoutMillis = TimeUnit.SECONDS.toMillis(30)
                socketTimeoutMillis = TimeUnit.SECONDS.toMillis(30)
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        val accessToken = tokenManager.getToken() ?: ""
                        val refreshToken = tokenManager.getRefreshToken() ?: ""
                        BearerTokens(
                            accessToken = accessToken,
                            refreshToken = refreshToken
                        )
                    }

                    refreshTokens {
                        logger.debug { "refreshTokens: started" }
                        val newToken = refreshToken()
                        if (newToken != null) {
                            logger.debug { "refreshTokens: success, saving tokens" }
                            tokenManager.saveToken(newToken.jwt)
                            tokenManager.saveRefreshToken(newToken.jwt)
                            BearerTokens(
                                accessToken = newToken.jwt,
                                refreshToken = newToken.jwt
                            )
                        } else {
                            logger.debug { "refreshTokens: failed, clearing data" }
                            tokenManager.deleteToken()
                            tokenManager.deleteRefreshToken()
                            tokenManager.deleteRoles()
                            null
                        }
                    }
                }
            }

            install(UserAgentInterceptorPlugin)

            defaultRequest {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                val currentBaseUrl = runBlocking { networkManager.getBaseUrl() }
                url(currentBaseUrl)
            }
        }
    }

    private suspend fun refreshToken(): AuthResponse? {
        val token = tokenManager.getToken()
        if (token.isNullOrBlank()) {
            logger.debug { "refreshToken: token is null or blank" }
            return null
        }
        return try {
            val client = createAuthHttpClient()
            val currentBaseUrl = networkManager.getBaseUrl()
            logger.debug { "refreshToken: calling ${currentBaseUrl}api/auth/refresh" }
            val response = client.post {
                url("${currentBaseUrl}api/auth/refresh")
                header("Authorization", "Bearer $token")
            }
            logger.debug { "refreshToken: response status=${response.status}" }
            val authResponse: AuthResponse = response.body()
            authResponse
        } catch (e: Exception) {
            logger.error(e) { "Failed to refresh token: ${e.message}" }
            null
        }
    }
}