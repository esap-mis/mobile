package javavlsu.kb.esap.esapmobile.core.config

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javavlsu.kb.esap.esapmobile.core.domain.api.AuthApiService
import javavlsu.kb.esap.esapmobile.core.domain.api.ChatApiService
import javavlsu.kb.esap.esapmobile.core.domain.api.MainApiService
import javavlsu.kb.esap.esapmobile.core.domain.repository.AuthRepository
import javavlsu.kb.esap.esapmobile.core.domain.repository.ChatRepository
import javavlsu.kb.esap.esapmobile.core.domain.repository.MainRepository
import javavlsu.kb.esap.esapmobile.presentation.util.CalendarDataSource


@Module
@InstallIn(ViewModelComponent::class)
class Config {

    @Provides
    fun provideAuthRepository(authApiService: AuthApiService) = AuthRepository(authApiService)

    @Provides
    fun provideMainRepository(mainApiService: MainApiService) = MainRepository(mainApiService)

    @Provides
    fun provideChatRepository(chatApiService: ChatApiService) = ChatRepository(chatApiService)

    @Provides
    fun provideCalendarDataSource() = CalendarDataSource()
}