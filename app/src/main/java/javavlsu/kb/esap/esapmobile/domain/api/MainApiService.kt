package javavlsu.kb.esap.esapmobile.domain.api

import javavlsu.kb.esap.esapmobile.domain.model.response.DoctorInfoResponse
import retrofit2.Response
import retrofit2.http.GET

interface MainApiService {
    @GET("api/doctor/home")
    suspend fun getDoctorInfo(): Response<DoctorInfoResponse>
}