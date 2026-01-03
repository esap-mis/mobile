package javavlsu.kb.esap.esapmobile.core.domain.api

import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import javavlsu.kb.esap.esapmobile.core.domain.model.request.AuthRequest
import javavlsu.kb.esap.esapmobile.core.domain.model.response.AuthResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.response.ServerStatusResponse
import javax.inject.Inject

interface IAuthApiService {
    suspend fun checkStatus(): javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse<ServerStatusResponse>
    suspend fun login(request: AuthRequest): javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse<AuthResponse>
    suspend fun resetPassword(request: AuthRequest): javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse<String>
    suspend fun refreshToken(refreshToken: String): javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse<AuthResponse>
}

class AuthApiService @Inject constructor(
    private val authClient: HttpClient
) : javavlsu.kb.esap.esapmobile.core.domain.api.BaseApiService(authClient),
    javavlsu.kb.esap.esapmobile.core.domain.api.IAuthApiService {

    override suspend fun checkStatus(): javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse<ServerStatusResponse> {
        return safeRequest {
            url("actuator/health")
            method = HttpMethod.Get
        }
    }

    override suspend fun login(request: AuthRequest): javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse<AuthResponse> {
        return safeRequest {
            url("api/auth/login")
            method = HttpMethod.Post
            setBody(request)
        }
    }

    override suspend fun resetPassword(request: AuthRequest): javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse<String> {
        return safeRequest {
            url("api/auth/password/reset")
            method = HttpMethod.Post
            setBody(request)
        }
    }

    override suspend fun refreshToken(refreshToken: String): javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse<AuthResponse> {
        return safeRequest {
            url("api/auth/refresh")
            method = HttpMethod.Post
            header("Authorization", "Bearer $refreshToken")
        }
    }
}