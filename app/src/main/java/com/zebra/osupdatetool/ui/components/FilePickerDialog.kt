package com.zebra.osupdatetool.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zebra.osupdatetool.util.StorageUtils
import java.io.File

/**
 * Plain file system browser listing folders and .zip files only.
 *
 * The MX Power Manager needs the absolute path of the BSP package, so the app
 * browses the storage itself instead of relying on content:// URIs.
 */
@Composable
fun FilePickerDialog(
    startDirectory: File?,
    onDismiss: () -> Unit,
    onFileSelected: (File) -> Unit
) {
    val roots = remember { StorageUtils.storageRoots() }
    var currentDirectory by remember {
        mutableStateOf(startDirectory?.takeIf { it.isDirectory } ?: roots.firstOrNull())
    }
    val entries = remember(currentDirectory) {
        currentDirectory?.let { StorageUtils.listEntries(it) } ?: emptyList()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("CLOSE")
            }
        },
        dismissButton = {
            val parent = currentDirectory?.parentFile
            val canGoUp = parent != null
                && parent.canRead()
                && roots.none { it.absolutePath == currentDirectory?.absolutePath }
            TextButton(
                enabled = canGoUp,
                onClick = { currentDirectory = parent }
            ) {
                Text("UP")
            }
        },
        title = {
            Column {
                Text("Choose BSP zip file")
                Text(
                    text = currentDirectory?.absolutePath ?: "no readable storage found",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    maxLines = 2,
                    softWrap = true,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        text = {
            Column {
                if (roots.size > 1) {
                    Row(Modifier.fillMaxWidth()) {
                        roots.forEach { root ->
                            TextButton(onClick = { currentDirectory = root }) {
                                Text(
                                    root.name.ifEmpty { root.absolutePath },
                                    maxLines = 3,
                                    softWrap = true,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                    HorizontalDivider()
                }
                if (entries.isEmpty()) {
                    Text("no folder or .zip file here")
                }
                LazyColumn(Modifier.height(360.dp)) {
                    items(entries) { entry ->
                        FileRow(entry) {
                            if (entry.isDirectory) {
                                currentDirectory = entry
                            } else {
                                onFileSelected(entry)
                            }
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    )
}

@Composable
private fun FileRow(file: File, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = when {
                !file.isDirectory -> Icons.AutoMirrored.Filled.InsertDriveFile
                file.canRead() -> Icons.Default.FolderOpen
                else -> Icons.Default.Folder
            },
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp)
        )
        Column {
            Text(file.name, fontSize = 14.sp, maxLines = 3, softWrap = true, overflow = TextOverflow.Ellipsis)
            if (!file.isDirectory) {
                Text(StorageUtils.humanReadableSize(file.length()), fontSize = 11.sp)
            }
        }
    }
}
