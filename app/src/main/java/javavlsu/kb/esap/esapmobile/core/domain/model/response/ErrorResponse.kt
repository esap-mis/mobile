package javavlsu.kb.esap.esapmobile.core.domain.model.response

data class ErrorResponse(
    val code: Int,
    val status: String,
    val message: String
)