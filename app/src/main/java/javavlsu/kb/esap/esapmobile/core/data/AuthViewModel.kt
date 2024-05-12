package javavlsu.kb.esap.esapmobile.core.data

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javavlsu.kb.esap.esapmobile.core.domain.dto.request.AuthRequest
import javavlsu.kb.esap.esapmobile.core.domain.dto.response.AuthResponse
import javavlsu.kb.esap.esapmobile.core.domain.repository.AuthRepository
import javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse
import javax.inject.Inject
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import javavlsu.kb.esap.esapmobile.core.data.model.User
import javavlsu.kb.esap.esapmobile.core.domain.dto.UserResponse
import javavlsu.kb.esap.esapmobile.core.domain.dto.response.ServerStatusResponse
import kotlinx.coroutines.launch

@HiltViewModel
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

    //FIXME переделать этот бред
    fun saveUser(response: AuthResponse) = viewModelScope.launch {
        val user = User(
            login = login.value,
            password = password.value,
            roles = response.roles
        )
        authRepository.insertUser(user)
    }
}