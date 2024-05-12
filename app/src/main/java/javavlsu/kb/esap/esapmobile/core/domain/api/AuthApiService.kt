package javavlsu.kb.esap.esapmobile.core.domain.api

import javavlsu.kb.esap.esapmobile.core.domain.dto.request.AuthRequest
import javavlsu.kb.esap.esapmobile.core.domain.dto.response.AuthResponse
import javavlsu.kb.esap.esapmobile.core.domain.dto.response.ServerStatusResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {
    @GET("actuator/health")
    suspend fun checkStatus(): Response<ServerStatusResponse>

    @POST("api/auth/login")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>

    @POST("api/auth/password/reset")
    suspend fun resetPassword(@Body request: AuthRequest): Response<String>

    @POST("api/auth/refresh")
    suspend fun refreshToken(@Body token: String): Response<AuthResponse>
}