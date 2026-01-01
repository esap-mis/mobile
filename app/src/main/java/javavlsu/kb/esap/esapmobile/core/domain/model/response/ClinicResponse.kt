package javavlsu.kb.esap.esapmobile.core.domain.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ClinicResponse(
    val id: Long,
    val name: String,
    val address: String,
    val phoneNumber: String
)