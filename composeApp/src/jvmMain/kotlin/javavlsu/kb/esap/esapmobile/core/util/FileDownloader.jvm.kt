package javavlsu.kb.esap.esapmobile.core.util

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.koin.core.module.Module
import org.koin.dsl.module
import java.io.File
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

class DesktopFileDownloader : FileDownloader {

    companion object {
        private val logger = KotlinLogging.logger {}
    }

    private val downloadsDir = File(System.getProperty("user.home"), "Downloads/ESAP")

    init {
        if (!downloadsDir.exists()) {
            downloadsDir.mkdirs()
        }
    }

    override fun downloadFile(
        url: String,
        fileName: String,
        fileExtension: String
    ): Flow<DownloadResult> = flow {
        try {
            emit(DownloadResult.Starting)

            val fullFileName = "$fileName.$fileExtension"
            val destinationFile = File(downloadsDir, fullFileName)

            logger.info { "Starting file download: $url to ${destinationFile.absolutePath}" }

            withContext(Dispatchers.IO) {
                val urlConnection = URL(url).openConnection().apply {
                    connectTimeout = 30_000
                    readTimeout = 30_000
                }

                urlConnection.getInputStream().use { inputStream ->
                    Files.copy(
                        inputStream,
                        Paths.get(destinationFile.toURI()),
                        StandardCopyOption.REPLACE_EXISTING
                    )
                }
            }

            logger.info { "File successfully downloaded: ${destinationFile.absolutePath}" }
            emit(DownloadResult.Success(destinationFile.absolutePath))

        } catch (e: Exception) {
            logger.error(e) { "Error while downloading file" }
            emit(DownloadResult.Error(e.message ?: "File download error"))
        }
    }

    override fun getDownloadedFilePath(fileName: String, fileExtension: String): String? {
        val fullFileName = "$fileName.$fileExtension"
        val file = File(downloadsDir, fullFileName)
        return if (file.exists()) file.absolutePath else null
    }

    override suspend fun isFileDownloaded(fileName: String, fileExtension: String): Boolean {
        return getDownloadedFilePath(fileName, fileExtension) != null
    }

    override suspend fun deleteDownloadedFile(fileName: String, fileExtension: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val filePath = getDownloadedFilePath(fileName, fileExtension)
                filePath?.let {
                    File(it).delete()
                } ?: false
            } catch (e: Exception) {
                logger.error(e) { "Error while deleting file" }
                false
            }
        }
    }
}

actual val fileDownloaderModule: Module = module {
    single<FileDownloader> { DesktopFileDownloader() }
}