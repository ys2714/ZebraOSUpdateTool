package com.zebra.emdk_kotlin_wrapper.utils

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.Keep
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException
import java.nio.charset.StandardCharsets

@Keep
object FileUtils {

    private val TAG = FileUtils::class.java.simpleName

    private val backgroundScope = CoroutineScope(Dispatchers.IO + Job())
    private val foregroundScope = CoroutineScope(Dispatchers.Main + Job())

    /**
     * Saves a text string to a file in the public "Download" directory.
     * This method is compatible with Android 10 (API 29) and newer versions,
     * using the MediaStore API to comply with Scoped Storage policies.
     *
     * @param context   The context to access ContentResolver.
     * @param fileName  The desired name of the file (e.g., "my-document.txt").
     * @param fileContent The text content to write into the file.
     */
    @Keep
    fun saveTextToDownloads(context: Context, fileName: String, fileContent: String) {
        // The implementation for Android 10 (API 29) and above is unified using MediaStore.
        // There is no need for conditional logic for versions 10, 11, 12, 13, or 14
        // as this approach is the standard for all of them.

        // Use ContentResolver to interact with the MediaStore.
        val resolver = context.contentResolver

        // ContentValues stores the file's metadata.
        val contentValues = ContentValues().apply {
            // Set the file's display name. This is the name shown to the user.
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)

            // Set the file's MIME type.
            put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")

            // Set the relative path for the file within the Downloads directory.
            // This ensures the file is placed in the correct public folder.
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        // Get the URI for the MediaStore's Downloads collection.
        val collectionUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI

        // Insert the new file metadata into the MediaStore, which returns a URI for the new file.
        val fileUri = resolver.insert(collectionUri, contentValues)

        if (fileUri == null) {
            // This can happen if the system fails to create the file entry.
            Toast.makeText(context, "Failed to create file in Downloads", Toast.LENGTH_SHORT).show()
            return
        }

        // Use a try-with-resources block to automatically close the OutputStream.
        try {
            resolver.openOutputStream(fileUri)?.use { outputStream ->
                // Write the text content to the file.
                outputStream.write(fileContent.toByteArray(StandardCharsets.UTF_8))
                toastMessage(context, "File saved to Downloads folder")
            } ?: throw IOException("Failed to open output stream for $fileUri")
        } catch (e: IOException) {
            // Handle potential I/O errors, for example, if storage is full.
            toastMessage(context, "Error saving file: ${e.message}")
            // If an error occurs, it's good practice to delete the incomplete file entry.
            resolver.delete(fileUri, null, null)
        }
    }

    private fun toastMessage(context: Context, message: String) {
        Log.d(TAG, message)
        foregroundScope.launch {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}
