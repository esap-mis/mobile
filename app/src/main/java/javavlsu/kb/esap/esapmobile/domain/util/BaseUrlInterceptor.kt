package javavlsu.kb.esap.esapmobile.domain.util

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class BaseUrlInterceptor @Inject constructor(
    private val networkManager: NetworkManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val baseUrl = runBlocking {
            networkManager.getBaseUrl().first()
        }
        val newUrl = request.url.newBuilder()
            .host(baseUrl.toHttpUrlOrNull()?.host ?: "")
            .build()
        request = request.newBuilder()
            .url(newUrl)
            .build()
        return chain.proceed(request)
    }
}
