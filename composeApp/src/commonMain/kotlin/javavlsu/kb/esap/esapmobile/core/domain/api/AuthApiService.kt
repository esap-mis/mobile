package javavlsu.kb.esap.esapmobile.core.domain.api

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import javavlsu.kb.esap.esapmobile.core.domain.model.request.AuthRequest
import javavlsu.kb.esap.esapmobile.core.domain.model.response.AuthResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.response.ServerStatusResponse

interface IAuthApiService {
    suspend fun checkStatus(): ApiResponse<ServerStatusResponse>
    suspend fun login(request: AuthRequest): ApiResponse<AuthResponse>
    suspend fun resetPassword(request: AuthRequest): ApiResponse<String>
    suspend fun refreshToken(refreshToken: String): ApiResponse<AuthResponse>
}

class AuthApiService(
    private val authClient: HttpClient
) : BaseApiService(authClient),
    IAuthApiService {

    override suspend fun checkStatus(): ApiResponse<ServerStatusResponse> {
        return safeRequest {
            url("actuator/health")
            method = HttpMethod.Get
        }
    }

    override suspend fun login(request: AuthRequest): ApiResponse<AuthResponse> {
        return safeRequest {
            url("api/auth/login")
            method = HttpMethod.Post
            setBody(request)
        }
    }

    override suspend fun resetPassword(request: AuthRequest): ApiResponse<String> {
        return safeRequest {
            url("api/auth/password/reset")
            method = HttpMethod.Post
            setBody(request)
        }
    }

    override suspend fun refreshToken(refreshToken: String): ApiResponse<AuthResponse> {
        return safeRequest {
            url("api/auth/refresh")
            method = HttpMethod.Post
            header("Authorization", "Bearer $refreshToken")
        }
    }
}