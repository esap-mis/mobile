package javavlsu.kb.esap.esapmobile.domain.repository

import javavlsu.kb.esap.esapmobile.domain.api.MainApiService
import javavlsu.kb.esap.esapmobile.domain.model.request.AppointmentRequest
import javavlsu.kb.esap.esapmobile.domain.util.apiRequestFlow
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
}