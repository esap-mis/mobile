package javavlsu.kb.esap.esapmobile.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javavlsu.kb.esap.esapmobile.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.domain.model.response.DoctorResponse
import javavlsu.kb.esap.esapmobile.domain.model.response.PatientResponse
import javavlsu.kb.esap.esapmobile.domain.repository.MainRepository
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
}