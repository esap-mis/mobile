package javavlsu.kb.esap.esapmobile.presentation.util

import app.cash.paging.PagingSource
import app.cash.paging.PagingState
import javavlsu.kb.esap.esapmobile.core.domain.api.MainApiService
import javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.response.PatientResponse

class PatientsPagingSource(
    private val mainApiService: MainApiService
) : PagingSource<Int, PatientResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PatientResponse> {
        return try {
            val currentPage = params.key ?: 0
            val response = mainApiService.getPatients(
                page = currentPage
            )

            when (response) {
                is ApiResponse.Success -> {
                    val patients = response.data
                    LoadResult.Page(
                        data = patients.content,
                        prevKey = if (currentPage == 0) null else currentPage - 1,
                        nextKey = if (patients.totalPages > currentPage + 1) currentPage + 1 else null
                    )
                }
                is ApiResponse.Failure -> {
                    LoadResult.Error(Exception(response.errorMessage))
                }
                is ApiResponse.Loading -> {
                    LoadResult.Error(Exception("Loading"))
                }
            }
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PatientResponse>): Int? {
        return state.anchorPosition
    }
}