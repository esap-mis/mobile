package javavlsu.kb.esap.esapmobile.core.domain.dto.response

data class ErrorResponse(
    val code: Int,
    val status: String,
    val message: String
)