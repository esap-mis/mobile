package javavlsu.kb.esap.esapmobile.core.domain.repository

import javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.core.domain.api.AuthApiService
import javavlsu.kb.esap.esapmobile.core.domain.model.request.AuthRequest
import javavlsu.kb.esap.esapmobile.core.domain.model.response.AuthResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.response.ServerStatusResponse
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApiService: AuthApiService,
) {
    suspend fun checkStatus(): ApiResponse<ServerStatusResponse> {
        return authApiService.checkStatus()
    }

    suspend fun login(request: AuthRequest): ApiResponse<AuthResponse> {
        return authApiService.login(request)
    }

    suspend fun resetPassword(request: AuthRequest): ApiResponse<String> {
        return authApiService.resetPassword(request)
    }
}