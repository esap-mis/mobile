package javavlsu.kb.esap.esapmobile.presentation.util

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment

fun downloadFile(context: Context, pdfUrl: String, documentName: String) {
    val request = DownloadManager.Request(Uri.parse(pdfUrl))
        .setTitle(documentName)
        .setDescription("Скачивание $documentName")
        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "$documentName.pdf")
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    downloadManager.enqueue(request)
}
