package javavlsu.kb.esap.esapmobile.core.domain.dto.response

data class MedicalCardResponse(
    val id: Long,
    val patient: PatientResponse,
    val medicalRecord: List<MedicalRecordResponse>
)