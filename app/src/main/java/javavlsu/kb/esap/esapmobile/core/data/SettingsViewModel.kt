package javavlsu.kb.esap.esapmobile.core.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javavlsu.kb.esap.esapmobile.core.domain.util.BaseUrlInterceptor
import javavlsu.kb.esap.esapmobile.core.domain.util.NetworkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val networkManager: NetworkManager,
    private val baseUrlInterceptor: BaseUrlInterceptor
): ViewModel() {
    val baseUrl = MutableLiveData<String>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            networkManager.getBaseUrl().collect {
                withContext(Dispatchers.Main) {
                    baseUrl.value = it
                }
            }
        }
    }

    fun setBaseUrl(newBaseUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            networkManager.setBaseUrl(newBaseUrl)
        }
    }

    fun changeBaseUrl(newUrl: String) {
        baseUrlInterceptor.setCustomBaseUrl(newUrl);
    }
}