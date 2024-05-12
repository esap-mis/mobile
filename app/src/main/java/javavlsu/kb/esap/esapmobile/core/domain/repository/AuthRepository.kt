package javavlsu.kb.esap.esapmobile.core.domain.repository

import javavlsu.kb.esap.esapmobile.core.data.dao.UserDAO
import javavlsu.kb.esap.esapmobile.core.data.model.User
import javavlsu.kb.esap.esapmobile.core.domain.api.AuthApiService
import javavlsu.kb.esap.esapmobile.core.domain.dto.request.AuthRequest
import javavlsu.kb.esap.esapmobile.core.domain.util.apiRequestFlow
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val userDAO: UserDAO,
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

    suspend fun insertUser(user: User) {
        userDAO.insert(user)
    }
}