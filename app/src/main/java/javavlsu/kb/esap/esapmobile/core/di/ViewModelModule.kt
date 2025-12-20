package javavlsu.kb.esap.esapmobile.core.di

import javavlsu.kb.esap.esapmobile.core.data.AuthViewModel
import javavlsu.kb.esap.esapmobile.core.data.CalendarViewModel
import javavlsu.kb.esap.esapmobile.core.data.ChatViewModel
import javavlsu.kb.esap.esapmobile.core.data.MainViewModel
import javavlsu.kb.esap.esapmobile.core.data.NotificationViewModel
import javavlsu.kb.esap.esapmobile.core.data.SettingsViewModel
import javavlsu.kb.esap.esapmobile.core.data.TokenViewModel
import javavlsu.kb.esap.esapmobile.core.domain.util.BaseUrlInterceptor
import javavlsu.kb.esap.esapmobile.core.domain.util.NetworkManager
import org.koin.dsl.module

val viewModelModule = module {
    single { MainViewModel(get()) }
    single { AuthViewModel(get()) }
    single { NotificationViewModel(get()) }
    single { CalendarViewModel(get()) }
    single {
        SettingsViewModel(
            get<NetworkManager>(),
            get<BaseUrlInterceptor>()
        )
    }
    single { TokenViewModel(get()) }
    single { ChatViewModel(get()) }
}