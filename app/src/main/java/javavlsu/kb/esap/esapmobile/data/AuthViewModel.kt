package javavlsu.kb.esap.esapmobile.data

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javavlsu.kb.esap.esapmobile.domain.model.AuthRequest
import javavlsu.kb.esap.esapmobile.domain.model.AuthResponse
import javavlsu.kb.esap.esapmobile.domain.repository.AuthRepository
import javavlsu.kb.esap.esapmobile.util.ApiResponse
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
): BaseViewModel() {

    private val _authResponse = MutableLiveData<ApiResponse<AuthResponse>>()
    val authResponse = _authResponse

    fun login(request: AuthRequest, coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _authResponse,
        coroutinesErrorHandler
    ) {
        authRepository.login(request)
    }
}