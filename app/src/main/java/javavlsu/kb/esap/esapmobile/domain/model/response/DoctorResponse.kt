package javavlsu.kb.esap.esapmobile.domain.model.response

data class DoctorResponse(
    val id: Long,
    val firstName: String,
    val patronymic: String,
    val lastName: String,
    val specialization: String,
    val gender: Int
)