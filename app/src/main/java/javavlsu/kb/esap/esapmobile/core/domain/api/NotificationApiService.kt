package javavlsu.kb.esap.esapmobile.core.domain.api

import javavlsu.kb.esap.esapmobile.core.domain.model.request.TokenRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NotificationApiService {
    @POST("api/notification/token")
    suspend fun registerToken(@Body request: TokenRequest): Response<String>
}