package javavlsu.kb.esap.esapmobile.core.domain.model

data class Page<T>(
    val content: List<T>,
    val totalPages: Int,
    val totalElements: Int,
)