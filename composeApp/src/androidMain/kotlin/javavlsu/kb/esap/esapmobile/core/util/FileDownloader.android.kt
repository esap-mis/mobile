package javavlsu.kb.esap.esapmobile.core.util

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.koin.core.module.Module
import org.koin.dsl.module
import java.io.File

class AndroidFileDownloader(
    private val context: Context
) : FileDownloader {

    override fun downloadFile(
        url: String,
        fileName: String,
        fileExtension: String
    ): Flow<DownloadResult> = callbackFlow {
        try {
            send(DownloadResult.Starting)

            val fullFileName = "$fileName.$fileExtension"
            val destinationDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val destinationFile = File(destinationDir, fullFileName)

            val request = DownloadManager.Request(Uri.parse(url))
                .setTitle(fileName)
                .setDescription("Скачивание $fileName")
                .setDestinationUri(Uri.fromFile(destinationFile))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val downloadId = downloadManager.enqueue(request)

            send(DownloadResult.Success(destinationFile.absolutePath))
            close()

        } catch (e: Exception) {
            send(DownloadResult.Error(e.message ?: "Ошибка скачивания"))
            close(e)
        }
    }

    override fun getDownloadedFilePath(fileName: String, fileExtension: String): String? {
        val fullFileName = "$fileName.$fileExtension"
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            fullFileName
        )
        return if (file.exists()) file.absolutePath else null
    }

    override suspend fun isFileDownloaded(fileName: String, fileExtension: String): Boolean {
        return getDownloadedFilePath(fileName, fileExtension) != null
    }

    override suspend fun deleteDownloadedFile(fileName: String, fileExtension: String): Boolean {
        return try {
            val filePath = getDownloadedFilePath(fileName, fileExtension)
            filePath?.let {
                File(it).delete()
            } ?: false
        } catch (e: Exception) {
            false
        }
    }
}

actual val fileDownloaderModule: Module = module {
    single<FileDownloader> { AndroidFileDownloader(get()) }
}