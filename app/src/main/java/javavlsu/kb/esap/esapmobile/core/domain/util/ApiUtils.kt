package javavlsu.kb.esap.esapmobile.core.domain.util

import javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

fun<T> apiRequestFlow(call: suspend () -> ApiResponse<T>): Flow<ApiResponse<T>> = flow {
    emit(ApiResponse.Loading)
    emit(call())
}.flowOn(Dispatchers.IO)