package javavlsu.kb.esap.esapmobile.core.di

import javavlsu.kb.esap.esapmobile.core.data.*
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get(), get()) }
    viewModel { AuthViewModel(get()) }
    viewModel { NotificationViewModel(get(), get()) }
    viewModel { CalendarViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { TokenViewModel(get()) }
    single { ChatViewModel(get()) }
    viewModel { FileDownloaderViewModel(get()) }
}