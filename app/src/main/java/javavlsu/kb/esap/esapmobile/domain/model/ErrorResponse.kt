package javavlsu.kb.esap.esapmobile.domain.model

data class ErrorResponse(
    val code: Int,
    val status: String,
    val message: String
)