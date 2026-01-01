package javavlsu.kb.esap.esapmobile.core.domain.model.response

import kotlinx.serialization.Serializable

@Serializable
data class MedicalRecordResponse(
    val id: Long,
    val record: String,
    val fioAndSpecializationDoctor: String,
    val date: String,
    val analyzes: List<AnalysisResponse>
)
