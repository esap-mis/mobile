package javavlsu.kb.esap.esapmobile.domain.repository

import javavlsu.kb.esap.esapmobile.domain.AuthApiService
import javavlsu.kb.esap.esapmobile.domain.model.AuthRequest
import javavlsu.kb.esap.esapmobile.domain.util.apiRequestFlow
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApiService: AuthApiService,
) {
    fun login(request: AuthRequest) = apiRequestFlow {
        authApiService.login(request)
    }
}