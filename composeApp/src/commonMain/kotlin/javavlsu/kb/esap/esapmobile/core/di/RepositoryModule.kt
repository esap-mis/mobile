package javavlsu.kb.esap.esapmobile.core.di

import javavlsu.kb.esap.esapmobile.core.domain.repository.AuthRepository
import javavlsu.kb.esap.esapmobile.core.domain.repository.ChatRepository
import javavlsu.kb.esap.esapmobile.core.domain.repository.MainRepository
import javavlsu.kb.esap.esapmobile.core.domain.repository.NotificationRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { AuthRepository(get()) }
    single { MainRepository(get()) }
    single { ChatRepository(get()) }
    single { NotificationRepository(get()) }
}