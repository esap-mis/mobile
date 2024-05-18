package javavlsu.kb.esap.esapmobile.core.domain.api

import javavlsu.kb.esap.esapmobile.core.domain.model.response.ModelResponse
import javavlsu.kb.esap.esapmobile.core.domain.util.CONNECT_TIMEOUT
import javavlsu.kb.esap.esapmobile.core.domain.util.READ_TIMEOUT
import javavlsu.kb.esap.esapmobile.core.domain.util.WRITE_TIMEOUT
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ChatApiService {
    @POST("api/chat")
    @Headers("$CONNECT_TIMEOUT:60", "$READ_TIMEOUT:300", "$WRITE_TIMEOUT:30")
    suspend fun sendMessage(@Body message: String): Response<ModelResponse>
}