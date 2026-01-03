package javavlsu.kb.esap.esapmobile.core.domain.api

import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import javavlsu.kb.esap.esapmobile.core.domain.model.Page
import javavlsu.kb.esap.esapmobile.core.domain.model.request.AppointmentRequest
import javavlsu.kb.esap.esapmobile.core.domain.model.response.AppointmentResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.response.DoctorResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.response.MedicalCardResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.response.PatientResponse
import java.time.LocalDate
import javax.inject.Inject

interface IMainApiService {
    suspend fun getDoctor(): ApiResponse<DoctorResponse>
    suspend fun getPatient(): ApiResponse<PatientResponse>
    suspend fun getDoctorList(date: LocalDate): ApiResponse<List<DoctorResponse>>
    suspend fun getDoctorById(doctorId: Long): ApiResponse<DoctorResponse>
    suspend fun makeAppointment(scheduleId: Long, appointmentRequest: AppointmentRequest): ApiResponse<String>
    suspend fun getUserAppointments(): ApiResponse<List<AppointmentResponse>>
    suspend fun getMedicalCard(patientId: Long): ApiResponse<MedicalCardResponse>
    suspend fun getPatients(page: Int, size: Int = 10): ApiResponse<Page<PatientResponse>>
    suspend fun getDoctors(page: Int, size: Int = 10): ApiResponse<Page<DoctorResponse>>
}

class MainApiService @Inject constructor(
    private val mainClient: HttpClient
) : BaseApiService(mainClient), IMainApiService {

    override suspend fun getDoctor(): ApiResponse<DoctorResponse> {
        return safeRequest {
            url("api/doctor/home")
            method = HttpMethod.Get
        }
    }

    override suspend fun getPatient(): ApiResponse<PatientResponse> {
        return safeRequest {
            url("api/doctor/home")
            method = HttpMethod.Get
        }
    }

    override suspend fun getDoctorList(date: LocalDate): ApiResponse<List<DoctorResponse>> {
        return safeRequest {
            url("api/doctor/schedules")
            method = HttpMethod.Get
            parameter("date", date.toString())
        }
    }

    override suspend fun getDoctorById(doctorId: Long): ApiResponse<DoctorResponse> {
        return safeRequest {
            url("api/doctor/$doctorId")
            method = HttpMethod.Get
        }
    }

    override suspend fun makeAppointment(
        scheduleId: Long,
        appointmentRequest: AppointmentRequest
    ): ApiResponse<String> {
        return safeRequest {
            url("api/schedule/$scheduleId/appointment")
            method = HttpMethod.Post
            setBody(appointmentRequest)
        }
    }

    override suspend fun getUserAppointments(): ApiResponse<List<AppointmentResponse>> {
        return safeRequest {
            url("api/schedule/appointments")
            method = HttpMethod.Get
        }
    }

    override suspend fun getMedicalCard(patientId: Long): ApiResponse<MedicalCardResponse> {
        return safeRequest {
            url("api/medicalCard/patient/$patientId")
            method = HttpMethod.Get
        }
    }

    override suspend fun getPatients(page: Int, size: Int): ApiResponse<Page<PatientResponse>> {
        return safeRequest {
            url("api/patient")
            method = HttpMethod.Get
            parameter("page", page)
            parameter("size", size)
        }
    }

    override suspend fun getDoctors(page: Int, size: Int): ApiResponse<Page<DoctorResponse>> {
        return safeRequest {
            url("api/doctor")
            method = HttpMethod.Get
            parameter("page", page)
            parameter("size", size)
        }
    }
}
