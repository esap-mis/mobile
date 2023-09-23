package javavlsu.kb.esap.esapmobile.data

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javavlsu.kb.esap.esapmobile.network.ApiService
import javavlsu.kb.esap.esapmobile.network.model.AuthRequest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {
    var login = mutableStateOf("")
    var password = mutableStateOf("")

    fun performLogin(username: String, password: String, onResult: (String) -> Unit) {
        val loginRequest = AuthRequest(username, password)

        viewModelScope.launch {
            try {
                val response = apiService.login(loginRequest)
                if (response.isSuccessful) {
                    val token = response.body()?.jwt ?: ""
                    onResult("Success: $token")
                } else {
                    val error = response.errorBody()?.string()
                    onResult(error ?: "Login error")
                }
            } catch (e: Exception) {
                onResult("Network error: ${e.message}")
            }
        }
    }
}