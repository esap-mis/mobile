package javavlsu.kb.esap.esapmobile.core.domain.network

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
import javavlsu.kb.esap.esapmobile.core.domain.util.AppLogger
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit

class NetworkClient(
    private val networkManager: NetworkManager,
    private val tokenManager: TokenManager,
    private val appLogger: AppLogger
) {

    companion object {
        const val NETWORK_CLIENT_TAG = "NetworkClient"
        const val KTOR_TAG = "KtorClient"
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
                logger = object : Logger {
                    override fun log(message: String) {
                        appLogger.d(KTOR_TAG, message)
                    }
                }
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
                logger = object : Logger {
                    override fun log(message: String) {
                        appLogger.d(KTOR_TAG, message)
                    }
                }
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
                        appLogger.d(NETWORK_CLIENT_TAG, "loadTokens: accessToken=${accessToken.take(10)}, refreshToken=${refreshToken.take(10)}")
                        BearerTokens(
                            accessToken = accessToken,
                            refreshToken = refreshToken
                        )
                    }

                    refreshTokens {
                        appLogger.d(NETWORK_CLIENT_TAG, "refreshTokens: started")
                        val newToken = refreshToken()
                        if (newToken != null) {
                            appLogger.d(NETWORK_CLIENT_TAG, "refreshTokens: success, saving tokens")
                            tokenManager.saveToken(newToken.jwt)
                            tokenManager.saveRefreshToken(newToken.jwt)
                            BearerTokens(
                                accessToken = newToken.jwt,
                                refreshToken = newToken.jwt
                            )
                        } else {
                            appLogger.e(NETWORK_CLIENT_TAG, "refreshTokens: failed, clearing data")
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
        val refreshToken = tokenManager.getRefreshToken()
        appLogger.d(NETWORK_CLIENT_TAG, "refreshToken: refreshToken=$refreshToken")
        if (refreshToken.isNullOrBlank()) {
            appLogger.e(NETWORK_CLIENT_TAG, "refreshToken: token is null or blank")
            return null
        }
        return try {
            val client = createAuthHttpClient()
            val currentBaseUrl = networkManager.getBaseUrl()
            appLogger.d(NETWORK_CLIENT_TAG, "refreshToken: calling ${currentBaseUrl}api/auth/refresh")
            val response = client.post {
                url("${currentBaseUrl}api/auth/refresh")
                header("Authorization", "Bearer $refreshToken")
            }
            appLogger.d(NETWORK_CLIENT_TAG, "refreshToken: response status=${response.status}")
            val authResponse: AuthResponse = response.body()
            authResponse
        } catch (e: Exception) {
            appLogger.e(NETWORK_CLIENT_TAG, "Failed to refresh token: ${e.message}", e)
            null
        }
    }
}