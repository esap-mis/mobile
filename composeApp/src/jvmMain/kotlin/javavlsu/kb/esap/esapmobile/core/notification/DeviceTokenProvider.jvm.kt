package javavlsu.kb.esap.esapmobile.core.notification

import org.koin.core.module.Module
import org.koin.dsl.module

class DesktopDeviceTokenProvider : DeviceTokenProvider {
    override fun getDeviceToken(callback: (String?) -> Unit) {
        callback(null)
    }

}

actual val deviceTokenProviderModule: Module = module {
    single<DeviceTokenProvider> { DesktopDeviceTokenProvider() }
}