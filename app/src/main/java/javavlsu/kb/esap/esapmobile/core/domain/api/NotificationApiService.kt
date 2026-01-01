package javavlsu.kb.esap.esapmobile.core.domain.api

import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import javavlsu.kb.esap.esapmobile.core.domain.model.request.TokenRequest
import javax.inject.Inject

interface INotificationApiService {
    suspend fun registerToken(request: TokenRequest): ApiResponse<String>
}

class NotificationApiService @Inject constructor(
    private val mainClient: HttpClient
) : BaseApiService(mainClient), INotificationApiService {

    override suspend fun registerToken(request: TokenRequest): ApiResponse<String> {
        return safeRequest {
            url("api/notification/token")
            method = HttpMethod.Post
            setBody(request)
        }
    }
}
