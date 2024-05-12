package javavlsu.kb.esap.esapmobile.core.domain.api

import javavlsu.kb.esap.esapmobile.core.domain.model.response.ModelResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatApiService {
    @POST("api/chat")
    suspend fun sendMessage(@Body message: String): Response<ModelResponse>
}