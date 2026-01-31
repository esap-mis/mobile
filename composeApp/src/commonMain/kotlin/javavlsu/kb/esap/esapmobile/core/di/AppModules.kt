package javavlsu.kb.esap.esapmobile.core.di

import javavlsu.kb.esap.esapmobile.core.domain.network.networkManagerModule
import javavlsu.kb.esap.esapmobile.core.domain.network.tokenManagerModule
import javavlsu.kb.esap.esapmobile.core.notification.deviceTokenProviderModule
import javavlsu.kb.esap.esapmobile.core.util.fileDownloaderModule

val appModules = listOf(
    networkModule,
    networkManagerModule,
    tokenManagerModule,
    fileDownloaderModule,
    deviceTokenProviderModule,
    repositoryModule,
    viewModelModule
)