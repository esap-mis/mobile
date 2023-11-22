package javavlsu.kb.esap.esapmobile.domain.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import javavlsu.kb.esap.esapmobile.domain.api.MainApiService
import javavlsu.kb.esap.esapmobile.domain.model.Page
import javavlsu.kb.esap.esapmobile.domain.model.response.PatientResponse
import retrofit2.HttpException
import java.io.IOException

class PatientsPagingSource(
    private val mainApiService: MainApiService
) : PagingSource<Int, PatientResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PatientResponse> {
        return try {
            val currentPage = params.key ?: 0
            val response = mainApiService.getPatients(
                page = currentPage
            )

            if (response.isSuccessful) {
                val patients = (response.body() as Page<PatientResponse>)
                LoadResult.Page(
                    data = patients.content,
                    prevKey = if (currentPage == 0) null else currentPage - 1,
                    nextKey = if (patients.totalPages > currentPage + 1) currentPage + 1 else null
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

    override fun getRefreshKey(state: PagingState<Int, PatientResponse>): Int? {
        return state.anchorPosition
    }
}