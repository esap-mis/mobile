package javavlsu.kb.esap.esapmobile.core.domain.repository

import javavlsu.kb.esap.esapmobile.core.domain.api.NotificationApiService
import javavlsu.kb.esap.esapmobile.core.domain.model.request.TokenRequest

class NotificationRepository(
    private val notificationApiService: NotificationApiService,
) {
    suspend fun registerDeviceToken(token: TokenRequest) =
        notificationApiService.registerToken(token)
}