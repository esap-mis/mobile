package javavlsu.kb.esap.esapmobile.core.domain.repository

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.core.domain.api.MainApiService
import javavlsu.kb.esap.esapmobile.core.domain.model.request.AppointmentRequest
import javavlsu.kb.esap.esapmobile.core.domain.model.response.AppointmentResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.response.DoctorResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.response.MedicalCardResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.response.PatientResponse
import javavlsu.kb.esap.esapmobile.presentation.util.DoctorsPagingSource
import javavlsu.kb.esap.esapmobile.presentation.util.PatientsPagingSource
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class MainRepository(
    private val mainApiService: MainApiService,
) {
    suspend fun getDoctor(): ApiResponse<DoctorResponse> = mainApiService.getDoctor()

    suspend fun getPatient(): ApiResponse<PatientResponse> = mainApiService.getPatient()

    suspend fun getDoctorList(date: LocalDate): ApiResponse<List<DoctorResponse>> =
        mainApiService.getDoctorList(date)

    suspend fun getDoctorById(doctorId: Long):  ApiResponse<DoctorResponse> =
        mainApiService.getDoctorById(doctorId)

    suspend fun makeAppointment(scheduleId: Long, appointmentRequest: AppointmentRequest): ApiResponse<String> =
        mainApiService.makeAppointment(scheduleId, appointmentRequest)

    suspend fun getUpcomingUserAppointments():  ApiResponse<List<AppointmentResponse>> =
        mainApiService.getUpcomingUserAppointments()

    suspend fun getPastUserAppointments():  ApiResponse<List<AppointmentResponse>> =
        mainApiService.getPastUserAppointments()

    suspend fun getMedicalCard(patientId: Long): ApiResponse<MedicalCardResponse> =
        mainApiService.getMedicalCard(patientId)

    fun getPatients(): Flow<PagingData<PatientResponse>> {
        return Pager(
            config = PagingConfig(pageSize = PagingConfig.MAX_SIZE_UNBOUNDED, prefetchDistance = 2),
            pagingSourceFactory = {
                PatientsPagingSource(mainApiService)
            }
        ).flow
    }

    fun getDoctors(): Flow<PagingData<DoctorResponse>> {
        return Pager(
            config = PagingConfig(pageSize = PagingConfig.MAX_SIZE_UNBOUNDED, prefetchDistance = 2),
            pagingSourceFactory = {
                DoctorsPagingSource(mainApiService)
            }
        ).flow
    }

    suspend fun cancelAppointment(appointmentId: Long): ApiResponse<Unit> =
        mainApiService.cancelAppointment(appointmentId)
}