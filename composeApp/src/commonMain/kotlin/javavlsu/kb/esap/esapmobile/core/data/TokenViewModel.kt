package javavlsu.kb.esap.esapmobile.core.data

import androidx.lifecycle.viewModelScope
import javavlsu.kb.esap.esapmobile.core.domain.network.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TokenViewModel(
    private val tokenManager: TokenManager,
): BaseViewModel() {

    val token = MutableStateFlow<String?>(null)
    val roles = MutableStateFlow<String?>(null)

    init {
        token.value = tokenManager.getToken()
        roles.value = tokenManager.getRoles()
    }

    fun saveToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            tokenManager.saveToken(token)
        }
    }

    fun saveRefreshToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            tokenManager.saveRefreshToken(token)
        }
    }

    fun deleteToken() {
        viewModelScope.launch(Dispatchers.IO) {
            tokenManager.deleteToken()
        }
    }

    fun saveRoles(roles: String) {
        viewModelScope.launch(Dispatchers.IO) {
            tokenManager.saveRoles(roles)
        }
    }

    fun deleteRoles() {
        viewModelScope.launch(Dispatchers.IO) {
            tokenManager.deleteRoles()
        }
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            tokenManager.deleteToken()
            tokenManager.deleteRefreshToken()
            tokenManager.deleteRoles()
        }
    }
}