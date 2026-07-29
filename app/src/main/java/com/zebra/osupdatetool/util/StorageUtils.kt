package com.zebra.osupdatetool.util

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.OpenableColumns
import java.io.File

/**
 * MX PowerMgr installs the BSP package from a real file system path (parm "ZipFile"),
 * a content:// URI coming from the system document picker can not be used directly.
 *
 * This object provides the two things needed to pick a BSP zip file:
 * 1. a plain file system browser (storageRoots / listEntries)
 * 2. a best effort content:// URI to file path resolver (resolveDocumentUri)
 */
object StorageUtils {

    const val ZIP_EXTENSION = ".zip"

    /**
     * Storage locations where a BSP package is usually placed.
     * The primary storage (/storage/emulated/0) comes first, then removable SD cards.
     */
    fun storageRoots(): List<File> {
        val roots = mutableListOf<File>()
        Environment.getExternalStorageDirectory()?.let { roots.add(it) }
        File("/storage").listFiles()?.forEach { volume ->
            if (volume.isDirectory
                && volume.name != "self"
                && volume.name != "emulated"
                && volume.canRead()) {
                roots.add(volume)
            }
        }
        // MX friendly staging folder, readable only on some devices
        File("/data/tmp/public").takeIf { it.canRead() }?.let { roots.add(it) }
        return roots.distinctBy { it.absolutePath }
    }

    /** Directories first, then zip files. Everything else is hidden from the picker. */
    fun listEntries(dir: File): List<File> {
        val children = dir.listFiles() ?: return emptyList()
        val dirs = children.filter { it.isDirectory && !it.isHidden }.sortedBy { it.name.lowercase() }
        val zips = children.filter { it.isFile && isZipFile(it) }.sortedBy { it.name.lowercase() }
        return dirs + zips
    }

    fun isZipFile(file: File): Boolean =
        file.name.lowercase().endsWith(ZIP_EXTENSION)

    fun hasAllFilesAccess(): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) Environment.isExternalStorageManager()
        else true

    fun humanReadableSize(bytes: Long): String {
        if (bytes < 1024) return "$bytes B"
        val units = listOf("KB", "MB", "GB")
        var value = bytes.toDouble() / 1024
        var unitIndex = 0
        while (value >= 1024 && unitIndex < units.lastIndex) {
            value /= 1024
            unitIndex++
        }
        return String.format("%.1f %s", value, units[unitIndex])
    }

    /**
     * Converts the URI returned by the system document picker into a real file path.
     *
     * @return the absolute path, or null when the document can not be mapped to a
     * file the MX service is able to read (for example a cloud provider document).
     */
    fun resolveDocumentUri(context: Context, uri: Uri): String? {
        if (uri.scheme == "file") {
            return uri.path
        }
        if (!DocumentsContract.isDocumentUri(context, uri)) {
            return null
        }
        val documentId = runCatching { DocumentsContract.getDocumentId(uri) }.getOrNull() ?: return null
        return when (uri.authority) {
            "com.android.externalstorage.documents" -> {
                // documentId looks like "primary:Download/BSP.zip" or "1A2B-3C4D:BSP.zip"
                val volume = documentId.substringBefore(':')
                val relativePath = documentId.substringAfter(':', "")
                val base = if (volume.equals("primary", ignoreCase = true)) {
                    Environment.getExternalStorageDirectory().absolutePath
                } else {
                    "/storage/$volume"
                }
                "$base/$relativePath".trimEnd('/')
            }
            "com.android.providers.downloads.documents" -> {
                if (documentId.startsWith("raw:")) {
                    documentId.removePrefix("raw:")
                } else {
                    // MediaStore backed document, look the file up by its display name
                    queryDisplayName(context, uri)?.let { findByName(it) }
                }
            }
            else -> null
        }
    }

    private fun queryDisplayName(context: Context, uri: Uri): String? =
        context.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
            ?.use { cursor ->
                if (cursor.moveToFirst()) cursor.getString(0) else null
            }

    /** Looks for [fileName] in the folders a BSP package is normally downloaded to. */
    private fun findByName(fileName: String): String? {
        storageRoots().forEach { root ->
            listOf("Download", "Downloads", "").forEach { folder ->
                val candidate = if (folder.isEmpty()) File(root, fileName) else File(File(root, folder), fileName)
                if (candidate.isFile) {
                    return candidate.absolutePath
                }
            }
        }
        return null
    }
}
