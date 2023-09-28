package javavlsu.kb.esap.esapmobile.domain.api

import javavlsu.kb.esap.esapmobile.domain.model.response.UserInfoResponse
import retrofit2.Response
import retrofit2.http.GET

interface MainApiService {
    @GET("api/doctor/home")
    suspend fun getUserInfo(): Response<UserInfoResponse>
}