package javavlsu.kb.esap.esapmobile.core.domain.network.plugin

import io.ktor.client.plugins.api.*

val UserAgentInterceptorPlugin = createClientPlugin("UserAgentInterceptorPlugin") {
    onRequest { request, _ ->
        request.headers.append("User-Agent", "mobile")
    }
}