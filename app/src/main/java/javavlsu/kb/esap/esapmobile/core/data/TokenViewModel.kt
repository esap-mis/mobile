package javavlsu.kb.esap.esapmobile.core.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javavlsu.kb.esap.esapmobile.core.domain.util.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TokenViewModel @Inject constructor(
    private val tokenManager: TokenManager,
): ViewModel() {

    val token = MutableLiveData<String?>()
    val roles = MutableLiveData<String?>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            tokenManager.getToken().collect {
                withContext(Dispatchers.Main) {
                    token.value = it
                }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            tokenManager.getRoles().collect {
                withContext(Dispatchers.Main) {
                    roles.value = it
                }
            }
        }
    }

    fun saveToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            tokenManager.saveToken(token)
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
}