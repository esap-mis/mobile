package javavlsu.kb.esap.esapmobile.core.domain.repository

import javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.core.domain.api.AuthApiService
import javavlsu.kb.esap.esapmobile.core.domain.model.request.AuthRequest
import javavlsu.kb.esap.esapmobile.core.domain.model.response.AuthResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.response.ServerStatusResponse

class AuthRepository(
    private val authApiService: AuthApiService,
) {
    suspend fun checkStatus(): ApiResponse<ServerStatusResponse> = authApiService.checkStatus()

    suspend fun login(request: AuthRequest): ApiResponse<AuthResponse> = authApiService.login(request)

    suspend fun resetPassword(request: AuthRequest): ApiResponse<String> = authApiService.resetPassword(request)
}