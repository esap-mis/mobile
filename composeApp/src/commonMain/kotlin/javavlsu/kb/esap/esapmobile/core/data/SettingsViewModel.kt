package javavlsu.kb.esap.esapmobile.core.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javavlsu.kb.esap.esapmobile.core.domain.network.NetworkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val networkManager: NetworkManager
): ViewModel() {
    val baseUrl = MutableStateFlow<String?>(null)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            baseUrl.value = networkManager.getBaseUrl()
        }
    }

    fun setBaseUrl(newBaseUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            networkManager.setBaseUrl(newBaseUrl)
        }
    }
}