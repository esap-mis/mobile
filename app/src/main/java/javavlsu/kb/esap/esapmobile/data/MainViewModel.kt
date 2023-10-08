package javavlsu.kb.esap.esapmobile.data

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javavlsu.kb.esap.esapmobile.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.domain.model.response.DoctorInfoResponse
import javavlsu.kb.esap.esapmobile.domain.repository.MainRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
): BaseViewModel() {

    private val _doctorInfoResponseResponse = MutableLiveData<ApiResponse<DoctorInfoResponse>>()
    val doctorInfoResponse = _doctorInfoResponseResponse

    fun getDoctorInfo(token: String, coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _doctorInfoResponseResponse,
        coroutinesErrorHandler
    ) {
        mainRepository.getDoctorInfo(token)
    }
}