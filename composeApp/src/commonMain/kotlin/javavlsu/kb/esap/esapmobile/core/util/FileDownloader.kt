package javavlsu.kb.esap.esapmobile.core.util

import kotlinx.coroutines.flow.Flow
import org.koin.core.module.Module

interface FileDownloader {

    fun downloadFile(
        url: String,
        fileName: String,
        fileExtension: String = "pdf"
    ): Flow<DownloadResult>

    fun getDownloadedFilePath(fileName: String, fileExtension: String = "pdf"): String?

    suspend fun isFileDownloaded(fileName: String, fileExtension: String = "pdf"): Boolean

    suspend fun deleteDownloadedFile(fileName: String, fileExtension: String = "pdf"): Boolean

    companion object {
        const val TAG = "FileDownloader"
    }
}

sealed class DownloadResult {
    data object Idle : DownloadResult()
    data object Starting : DownloadResult()
    data class Loading(val percentage: Int) : DownloadResult()
    data class Success(val filePath: String) : DownloadResult()
    data class Error(val message: String) : DownloadResult()
}

expect val fileDownloaderModule: Module