package com.zebra.emdk_kotlin_wrapper.utils

import android.content.Context
import android.os.DropBoxManager
import android.util.Log
import androidx.annotation.Keep
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Keep
object AndroidDropboxLogger {

    @Keep
    fun getDropBoxLogs(context: Context) {
        val dropBoxManager = context.getSystemService(Context.DROPBOX_SERVICE) as? DropBoxManager
        if (dropBoxManager == null) {
            Log.e("DropBoxReader", "Could not get DropBoxManager service.")
            return
        }

        // A timestamp to start from, e.g., 0L for all logs since the epoch.
        var timestamp = 0L
        val logStringBuilder = StringBuilder()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        Log.d("DropBoxReader", "Starting to read DropBox entries...")

        while (true) {
            // Get the next entry after the given timestamp
            val entry: DropBoxManager.Entry? = dropBoxManager.getNextEntry(null, timestamp)
            if (entry == null) {
                Log.d("DropBoxReader", "No more entries found.")
                break // Exit the loop when there are no more entries
            }

            val entryTime = Date(entry.timeMillis)
            val entryTag = entry.tag

            logStringBuilder.append("========================================\n")
            logStringBuilder.append("Tag: $entryTag\n")
            logStringBuilder.append("Time: ${dateFormat.format(entryTime)}\n")
            logStringBuilder.append("----------------------------------------\n")

            // Read the text content of the entry
            val entryText = entry.getText(500) // Read up to 500 characters
            if (entryText != null) {
                logStringBuilder.append(entryText)
                logStringBuilder.append("\n")
            } else {
                logStringBuilder.append("(No text content or content is not text)\n")
            }

            // Always close the entry to release resources
            entry.close()

            // Update the timestamp to the current entry's time to get the next one in the following loop
            timestamp = entry.timeMillis
        }

        // Print all collected logs to Logcat
        if (logStringBuilder.isNotEmpty()) {
            Log.i("DropBoxLogs", logStringBuilder.toString())
        } else {
            Log.i("DropBoxLogs", "No DropBox logs were found.")
        }
    }

}