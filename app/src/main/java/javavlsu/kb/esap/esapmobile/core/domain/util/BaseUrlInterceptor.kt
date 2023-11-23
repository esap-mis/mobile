package javavlsu.kb.esap.esapmobile.core.domain.util

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class BaseUrlInterceptor @Inject constructor(
    private val networkManager: NetworkManager
) : Interceptor {

    private var customBaseUrl: String? = null

    fun setCustomBaseUrl(url: String) {
        customBaseUrl = url
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val baseUrl = customBaseUrl ?: runBlocking {
            networkManager.getBaseUrl().first()
        }
        val newUrl = request.url.newBuilder()
            .host(baseUrl.toHttpUrl().host)
            .build()
        request = request.newBuilder()
            .url(newUrl)
            .build()
        return chain.proceed(request)
    }
}
