package javavlsu.kb.esap.esapmobile.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import javavlsu.kb.esap.esapmobile.domain.repository.paging.PatientsPagingSource
import javavlsu.kb.esap.esapmobile.domain.api.MainApiService
import javavlsu.kb.esap.esapmobile.domain.model.request.AppointmentRequest
import javavlsu.kb.esap.esapmobile.domain.model.response.PatientResponse
import javavlsu.kb.esap.esapmobile.domain.util.apiRequestFlow
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val mainApiService: MainApiService,
) {
    fun getDoctor() = apiRequestFlow {
        mainApiService.getDoctor()
    }

    fun getPatient() = apiRequestFlow {
        mainApiService.getPatient()
    }

    fun getDoctorList(date: LocalDate) = apiRequestFlow {
        mainApiService.getDoctorList(date)
    }

    fun getDoctorById(doctorId: Long) = apiRequestFlow {
        mainApiService.getDoctorById(doctorId)
    }

    fun makeAppointment(scheduleId: Long, appointmentRequest: AppointmentRequest) = apiRequestFlow {
        mainApiService.makeAppointment(scheduleId, appointmentRequest)
    }

    fun getUserAppointments() = apiRequestFlow {
        mainApiService.getUserAppointments()
    }

    fun getMedicalCard(patientId: Long) = apiRequestFlow {
        mainApiService.getMedicalCard(patientId)
    }

    fun getPatients(): Flow<PagingData<PatientResponse>> {
        return Pager(
            config = PagingConfig(pageSize = PagingConfig.MAX_SIZE_UNBOUNDED, prefetchDistance = 2),
            pagingSourceFactory = {
                PatientsPagingSource(mainApiService)
            }
        ).flow
    }
}