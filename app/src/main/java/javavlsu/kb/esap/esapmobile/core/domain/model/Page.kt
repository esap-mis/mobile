package javavlsu.kb.esap.esapmobile.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Page<T>(
    val content: List<T>,
    val totalPages: Int,
    val totalElements: Int,
)