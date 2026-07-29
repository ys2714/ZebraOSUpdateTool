package com.zebra.emdk_kotlin_wrapper.mx

import android.content.Context
import com.zebra.emdk_kotlin_wrapper.oeminfo.OEMInfoHelper
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Date

/**
 * https://techdocs.zebra.com/oeminfo/consume/
 *
 *
 * */
internal fun MXProfileProcessor.fetchProductModelInBackground(
    context: Context,
    delaySeconds: Long,
    callback: (String) -> Unit) {
    fetchOEMInfoInBackground(context, MXConst.PRODUCT_MODEL_URI, delaySeconds, callback)
}

internal fun MXProfileProcessor.fetchSerialNumberInBackground(
    context: Context,
    delaySeconds: Long,
    callback: (String) -> Unit) {
    fetchOEMInfoInBackground(context,MXConst.SERIAL_URI, delaySeconds, callback)
}

internal fun MXProfileProcessor.fetchIMEIInBackground(
    context: Context,
    delaySeconds: Long,
    callback: (String) -> Unit) {
    fetchOEMInfoInBackground(context,MXConst.IMEI_URI, delaySeconds, callback)
}

internal fun MXProfileProcessor.fetchTouchModeInBackground(
    context: Context,
    delaySeconds: Long,
    callback: (String) -> Unit) {
    fetchOEMInfoInBackground(context,MXConst.TOUCH_MODE_URI, delaySeconds, callback)
}

internal fun MXProfileProcessor.fetchVendorTouchModeInBackground(
    context: Context,
    delaySeconds: Long,
    callback: (String) -> Unit) {
    fetchOEMInfoInBackground(context,MXConst.VENDOR_TOUCH_MODE_URI, delaySeconds, callback)
}

internal fun MXProfileProcessor.fetchOEMInfoInBackground(
    context: Context,
    serviceId: String,
    delaySeconds: Long = 0,
    callback: (String) -> Unit) {
    backgroundScope.launch {
        val info = OEMInfoHelper.getOEMInfo(context, serviceId)
        if (info == null) {
            getCallServicePermission(context, serviceId, delaySeconds, { errorInfo ->
                if (errorInfo != null) {
                    callback("")
                } else {
                    val result = OEMInfoHelper.getOEMInfo(context, serviceId) ?: ""
                    foregroundScope.launch {
                        callback(result)
                    }
                }
            })
        } else {
            foregroundScope.launch {
                callback(info)
            }
        }
    }
}

/**
 * https://techdocs.zebra.com/oeminfo/consume/
 *
 * status: PASSED, FAILED, CANCELLED, IN_PROGRESS, IN_SUSPEND, WAITING_FOR_REBOOT
 * detail: Verbose text describing the status
 * ts: Returns an epoch timestamp indicating when the intent was received
 * */
internal fun MXProfileProcessor.fetchOSUpdateStatusInBackground(
    context: Context,
    callback: (MXBase.OSUpdateStatus, String, String) -> Unit) {

    backgroundScope.launch {
        val status = async { fetchOEMInfo(context, MXConst.OSUPDATE_STATUS_URI) }.await()
        val detail = async { fetchOEMInfo(context, MXConst.OSUPDATE_DETAIL_URI) }.await()
        val timestamp = async { fetchOEMInfo(context, MXConst.OSUPDATE_TIMESTAMP_URI) }.await()

        foregroundScope.launch {
            try {
                val value = MXBase.OSUpdateStatus.valueOf(status)
                callback(value, detail, timestamp)
            } catch (e: Exception) {
                callback(MXBase.OSUpdateStatus.UNKNOWN, "no data. please check again after OS update", "--:--:--")
            }
        }
    }
}

internal suspend fun MXProfileProcessor.fetchOEMInfo(
    context: Context,
    serviceId: String,
    delaySeconds: Long = 0): String = suspendCancellableCoroutine { continuation ->
    var result: Result<String>
    val info = OEMInfoHelper.getOEMInfo(context, serviceId)
    if (info == null) {
        getCallServicePermission(context, serviceId, delaySeconds, { errorInfo ->
            if (errorInfo != null) {
                result = Result.failure(RuntimeException(errorInfo.errorDescription))
            } else {
                val oemInfo = OEMInfoHelper.getOEMInfo(context, serviceId) ?: ""
                result = Result.success(oemInfo)
            }
            continuation.resumeWith(result)
        })
    } else {
        result = Result.success(info)
        continuation.resumeWith(result)
    }
}