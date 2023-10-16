package javavlsu.kb.esap.esapmobile.data

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javavlsu.kb.esap.esapmobile.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.domain.model.response.DoctorResponse
import javavlsu.kb.esap.esapmobile.domain.repository.MainRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
): BaseViewModel() {

    private val _doctorResponse = MutableLiveData<ApiResponse<DoctorResponse>>()
    val doctorResponse = _doctorResponse

    fun getDoctor(coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _doctorResponse,
        coroutinesErrorHandler
    ) {
        mainRepository.getDoctor()
    }
}