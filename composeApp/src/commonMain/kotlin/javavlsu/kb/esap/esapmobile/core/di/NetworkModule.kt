package javavlsu.kb.esap.esapmobile.core.di

import javavlsu.kb.esap.esapmobile.core.domain.api.AuthApiService
import javavlsu.kb.esap.esapmobile.core.domain.api.ChatApiService
import javavlsu.kb.esap.esapmobile.core.domain.api.MainApiService
import javavlsu.kb.esap.esapmobile.core.domain.api.NotificationApiService
import javavlsu.kb.esap.esapmobile.core.domain.network.NetworkClient
import javavlsu.kb.esap.esapmobile.core.domain.network.NetworkManager
import javavlsu.kb.esap.esapmobile.core.domain.util.*
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule = module {

    single { NetworkClient(get(), get(), get()) }

    single(named("authHttpClient")) {
        get<NetworkClient>().createAuthHttpClient()
    }

    single(named("mainHttpClient")) {
        get<NetworkClient>().createMainHttpClient()
    }

    single<AuthApiService> {
        AuthApiService(get(named("authHttpClient")))
    }

    single<MainApiService> {
        MainApiService(get(named("mainHttpClient")))
    }

    single<NotificationApiService> {
        NotificationApiService(get(named("mainHttpClient")))
    }

    single<ChatApiService> {
        ChatApiService(get(named("mainHttpClient")))
    }

//    single { ChatHistoryStore(androidContext()) }
}