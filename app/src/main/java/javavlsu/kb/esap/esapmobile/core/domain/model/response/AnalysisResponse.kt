package javavlsu.kb.esap.esapmobile.core.domain.model.response

import kotlinx.serialization.Serializable

@Serializable
data class AnalysisResponse(
    val id: Long,
    val name: String,
    val result: String,
    val date: String
)