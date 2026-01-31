package javavlsu.kb.esap.esapmobile.core.data

import androidx.lifecycle.viewModelScope
import javavlsu.kb.esap.esapmobile.core.util.DownloadResult
import javavlsu.kb.esap.esapmobile.core.util.FileDownloader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FileDownloaderViewModel(
    private val fileDownloader: FileDownloader
) : BaseViewModel() {

    private val _downloadState = MutableStateFlow<DownloadResult>(DownloadResult.Idle)
    val downloadState: StateFlow<DownloadResult> = _downloadState.asStateFlow()

    private val _downloadedFiles = MutableStateFlow<List<String>>(emptyList())
    val downloadedFiles: StateFlow<List<String>> = _downloadedFiles.asStateFlow()

    fun downloadFile(url: String, fileName: String, fileExtension: String = "pdf") {
        viewModelScope.launch {
            _downloadState.value = DownloadResult.Idle

            fileDownloader.downloadFile(url, fileName, fileExtension)
                .collect { result ->
                    _downloadState.value = result

                    when (result) {
                        is DownloadResult.Success -> {
                            refreshDownloadedFiles()
                        }
                        else -> {}
                    }
                }
        }
    }

    fun refreshDownloadedFiles() {
        // В реальном приложении нужно сканировать папку загрузок
        // Для простоты пока оставляем пустым
    }

    fun openFile(filePath: String) {
        viewModelScope.launch {
            // Платформенно-специфичная логика открытия файла
            // Можно реализовать через expect/actual
        }
    }

    fun cancelDownload() {
        // Отмена скачивания (нужна реализация)
    }
}