package javavlsu.kb.esap.esapmobile.core.data

import javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.request.AuthRequest
import javavlsu.kb.esap.esapmobile.core.domain.model.response.AuthResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.response.ServerStatusResponse
import javavlsu.kb.esap.esapmobile.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel(
    private val authRepository: AuthRepository
) : javavlsu.kb.esap.esapmobile.core.data.BaseAuthViewModel() {

    // Состояния
    private val _authState = MutableStateFlow<ApiResponse<AuthResponse>?>(null)
    val authState: StateFlow<ApiResponse<AuthResponse>?> = _authState

    private val _serverStatusState = MutableStateFlow<ApiResponse<ServerStatusResponse>?>(null)
    val serverStatusState: StateFlow<ApiResponse<ServerStatusResponse>?> = _serverStatusState

    private val _passwordResetState = MutableStateFlow<ApiResponse<String>?>(null)
    val passwordResetState: StateFlow<ApiResponse<String>?> = _passwordResetState
    
    private val _login = MutableStateFlow("")
    val login: StateFlow<String> = _login

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    fun setLogin(value: String) {
        _login.value = value
    }

    fun setPassword(value: String) {
        _password.value = value
    }

    init {
        checkServerStatus()
    }

    // Методы запросов
    fun checkServerStatus() {
        launchRequestWithState(
            state = _serverStatusState,
            request = { authRepository.checkStatus() }
        )
    }

    fun login() {
        launchRequestWithState(
            state = _authState,
            request = {
                val request = AuthRequest(_login.value, _password.value)
                authRepository.login(request)
            }
        )
    }

    fun resetPassword() {
        launchRequestWithState(
            state = _passwordResetState,
            request = {
                val request = AuthRequest(_login.value, _password.value)
                authRepository.resetPassword(request)
            }
        )
    }

    // Очистка состояния
    fun clearAuthState() {
        _authState.value = null
    }

    fun clearServerStatusState() {
        _serverStatusState.value = null
    }

    fun clearPasswordResetState() {
        _passwordResetState.value = null
    }

    fun clearInputFields() {
        _login.value = ""
        _password.value = ""
    }
}