package javavlsu.kb.esap.esapmobile.core.domain.api

import javavlsu.kb.esap.esapmobile.core.domain.model.Page
import javavlsu.kb.esap.esapmobile.core.domain.model.request.AppointmentRequest
import javavlsu.kb.esap.esapmobile.core.domain.model.response.AppointmentResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.response.DoctorResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.response.MedicalCardResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.response.PatientResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDate

interface MainApiService {
    @GET("api/doctor/home")
    suspend fun getDoctor(): Response<DoctorResponse>

    @GET("api/doctor/home")
    suspend fun getPatient(): Response<PatientResponse>

    @GET("api/doctor/schedules")
    suspend fun getDoctorList(@Query("date") date: LocalDate): Response<List<DoctorResponse>>

    @GET("api/doctor/{doctorId}")
    suspend fun getDoctorById(@Path("doctorId") doctorId: Long): Response<DoctorResponse>

    @POST("api/schedule/{scheduleId}/appointment")
    suspend fun makeAppointment(
        @Path("scheduleId") scheduleId: Long,
        @Body appointmentRequest: AppointmentRequest
    ): Response<String>

    @GET("/api/schedule/appointments")
    suspend fun getUserAppointments(): Response<List<AppointmentResponse>>

    @GET("/api/medicalCard/patient/{patientId}")
    suspend fun getMedicalCard(@Path("patientId") patientId: Long): Response<MedicalCardResponse>

    @GET("/api/patient")
    suspend fun getPatients(
        @Query("page") page: Int,
        @Query("size") size: Int = 10
    ): Response<Page<PatientResponse>>

    @GET("/api/doctor")
    suspend fun getDoctors(
        @Query("page") page: Int,
        @Query("size") size: Int = 10
    ): Response<Page<DoctorResponse>>
}