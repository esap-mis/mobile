package javavlsu.kb.esap.esapmobile.core.data

import androidx.lifecycle.viewModelScope
import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.request.AppointmentRequest
import javavlsu.kb.esap.esapmobile.core.domain.model.response.AppointmentResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.response.DoctorResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.response.MedicalCardResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.response.PatientResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.response.ScheduleResponse
import javavlsu.kb.esap.esapmobile.core.domain.network.TokenManager
import javavlsu.kb.esap.esapmobile.core.domain.repository.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate

class MainViewModel(
    private val mainRepository: MainRepository,
    private val tokenManager: TokenManager
) : BaseViewModel() {

    private val _doctorState = MutableStateFlow<ApiResponse<DoctorResponse>?>(null)
    val doctorState: StateFlow<ApiResponse<DoctorResponse>?> = _doctorState.asStateFlow()

    private val _patientState = MutableStateFlow<ApiResponse<PatientResponse>?>(null)
    val patientState: StateFlow<ApiResponse<PatientResponse>?> = _patientState.asStateFlow()

    private val _doctorListState = MutableStateFlow<ApiResponse<List<DoctorResponse>>?>(null)
    val doctorListState: StateFlow<ApiResponse<List<DoctorResponse>>?> = _doctorListState.asStateFlow()

    private val _doctorByIdState = MutableStateFlow<ApiResponse<DoctorResponse>?>(null)
    val doctorByIdState: StateFlow<ApiResponse<DoctorResponse>?> = _doctorByIdState.asStateFlow()

    private val _makeAppointmentState = MutableStateFlow<ApiResponse<String>?>(null)
    val makeAppointmentState: StateFlow<ApiResponse<String>?> = _makeAppointmentState.asStateFlow()

    private val _upcomingAppointmentsState = MutableStateFlow<ApiResponse<List<AppointmentResponse>>?>(null)
    val upcomingAppointmentsState: StateFlow<ApiResponse<List<AppointmentResponse>>?> = _upcomingAppointmentsState.asStateFlow()

    private val _pastAppointmentsState = MutableStateFlow<ApiResponse<List<AppointmentResponse>>?>(null)
    val pastAppointmentsState: StateFlow<ApiResponse<List<AppointmentResponse>>?> = _pastAppointmentsState.asStateFlow()

    private val _medicalCardState = MutableStateFlow<ApiResponse<MedicalCardResponse>?>(null)
    val medicalCardState: StateFlow<ApiResponse<MedicalCardResponse>?> = _medicalCardState.asStateFlow()

    private val _patientsList = MutableStateFlow<PagingData<PatientResponse>>(PagingData.empty())
    val patientsList: StateFlow<PagingData<PatientResponse>> = _patientsList.asStateFlow()

    private val _doctorsList = MutableStateFlow<PagingData<DoctorResponse>>(PagingData.empty())
    val doctorsList: StateFlow<PagingData<DoctorResponse>> = _doctorsList.asStateFlow()

    private val _userRole = MutableStateFlow<String?>(null)
    val userRole: StateFlow<String?> = _userRole.asStateFlow()

    private val _currentUser = MutableStateFlow<Any?>(null)
    val currentUser: StateFlow<Any?> = _currentUser.asStateFlow()

    private val _cancelAppointmentState = MutableStateFlow<ApiResponse<Unit>?>(null)
    val cancelAppointmentState: StateFlow<ApiResponse<Unit>?> = _cancelAppointmentState.asStateFlow()

    private val _doctorSchedulesState = MutableStateFlow<ApiResponse<List<ScheduleResponse>>?>(null)
    val doctorSchedulesState: StateFlow<ApiResponse<List<ScheduleResponse>>?> = _doctorSchedulesState.asStateFlow()

    init {
        observeUserRole()
        getUpcomingAppointments()
        getPastAppointments()
    }

    private fun observeUserRole() {
        viewModelScope.launch {
            val roles = tokenManager.getRoles()
            _userRole.value = roles
            loadUserDataBasedOnRole(roles)
        }
    }

    private fun loadUserDataBasedOnRole(roles: String?) {
        when {
            roles?.contains("ROLE_DOCTOR") == true ||
                    roles?.contains("ROLE_CHIEF_DOCTOR") == true -> {
                getDoctor()
            }
            roles?.contains("ROLE_PATIENT") == true -> {
                getPatient()
            }
        }
    }

    fun getDoctor() {
        launchRequestWithState(_doctorState, true) {
            mainRepository.getDoctor()
        }
    }

    fun getPatient() {
        launchRequestWithState(_patientState, true) {
            mainRepository.getPatient()
        }
    }

    fun getDoctorList(date: LocalDate) {
        launchRequestWithState(_doctorListState, true) {
            mainRepository.getDoctorList(date)
        }
    }

    fun getDoctorById(doctorId: Long) {
        launchRequestWithState(_doctorByIdState, true) {
            mainRepository.getDoctorById(doctorId)
        }
    }

    fun makeAppointment(scheduleId: Long, appointmentRequest: AppointmentRequest) {
        launchRequestWithState(_makeAppointmentState, true) {
            val response = mainRepository.makeAppointment(scheduleId, appointmentRequest)
            if (response is ApiResponse.Success) {
                getUpcomingAppointments()
            }
            response
        }
    }

    fun getUpcomingAppointments() {
        launchRequestWithState(_upcomingAppointmentsState, true) {
            mainRepository.getUpcomingUserAppointments()
        }
    }

    fun getPastAppointments() {
        launchRequestWithState(_pastAppointmentsState, true) {
            mainRepository.getPastUserAppointments()
        }
    }

    fun getPatientMedicalCard(patientId: Long) {
        launchRequestWithState(_medicalCardState, true) {
            mainRepository.getMedicalCard(patientId)
        }
    }

    fun getPatientsList() {
        viewModelScope.launch {
            mainRepository.getPatients()
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _patientsList.value = pagingData
                }
        }
    }

    fun getDoctorsList() {
        viewModelScope.launch {
            mainRepository.getDoctors()
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _doctorsList.value = pagingData
                }
        }
    }

    fun cancelAppointment(appointmentId: Long) {
        launchRequestWithState(_cancelAppointmentState, true) {
            val response = mainRepository.cancelAppointment(appointmentId)
            if (response is ApiResponse.Success) {
                getUpcomingAppointments()
            }
            response
        }
    }

    fun getDoctorSchedules(doctorId: Long) {
        launchRequestWithState(_doctorSchedulesState, true) {
            mainRepository.getDoctorSchedules(doctorId)
        }
    }
}