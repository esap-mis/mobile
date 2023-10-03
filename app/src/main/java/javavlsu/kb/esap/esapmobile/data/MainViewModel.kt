package javavlsu.kb.esap.esapmobile.data

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javavlsu.kb.esap.esapmobile.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.domain.model.response.UserInfoResponse
import javavlsu.kb.esap.esapmobile.domain.repository.MainRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
): BaseViewModel() {

    private val _userInfoResponseResponse = MutableLiveData<ApiResponse<UserInfoResponse>>()
    val userInfoResponse = _userInfoResponseResponse

    fun getUserInfo(token: String, coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _userInfoResponseResponse,
        coroutinesErrorHandler
    ) {
        mainRepository.getUserInfo(token)
    }
}