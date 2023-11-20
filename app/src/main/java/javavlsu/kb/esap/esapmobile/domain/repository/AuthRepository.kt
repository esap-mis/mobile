package javavlsu.kb.esap.esapmobile.domain.repository

import javavlsu.kb.esap.esapmobile.domain.api.AuthApiService
import javavlsu.kb.esap.esapmobile.domain.model.request.AuthRequest
import javavlsu.kb.esap.esapmobile.domain.util.apiRequestFlow
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApiService: AuthApiService,
) {
    fun checkStatus() = apiRequestFlow {
        authApiService.checkStatus()
    }

    fun login(request: AuthRequest) = apiRequestFlow {
        authApiService.login(request)
    }

    fun resetPassword(request: AuthRequest) = apiRequestFlow {
        authApiService.resetPassword(request)
    }
}