package javavlsu.kb.esap.esapmobile.core.di

import javavlsu.kb.esap.esapmobile.core.domain.api.AuthApiService
import javavlsu.kb.esap.esapmobile.core.domain.api.AuthApiServiceImpl
import javavlsu.kb.esap.esapmobile.core.domain.api.ChatApiService
import javavlsu.kb.esap.esapmobile.core.domain.api.ChatApiServiceImpl
import javavlsu.kb.esap.esapmobile.core.domain.api.MainApiService
import javavlsu.kb.esap.esapmobile.core.domain.api.MainApiServiceImpl
import javavlsu.kb.esap.esapmobile.core.domain.api.NotificationApiService
import javavlsu.kb.esap.esapmobile.core.domain.api.NotificationApiServiceImpl
import javavlsu.kb.esap.esapmobile.core.domain.network.NetworkClient
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule = module {

    single {
        NetworkClient(get(), get())
    }

    single(named("authHttpClient")) {
        get<NetworkClient>().createAuthHttpClient()
    }

    single(named("mainHttpClient")) {
        get<NetworkClient>().createMainHttpClient()
    }

    single<AuthApiService> {
        AuthApiServiceImpl(get(named("authHttpClient")))
    }

    single<MainApiService> {
        MainApiServiceImpl(get(named("mainHttpClient")))
    }

    single<NotificationApiService> {
        NotificationApiServiceImpl(get(named("mainHttpClient")))
    }

    single<ChatApiService> {
        ChatApiServiceImpl(get(named("mainHttpClient")))
    }
}