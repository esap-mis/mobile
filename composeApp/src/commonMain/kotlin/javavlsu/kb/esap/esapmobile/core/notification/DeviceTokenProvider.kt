package javavlsu.kb.esap.esapmobile.core.notification

import org.koin.core.module.Module

interface DeviceTokenProvider {
    fun getDeviceToken(callback: (String?) -> Unit)
}

expect val deviceTokenProviderModule: Module

