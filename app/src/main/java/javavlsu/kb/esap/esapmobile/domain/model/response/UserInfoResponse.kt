package javavlsu.kb.esap.esapmobile.domain.model.response

data class UserInfoResponse(
    val id: Long,
    val login: String,
    val firstName: String,
    val patronymic: String,
    val lastName: String,
    val specialization: String,
    val gender: Int
)