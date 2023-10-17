package javavlsu.kb.esap.esapmobile.domain.api

import javavlsu.kb.esap.esapmobile.domain.model.response.DoctorResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.LocalDate

interface MainApiService {
    @GET("api/doctor/home")
    suspend fun getDoctor(): Response<DoctorResponse>

    @GET("api/doctor/schedules")
    suspend fun getDoctorList(@Query("date") date: LocalDate): Response<List<DoctorResponse>>
}