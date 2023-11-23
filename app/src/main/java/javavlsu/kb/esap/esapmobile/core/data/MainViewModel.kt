package javavlsu.kb.esap.esapmobile.core.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.request.AppointmentRequest
import javavlsu.kb.esap.esapmobile.core.domain.model.response.AppointmentResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.response.DoctorResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.response.MedicalCardResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.response.PatientResponse
import javavlsu.kb.esap.esapmobile.core.domain.repository.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
): BaseViewModel() {

    private val _doctorResponse = MutableLiveData<ApiResponse<DoctorResponse>>()
    val doctorResponse = _doctorResponse

    private val _patientResponse = MutableLiveData<ApiResponse<PatientResponse>>()
    val patientResponse = _patientResponse

    private val _doctorListResponse = MutableLiveData<ApiResponse<List<DoctorResponse>>>()
    val doctorListResponse = _doctorListResponse

    private val _doctorResponseById = MutableLiveData<ApiResponse<DoctorResponse>>()
    val doctorResponseById = _doctorResponseById

    private val _makeAppointmentResponse = MutableLiveData<ApiResponse<String>>()
    val makeAppointmentResponse = _makeAppointmentResponse

    private val _userAppointmentList = MutableLiveData<ApiResponse<List<AppointmentResponse>>>()
    val userAppointmentList = _userAppointmentList

    private val _medicalCardResponse = MutableLiveData<ApiResponse<MedicalCardResponse>>()
    val medicalCardResponse = _medicalCardResponse

    fun getDoctor(coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _doctorResponse,
        coroutinesErrorHandler
    ) {
        mainRepository.getDoctor()
    }

    fun getPatient(coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _patientResponse,
        coroutinesErrorHandler
    ) {
        mainRepository.getPatient()
    }

    fun getDoctorList(date: LocalDate, coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _doctorListResponse,
        coroutinesErrorHandler
    ) {
        mainRepository.getDoctorList(date)
    }

    fun getDoctorById(doctorId: Long, coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _doctorResponseById,
        coroutinesErrorHandler
    ) {
        mainRepository.getDoctorById(doctorId)
    }

    fun makeAppointment(scheduleId: Long,
                        appointmentRequest: AppointmentRequest,
                        coroutinesErrorHandler: CoroutinesErrorHandler
    ) = baseRequest(
        _makeAppointmentResponse,
        coroutinesErrorHandler
    ) {
        mainRepository.makeAppointment(scheduleId, appointmentRequest)
    }

    fun getUserAppointments(coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _userAppointmentList,
        coroutinesErrorHandler
    ) {
        mainRepository.getUserAppointments()
    }

    fun getPatientMedicalCard(patientId: Long, coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _medicalCardResponse,
        coroutinesErrorHandler
    ) {
        mainRepository.getMedicalCard(patientId)
    }

    private val _patientList: MutableStateFlow<PagingData<PatientResponse>> = MutableStateFlow(PagingData.empty())
    val patientList: StateFlow<PagingData<PatientResponse>> = _patientList

    fun getPatientsList() {
        viewModelScope.launch {
            mainRepository.getPatients()
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _patientList.value = pagingData
                }
        }
    }
}