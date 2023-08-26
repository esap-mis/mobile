package javavlsu.kb.esap.esapmobile.network

import javavlsu.kb.esap.esapmobile.network.model.AuthRequest
import javavlsu.kb.esap.esapmobile.network.model.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>
}