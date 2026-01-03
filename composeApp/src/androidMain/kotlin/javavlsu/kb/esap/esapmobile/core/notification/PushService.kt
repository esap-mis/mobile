package javavlsu.kb.esap.esapmobile.core.notification

import com.google.firebase.messaging.FirebaseMessagingService

class PushService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}