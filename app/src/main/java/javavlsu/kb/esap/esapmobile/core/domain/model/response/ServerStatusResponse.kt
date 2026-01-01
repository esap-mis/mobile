package javavlsu.kb.esap.esapmobile.core.domain.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ServerStatusResponse(
    val status: String
)