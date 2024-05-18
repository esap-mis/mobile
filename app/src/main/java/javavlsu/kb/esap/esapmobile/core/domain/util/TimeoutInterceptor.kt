package javavlsu.kb.esap.esapmobile.core.domain.util

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.util.concurrent.TimeUnit

const val CONNECT_TIMEOUT = "CONNECT_TIMEOUT"
const val READ_TIMEOUT = "READ_TIMEOUT"
const val WRITE_TIMEOUT = "WRITE_TIMEOUT"

class TimeoutInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()

        val connectTimeout =
            request.header(CONNECT_TIMEOUT)?.toInt() ?: chain.connectTimeoutMillis()
        val readTimeout = request.header(READ_TIMEOUT)?.toInt() ?: chain.readTimeoutMillis()
        val writeTimeout = request.header(WRITE_TIMEOUT)?.toInt() ?: chain.writeTimeoutMillis()

        return chain
            .withConnectTimeout(connectTimeout, TimeUnit.SECONDS)
            .withReadTimeout(readTimeout, TimeUnit.SECONDS)
            .withWriteTimeout(writeTimeout, TimeUnit.SECONDS)
            .proceed(request)
    }
}