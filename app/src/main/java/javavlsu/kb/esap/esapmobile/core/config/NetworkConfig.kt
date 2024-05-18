package javavlsu.kb.esap.esapmobile.core.config

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javavlsu.kb.esap.esapmobile.core.domain.api.AuthApiService
import javavlsu.kb.esap.esapmobile.core.domain.api.ChatApiService
import javavlsu.kb.esap.esapmobile.core.domain.api.MainApiService
import javavlsu.kb.esap.esapmobile.core.domain.api.NotificationApiService
import javavlsu.kb.esap.esapmobile.core.domain.util.AuthAuthenticator
import javavlsu.kb.esap.esapmobile.core.domain.util.AuthInterceptor
import javavlsu.kb.esap.esapmobile.core.domain.util.BaseUrlInterceptor
import javavlsu.kb.esap.esapmobile.core.domain.util.ChatHistoryStore
import javavlsu.kb.esap.esapmobile.core.domain.util.NetworkManager
import javavlsu.kb.esap.esapmobile.core.domain.util.TokenManager
import javavlsu.kb.esap.esapmobile.core.domain.util.UserAgentInterceptor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data_store")

@Module
@InstallIn(SingletonComponent::class)
class NetworkConfig {

    @Singleton
    @Provides
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager = TokenManager(context)

    @Singleton
    @Provides
    fun provideNetworkManager(@ApplicationContext context: Context): NetworkManager = NetworkManager(context)

    @Singleton
    @Provides
    @Named("authOkHttpClient")
    fun provideAuthOkHttpClient(
        baseUrlInterceptor: BaseUrlInterceptor,
        userAgentInterceptor: UserAgentInterceptor
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(userAgentInterceptor)
            .addInterceptor(baseUrlInterceptor)
            .build()
    }

    @Singleton
    @Provides
    @Named("mainOkHttpClient")
    fun provideMainOkHttpClient(
        authInterceptor: AuthInterceptor,
        authAuthenticator: AuthAuthenticator,
        baseUrlInterceptor: BaseUrlInterceptor,
        userAgentInterceptor: UserAgentInterceptor,
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(userAgentInterceptor)
            .addInterceptor(baseUrlInterceptor)
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .authenticator(authAuthenticator)
            .connectTimeout(5, TimeUnit.MINUTES)
            .build()
    }

    @Singleton
    @Provides
    fun provideAuthInterceptor(tokenManager: TokenManager): AuthInterceptor =
        AuthInterceptor(tokenManager)

    @Singleton
    @Provides
    fun provideUserAgentInterceptor(): UserAgentInterceptor =
        UserAgentInterceptor("mobile")

    @Singleton
    @Provides
    fun provideBaseUrlInterceptor(networkManager: NetworkManager): BaseUrlInterceptor =
        BaseUrlInterceptor(networkManager)

    @Singleton
    @Provides
    fun provideAuthAuthenticator(
        tokenManager: TokenManager,
        networkManager: NetworkManager
    ): AuthAuthenticator =
        AuthAuthenticator(tokenManager, networkManager)

    @Singleton
    @Provides
    fun provideRetrofitBuilder(networkManager: NetworkManager): Retrofit.Builder {
        val baseUrl = runBlocking {
            networkManager.getBaseUrl().first()
        }
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Singleton
    @Provides
    fun provideAuthAPIService(@Named("authOkHttpClient") authOkHttpClient: OkHttpClient, retrofit: Retrofit.Builder): AuthApiService =
        retrofit
            .client(authOkHttpClient)
            .build()
            .create(AuthApiService::class.java)

    @Singleton
    @Provides
    fun provideMainAPIService(@Named("mainOkHttpClient") mainOkHttpClient: OkHttpClient, retrofit: Retrofit.Builder): MainApiService =
        retrofit
            .client(mainOkHttpClient)
            .build()
            .create(MainApiService::class.java)

    @Singleton
    @Provides
    fun provideNotificationAPIService(@Named("mainOkHttpClient") mainOkHttpClient: OkHttpClient, retrofit: Retrofit.Builder): NotificationApiService =
        retrofit
            .client(mainOkHttpClient)
            .build()
            .create(NotificationApiService::class.java)

    @Singleton
    @Provides
    fun provideChatAPIService(@Named("mainOkHttpClient") mainOkHttpClient: OkHttpClient, retrofit: Retrofit.Builder): ChatApiService =
        retrofit
            .client(mainOkHttpClient)
            .build()
            .create(ChatApiService::class.java)

    @Singleton
    @Provides
    fun provideChatHistoryStore(@ApplicationContext context: Context): ChatHistoryStore = ChatHistoryStore(context)
}