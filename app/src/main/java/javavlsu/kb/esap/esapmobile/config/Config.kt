package javavlsu.kb.esap.esapmobile.config

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javavlsu.kb.esap.esapmobile.domain.api.AuthApiService
import javavlsu.kb.esap.esapmobile.domain.api.MainApiService
import javavlsu.kb.esap.esapmobile.domain.repository.AuthRepository
import javavlsu.kb.esap.esapmobile.domain.repository.MainRepository
import javavlsu.kb.esap.esapmobile.presentation.util.CalendarDataSource


@Module
@InstallIn(ViewModelComponent::class)
class Config {

    @Provides
    fun provideAuthRepository(authApiService: AuthApiService) = AuthRepository(authApiService)

    @Provides
    fun provideMainRepository(mainApiService: MainApiService) = MainRepository(mainApiService)

    @Provides
    fun provideCalendarDataSource() = CalendarDataSource()
}