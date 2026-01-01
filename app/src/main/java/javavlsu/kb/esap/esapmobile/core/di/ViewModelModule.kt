package javavlsu.kb.esap.esapmobile.core.di

import javavlsu.kb.esap.esapmobile.core.data.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { AuthViewModel(get()) }
    viewModel { NotificationViewModel(get()) }
    viewModel { CalendarViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { TokenViewModel(get()) }
    viewModel { ChatViewModel(get()) }
}