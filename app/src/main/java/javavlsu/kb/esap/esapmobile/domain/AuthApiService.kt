package javavlsu.kb.esap.esapmobile.domain

import javavlsu.kb.esap.esapmobile.domain.model.AuthRequest
import javavlsu.kb.esap.esapmobile.domain.model.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>
}