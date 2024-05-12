package javavlsu.kb.esap.esapmobile.core.domain.dto.response

data class MedicalRecordResponse(
    val id: Long,
    val record: String,
    val fioAndSpecializationDoctor: String,
    val date: String,
    val analyzes: List<AnalysisResponse>
)
