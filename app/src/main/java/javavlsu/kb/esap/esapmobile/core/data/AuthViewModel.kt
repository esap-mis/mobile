package javavlsu.kb.esap.esapmobile.core.data

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.request.AuthRequest
import javavlsu.kb.esap.esapmobile.core.domain.model.response.AuthResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.response.ServerStatusResponse
import javavlsu.kb.esap.esapmobile.core.domain.repository.AuthRepository
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
): BaseViewModel() {

    private val _authResponse = MutableLiveData<ApiResponse<AuthResponse>>()
    val authResponse = _authResponse

    private val _serverStatusResponse = MutableLiveData<ApiResponse<ServerStatusResponse>>()
    val serverStatusResponse = _serverStatusResponse

    private var _login  = mutableStateOf("")
    val login: State<String> = _login

    fun setLogin(value: String){
        _login.value = value
    }

    private var _password  = mutableStateOf("")
    val password: State<String> = _password

    fun setPassword(value: String){
        _password.value = value
    }

    fun checkServerStatus(coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _serverStatusResponse,
        coroutinesErrorHandler
    ) {
        authRepository.checkStatus()
    }

    fun login(coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _authResponse,
        coroutinesErrorHandler
    ) {
        val request = AuthRequest(_login.value, _password.value)
        authRepository.login(request)
    }

    private val _passwordResetResponse = MutableLiveData<ApiResponse<String>>()
    val passwordResetResponse = _passwordResetResponse

    fun resetPassword(coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _passwordResetResponse,
        coroutinesErrorHandler
    ) {
        val request = AuthRequest(_login.value, _password.value)
        authRepository.resetPassword(request)
    }
}