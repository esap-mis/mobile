package javavlsu.kb.esap.esapmobile.core.domain.network

import android.util.Log
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.rootCause
import javavlsu.kb.esap.esapmobile.R
import javavlsu.kb.esap.esapmobile.core.domain.model.response.AuthResponse
import javavlsu.kb.esap.esapmobile.core.domain.network.plugin.UserAgentInterceptorPlugin
import javavlsu.kb.esap.esapmobile.core.domain.util.NetworkManager
import javavlsu.kb.esap.esapmobile.core.domain.util.TokenManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NetworkClient @Inject constructor(
    private val networkManager: NetworkManager,
    private val tokenManager: TokenManager
) {

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
                        Log.d("Ktor", message)
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

            // Базовая конфигурация
            defaultRequest {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                val currentBaseUrl = runBlocking { networkManager.getBaseUrl().first() }
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
                        Log.d("Ktor", message)
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
                        val accessToken = tokenManager.getToken().first() ?: ""
                        val refreshToken = tokenManager.getRefreshToken().first() ?: ""
                        Log.d("NetworkClient", "loadTokens: accessToken=${accessToken.take(10)}, refreshToken=${refreshToken.take(10)}")
                        BearerTokens(
                            accessToken = accessToken,
                            refreshToken = refreshToken
                        )
                    }

                    refreshTokens {
                        Log.d("NetworkClient", "refreshTokens: started")
                        val newToken = refreshToken()
                        if (newToken != null) {
                            Log.d("NetworkClient", "refreshTokens: success, saving tokens")
                            tokenManager.saveToken(newToken.jwt)
                            tokenManager.saveRefreshToken(newToken.jwt)
                            BearerTokens(
                                accessToken = newToken.jwt,
                                refreshToken = newToken.jwt
                            )
                        } else {
                            Log.e("NetworkClient", "refreshTokens: failed, clearing data")
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
                val currentBaseUrl = runBlocking { networkManager.getBaseUrl().first() }
                url(currentBaseUrl)
            }
        }
    }

    private suspend fun refreshToken(): AuthResponse? {
        val refreshToken = tokenManager.getRefreshToken().first()
        Log.d("NetworkClient", "refreshToken: refreshToken=$refreshToken")
        if (refreshToken.isNullOrBlank()) {
            Log.e("NetworkClient", "refreshToken: token is null or blank")
            return null
        }
        return try {
            val client = createAuthHttpClient()
            val currentBaseUrl = networkManager.getBaseUrl().first()
            Log.d("NetworkClient", "refreshToken: calling ${currentBaseUrl}api/auth/refresh")
            val response = client.post {
                url("${currentBaseUrl}api/auth/refresh")
                header("Authorization", "Bearer $refreshToken")
            }
            Log.d("NetworkClient", "refreshToken: response status=${response.status}")
            val authResponse: AuthResponse = response.body()
            authResponse
        } catch (e: Exception) {
            Log.e("NetworkClient", "Failed to refresh token: ${e.message}", e)
            null
        }
    }
}