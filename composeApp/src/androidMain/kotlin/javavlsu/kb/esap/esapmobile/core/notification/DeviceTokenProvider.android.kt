package javavlsu.kb.esap.esapmobile.core.notification

import com.google.firebase.messaging.FirebaseMessaging
import org.koin.core.module.Module
import org.koin.dsl.module

class AndroidDeviceTokenProvider : DeviceTokenProvider {

    override fun getDeviceToken(callback: (String?) -> Unit) {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(task.result)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                callback(null)
            }
    }
}

actual val deviceTokenProviderModule: Module = module  {
    single<DeviceTokenProvider> { AndroidDeviceTokenProvider() }
}