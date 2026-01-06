package javavlsu.kb.esap.esapmobile.core.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _errorMessage = MutableStateFlow<String>("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _genericError = MutableStateFlow<String>("")
    val genericError: StateFlow<String> = _genericError

    protected fun <T> launchRequest(
        request: suspend () -> ApiResponse<T>,
        onSuccess: (T) -> Unit,
        onLoading: (Boolean) -> Unit = {},
        showLoading: Boolean = true
    ) {
        viewModelScope.launch {
            try {
                if (showLoading) {
                    _loading.value = true
                    onLoading(true)
                }

                val result = request()

                when (result) {
                    is ApiResponse.Success -> {
                        onSuccess(result.data)
                    }
                    is ApiResponse.Failure -> {
                        _errorMessage.emit(result.errorMessage)
                    }
                    ApiResponse.Loading -> {}
                }
            } catch (e: Exception) {
                _errorMessage.emit(e.localizedMessage ?: "Произошла ошибка! Пожалуйста, попробуйте еще раз.")
            } finally {
                if (showLoading) {
                    _loading.value = false
                    onLoading(false)
                }
            }
        }
    }

    protected fun <T> launchRequestWithState(
        state: MutableStateFlow<ApiResponse<T>?>,
        showLoading: Boolean = true,
        request: suspend () -> ApiResponse<T>
    ) {
        viewModelScope.launch {
            try {
                if (showLoading) {
                    state.value = ApiResponse.Loading
                }

                val result = request()
                state.value = result

                if (result is ApiResponse.Failure) {
                    _errorMessage.emit(result.errorMessage)
                }
            } catch (e: Exception) {
                state.value = ApiResponse.Failure(
                    errorMessage = e.localizedMessage ?: "Неизвестная ошибка",
                    code = 400
                )
                _errorMessage.emit(e.localizedMessage ?: "Произошла ошибка! Пожалуйста, попробуйте еще раз.")
            }
        }
    }
}