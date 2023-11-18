package javavlsu.kb.esap.esapmobile.domain.model.response

data class MedicalRecordResponse(
    val id: Long,
    val record: String,
    val fioAndSpecializationDoctor: String,
    val date: String,
    val analyzes: List<AnalysisResponse>
)
