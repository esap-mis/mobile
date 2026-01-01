package javavlsu.kb.esap.esapmobile.presentation.util

import androidx.paging.PagingSource
import androidx.paging.PagingState
import javavlsu.kb.esap.esapmobile.core.domain.api.MainApiService
import javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.response.DoctorResponse

class DoctorsPagingSource(
    private val mainApiService: MainApiService
) : PagingSource<Int, DoctorResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DoctorResponse> {
        return try {
            val currentPage = params.key ?: 0
            val response = mainApiService.getDoctors(
                page = currentPage
            )

            when (response) {
                is ApiResponse.Success -> {
                    val doctors = response.data
                    LoadResult.Page(
                        data = doctors.content,
                        prevKey = if (currentPage == 0) null else currentPage - 1,
                        nextKey = if (doctors.totalPages > currentPage + 1) currentPage + 1 else null
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

    override fun getRefreshKey(state: PagingState<Int, DoctorResponse>): Int? {
        return state.anchorPosition
    }
}