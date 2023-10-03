package javavlsu.kb.esap.esapmobile.domain.api

import javavlsu.kb.esap.esapmobile.domain.model.response.UserInfoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface MainApiService {
    @GET("api/doctor/home")
    suspend fun getUserInfo(@Header("Authorization") token: String): Response<UserInfoResponse>
}