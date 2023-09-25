package javavlsu.kb.esap.esapmobile.data

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javavlsu.kb.esap.esapmobile.domain.model.AuthRequest
import javavlsu.kb.esap.esapmobile.domain.model.AuthResponse
import javavlsu.kb.esap.esapmobile.domain.repository.AuthRepository
import javavlsu.kb.esap.esapmobile.util.ApiResponse
import javax.inject.Inject
import androidx.compose.runtime.State

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
): BaseViewModel() {

    private val _authResponse = MutableLiveData<ApiResponse<AuthResponse>>()
    val authResponse = _authResponse

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

    fun login(coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _authResponse,
        coroutinesErrorHandler
    ) {
        val request = AuthRequest(_login.value, _password.value)
        authRepository.login(request)
    }
}