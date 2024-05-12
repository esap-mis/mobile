package javavlsu.kb.esap.esapmobile.presentation.util

import androidx.paging.PagingSource
import androidx.paging.PagingState
import javavlsu.kb.esap.esapmobile.core.domain.api.MainApiService
import javavlsu.kb.esap.esapmobile.core.domain.dto.Page
import javavlsu.kb.esap.esapmobile.core.domain.dto.response.DoctorResponse
import retrofit2.HttpException
import java.io.IOException

class DoctorsPagingSource(
    private val mainApiService: MainApiService
) : PagingSource<Int, DoctorResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DoctorResponse> {
        return try {
            val currentPage = params.key ?: 0
            val response = mainApiService.getDoctors(
                page = currentPage
            )

            if (response.isSuccessful) {
                val doctors = (response.body() as Page<DoctorResponse>)
                LoadResult.Page(
                    data = doctors.content,
                    prevKey = if (currentPage == 0) null else currentPage - 1,
                    nextKey = if (doctors.totalPages > currentPage + 1) currentPage + 1 else null
                )
            } else {
                LoadResult.Error(HttpException(response))
            }
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, DoctorResponse>): Int? {
        return state.anchorPosition
    }
}