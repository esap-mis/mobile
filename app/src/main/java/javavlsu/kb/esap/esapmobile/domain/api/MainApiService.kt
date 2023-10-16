package javavlsu.kb.esap.esapmobile.domain.api

import javavlsu.kb.esap.esapmobile.domain.model.response.DoctorListResponse
import javavlsu.kb.esap.esapmobile.domain.model.response.DoctorResponse
import retrofit2.Response
import retrofit2.http.GET

interface MainApiService {
    @GET("api/doctor/home")
    suspend fun getDoctor(): Response<DoctorResponse>

    @GET("api/doctor")
    suspend fun getDoctorList(): Response<DoctorListResponse>
}