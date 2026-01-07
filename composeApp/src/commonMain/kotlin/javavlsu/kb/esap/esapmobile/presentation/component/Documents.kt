package javavlsu.kb.esap.esapmobile.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import esapmobile.composeapp.generated.resources.Res
import esapmobile.composeapp.generated.resources.ic_pdf
import javavlsu.kb.esap.esapmobile.core.data.FileDownloaderViewModel
import javavlsu.kb.esap.esapmobile.core.util.DownloadResult
import javavlsu.kb.esap.esapmobile.presentation.theme.Green30
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DocumentCard(
    title: String,
    fileName: String,
    downloadUrl: String,
    downloadViewModel: FileDownloaderViewModel = koinViewModel()
) {
    val downloadState by downloadViewModel.downloadState.collectAsState()

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(8.dp)
            .clickable(
                enabled = downloadState !is DownloadResult.Loading &&
                        downloadState !is DownloadResult.Success
            ) {
                downloadViewModel.downloadFile(downloadUrl, fileName, "pdf")
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxHeight()
        ) {
            when (downloadState) {
                is DownloadResult.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                }
                is DownloadResult.Success -> {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Green30,
                        modifier = Modifier.size(28.dp)
                    )
                }
                is DownloadResult.Error -> {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(28.dp)
                    )
                }
                else -> {
                    Icon(
                        painter = painterResource(Res.drawable.ic_pdf),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier
                    .padding(vertical = 6.dp)
                    .fillMaxHeight()
                    .width(1.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = title,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.width(8.dp))

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier
                    .padding(vertical = 6.dp)
                    .fillMaxHeight()
                    .width(1.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Default.Download,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}