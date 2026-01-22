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
    val isDarkMode = networkManager.isDarkModeFlow

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val url = networkManager.getBaseUrl()
            launch(Dispatchers.Main) {
                baseUrl.value = url
            }
        }
    }

    fun setBaseUrl(newBaseUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            networkManager.setBaseUrl(newBaseUrl)
        }
    }

    fun setIsDarkMode(dark: Boolean?) {
        viewModelScope.launch(Dispatchers.IO) {
            networkManager.setIsDarkMode(dark)
        }
    }
}