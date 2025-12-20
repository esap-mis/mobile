package javavlsu.kb.esap.esapmobile.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import javavlsu.kb.esap.esapmobile.core.domain.api.AuthApiService
import javavlsu.kb.esap.esapmobile.core.domain.api.ChatApiService
import javavlsu.kb.esap.esapmobile.core.domain.api.MainApiService
import javavlsu.kb.esap.esapmobile.core.domain.api.NotificationApiService
import javavlsu.kb.esap.esapmobile.core.domain.util.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data_store")

val networkModule = module {
    single { TokenManager(androidContext()) }
    single { NetworkManager(androidContext()) }
    single { TokenManager(androidContext()) }

    single { AuthInterceptor(get()) }
    single { UserAgentInterceptor("mobile") }
    single { BaseUrlInterceptor(get()) }
    single { TimeoutInterceptor() }
    single { AuthAuthenticator(get(), get()) }

    single(named("authOkHttpClient")) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(get<UserAgentInterceptor>())
            .addInterceptor(get<BaseUrlInterceptor>())
            .build()
    }

    single(named("mainOkHttpClient")) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        OkHttpClient.Builder()
            .addInterceptor(get<UserAgentInterceptor>())
            .addInterceptor(get<BaseUrlInterceptor>())
            .addInterceptor(get<AuthInterceptor>())
            .addInterceptor(loggingInterceptor)
            .addInterceptor(get<TimeoutInterceptor>())
            .authenticator(get<AuthAuthenticator>())
            .build()
    }

    single {
        val networkManager = get<NetworkManager>()
        val baseUrl = runBlocking {
            networkManager.getBaseUrl().first()
        }
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
    }

    single<AuthApiService> {
        val retrofit = Retrofit.Builder()
            .baseUrl(runBlocking { get<NetworkManager>().getBaseUrl().first() })
            .addConverterFactory(GsonConverterFactory.create())
            .client(get(named("authOkHttpClient")))
            .build()
        retrofit.create(AuthApiService::class.java)
    }

    single<MainApiService> {
        val retrofit = Retrofit.Builder()
            .baseUrl(runBlocking { get<NetworkManager>().getBaseUrl().first() })
            .addConverterFactory(GsonConverterFactory.create())
            .client(get(named("mainOkHttpClient")))
            .build()
        retrofit.create(MainApiService::class.java)
    }

    single<NotificationApiService> {
        val retrofit = Retrofit.Builder()
            .baseUrl(runBlocking { get<NetworkManager>().getBaseUrl().first() })
            .addConverterFactory(GsonConverterFactory.create())
            .client(get(named("mainOkHttpClient")))
            .build()
        retrofit.create(NotificationApiService::class.java)
    }

    single<ChatApiService> {
        val retrofit = Retrofit.Builder()
            .baseUrl(runBlocking { get<NetworkManager>().getBaseUrl().first() })
            .addConverterFactory(GsonConverterFactory.create())
            .client(get(named("mainOkHttpClient")))
            .build()
        retrofit.create(ChatApiService::class.java)
    }

    single { ChatHistoryStore(androidContext()) }
}