package com.zebra.emdk_kotlin_wrapper.oeminfo

import android.content.ContentResolver
import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper.getMainLooper
import android.util.Log
import androidx.core.net.toUri


/**
 * https://techdocs.zebra.com/oeminfo/consume/
 *
 * A utility object for retrieving OEM information from the device.
 *
 * Notes
 * Data acquired through the OEMinfo content provider is subject to the rules and behavior listed below.
 *
 * With the exception of OS Update events, OEMinfo does NOT read system properties that can change at runtime.
 * OEMinfo reads system properties only after being signaled by the BOOT_COMPLETE event.
 * After receiving BOOT_COMPLETE, OEMinfo queries selected system properties and refreshes the content provider. This generally takes a few seconds, but a delay of about one minute is typical before results of an OS Update are published to the ZDPI.
 * If an app queries OEMinfo too soon after reboot, some URIs might return "stale" data, posing a potential issue for non-static values.
 * OEMinfo requires extra time populate the content provider database when device data is wiped after an Enterprise Reset, Factory Reset or other erasure event.
 * Zebra recommends registering apps with a content observer on the URI to receive a callback whenever new data is available to avoid issues relating to stale or missing data due to re-population delays.
 * OEMinfo is “System UID” and platform-signed, and is therefore subject to platform permissions and SELinux restrictions across Android versions and devices.
 * The same set of system properties might not be readable on all devices.
 * System properties might become restricted, removed or added after an OS update.
 */
internal object OEMInfoHelper {

    private val TAG = OEMInfoHelper::class.java.simpleName

    /**
     * Retrieves OEM information, such as serial number or IMEI, from a content provider.
     * This method should be called from a background thread to avoid blocking the UI.
     *
     * @param ctx       The context to use for accessing the content resolver.
     * @param serviceId The URI of the content provider service.
     * @return The requested OEM information as a String, or null if it cannot be retrieved.
     */
    fun getOEMInfo(ctx: Context, serviceId: String): String? {
        // Use the 'use' extension function to ensure the cursor is automatically closed.
        try {
            ctx.contentResolver.query(Uri.parse(serviceId), null, null, null, null)?.use { cursor ->
                // Check if the cursor is valid and contains at least one row.
                if (cursor.moveToFirst() && cursor.columnCount > 0) {
                    return cursor.getString(0)
                }
            }
        } catch (e: Exception) {
            // Log any exceptions that occur during the query.
            Log.e(TAG, "Error querying OEM info for service: $serviceId", e)
        }
        // Return null if the query fails or no data is found.
        return null
    }

    fun observeOEMInfo(ctx: Context, serviceId: String, onChange: (String?) -> Unit) {

        // Prepare the URI
        val myUri: Uri = serviceId.toUri()

        // Register with content observer
        val cr: ContentResolver = ctx.contentResolver
        cr.registerContentObserver(
            myUri, true,
            object : ContentObserver(Handler(getMainLooper())) {
                override fun onChange(selfChange: Boolean, uri: Uri?) {
                    super.onChange(selfChange, uri)

                    // Content has changed
                    Log.d(TAG, "content has changed, uri = " + uri)

                    // Reading the data is like above example
                    val info = getOEMInfo(ctx, serviceId)
                    onChange(info)
                }
            })
    }
}
