package com.zebra.emdk_kotlin_wrapper.dw

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.zebra.emdk_kotlin_wrapper.dw.DWScannerMap.DWScannerInfo
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.collections.forEach

/**
 * https://techdocs.zebra.com/datawedge/15-0/guide/api/enumeratescanners/
 *
 * Wrong:
 * com.symbol.datawedge.api.ACTION_ENUMERATEDSCANNERLIST
 * Right:
 * com.symbol.datawedge.api.RESULT_ENUMERATE_SCANNERS
 * */
internal suspend fun DWAPI.sendEnumerateScannersIntent(context: Context): List<DWScannerInfo> = suspendCancellableCoroutine { continuation ->
    DWIntentFactory.callDWAPI(context, DWAPI.ActionExtraKeys.ENUMERATE_SCANNERS, "") { result ->
        result.onSuccess { intent ->
            callDWAPIEnumerateScannersHandler(intent) {
                continuation.resumeWith(Result.success(it))
            }
        }.onFailure {
            continuation.resumeWith(Result.failure(it))
        }
    }
}

/**
 * https://techdocs.zebra.com/datawedge/6-8/guide/api/getscannerstatus/
 *
 * - Parameters:
 * ACTION [String]: "com.symbol.datawedge.api.ACTION"
 * EXTRA_DATA [String]: "com.symbol.datawedge.api.GET_SCANNER_STATUS"
 * EXTRA VALUE: Empty string
 *
 * - Result Codes:
 * EXTRA NAME: "com.symbol.datawedge.api.RESULT_SCANNER_STATUS"
 * EXTRA TYPE [String]: [ ] Possible values:
 *
 * WAITING - Scanner is ready to be triggered
 * SCANNING - Scanner is emitting a scanner beam
 * DISABLED - Scanner is disabled
 * CONNECTED - An external (Bluetooth or serial) scanner is connected
 * DISCONNECTED - The external scanner is disconnected
 * Error and debug messages are logged to the Android logging system, which can be viewed and filtered by the logcat command. Use logcat from an ADB shell to view the log messages:
 * $ adb logcat -s DWAPI
 * */
internal suspend fun DWAPI.sendGetSelectedScannerStatusIntent(context: Context): DWAPI.ScannerStatus = suspendCancellableCoroutine { continuation ->
    DWIntentFactory.callDWAPI(context, DWAPI.ActionExtraKeys.GET_SCANNER_STATUS, "") { result ->
        result.onSuccess {
            callDWAPIGetSelectedScannerStatusHandler(it) { status ->
                if (status != null) {
                    continuation.resumeWith(Result.success(status))
                } else {
                    continuation.resumeWith(Result.failure(java.lang.Exception("Scanner status is null")))
                }
            }
        }.onFailure {
            continuation.resumeWith(Result.failure(it))
        }
    }
}

/**
 * https://techdocs.zebra.com/datawedge/latest/guide/api/switchdatacapture/
 *
 * Used to switch between the following at runtime:
 * 1. Barcode scanning and Barcode Highlighting, e.g. to have the ability to alternate between scanning barcodes and using the viewfinder (or preview screen) to highlight barcodes to aid in finding items.
 * 2. Workflow Input options, e.g. to scan a driver license and read a license plate in the same app.
 * */
internal suspend fun DWAPI.sendSwitchDataCaptureIntent(context: Context, plugin: DWAPI.Plugin.Input): Boolean = suspendCancellableCoroutine { continuation ->
    DWIntentFactory.callDWAPI(context, DWAPI.ActionExtraKeys.SWITCH_DATACAPTURE, plugin.value) { result ->
        result.onSuccess {
            continuation.resumeWith(Result.success(true))
        }.onFailure {
            continuation.resumeWith(Result.failure(it))
        }
    }
}

/**
 * https://techdocs.zebra.com/datawedge/15-0/guide/api/scannerinputplugin/
 *
 * - Parameters:
 * ACTION [String]: "com.symbol.datawedge.api.ACTION"
 * EXTRA_DATA [String]: "com.symbol.datawedge.api.SCANNER_INPUT_PLUGIN"
 *
 * <parameter>: The parameter as a string, using either of the following:
 *
 * SUSPEND_PLUGIN - suspends the scanner so it is temporarily inactive when switching from the WAITING or SCANNING state. SCANNER_STATUS notification broadcasts IDLE state.
 * RESUME_PLUGIN - resumes the scanner when changing from the SUSPEND_PLUGIN suspended state. SCANNER_STATUS notification broadcasts WAITING and SCANNING states, rotating between each depending on whether scanning is taking place. In the WAITING state it is expecting an action from the user such as a trigger press. In the SCANNING state it is actively performing a scan resulting from an action such as a trigger press.
 * ENABLE_PLUGIN - enables the plug-in; scanner becomes active. SCANNER_STATUS notification broadcasts WAITING and SCANNING states, rotating between each depending on whether scanning is taking place.
 * DISABLE_PLUGIN - disables the plug-in; scanner becomes inactive. SCANNER_STATUS notification broadcasts DISABLED state.
 *
 * Result Codes
 * DataWedge returns the following error codes if the app includes the intent extras SEND_RESULT and COMMAND_IDENTIFIER to enable the app to get results using the DataWedge result intent mechanism. See Example, below.
 *
 * SCANNER_ALREADY_SUSPENDED - An intent was received to suspend the scanner which is already suspended.
 * PLUGIN_DISABLED - The scanner plug-in is disabled so the suspend/resume action cannot be executed.
 * SCANNER_ALREADY_RESUMED - A resume intent is received but the scanner is not in a suspended state.
 * SCANNER_RESUME_FAILED - The scanner resume is unsuccessful.
 * SCANNER_ALREADY_DISABLED - The scanner is in a disabled state, preventing any further action.
 * DATAWEDGE_DISABLED - The action to disable DataWedge is unsuccessful.
 * PARAMETER_INVALID - An invalid parameter is received.
 * PROFILE_DISABLED - The action to to disable profile is unsuccessful.
 * SCANNER_ALREADY_DISABLED - An intent was received to disable the scanner which is already disabled.
 * SCANNER_ALREADY_ENABLED - An intent was received to enable the scanner which is already enabled.
 * SCANNER_DISABLE_FAILED - The scanner disable is unsuccessful.
 * SCANNER_ENABLE_FAILED - The action to enable the scanner is unsuccessful.
 * */
internal suspend fun DWAPI.sendControlScannerInputPluginIntent(context: Context, command: DWAPI.ControlScannerInputPluginCommand): Boolean = suspendCancellableCoroutine { continuation ->
    DWIntentFactory.callDWAPI(context, DWAPI.ActionExtraKeys.SCANNER_INPUT_PLUGIN, command.value) { result ->
        result.onSuccess {
            continuation.resumeWith(Result.success(true))
        }.onFailure {
            if (it.message == "SCANNER_ALREADY_SUSPENDED" ||
                it.message == "SCANNER_ALREADY_RESUMED" ||
                it.message == "SCANNER_ALREADY_DISABLED" ||
                it.message == "SCANNER_ALREADY_ENABLED") {
                continuation.resumeWith(Result.success(true))
            } else {
                continuation.resumeWith(Result.failure(it))
            }
        }
    }
}

private fun callDWAPIEnumerateScannersHandler(intent: Intent, callback: (List<DWScannerInfo>) -> Unit) {
    if (intent.action == DWAPI.ResultActionNames.RESULT_ACTION.value &&
        intent.hasExtra(DWAPI.ResultExtraKeys.ENUMERATE_SCANNERS.value)
    )
    {
        val bundles: ArrayList<Bundle>? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra(
                DWAPI.ResultExtraKeys.ENUMERATE_SCANNERS.value,
                Bundle::class.java) as ArrayList<Bundle>
        } else {
            intent.getParcelableArrayListExtra<Bundle>(DWAPI.ResultExtraKeys.ENUMERATE_SCANNERS.value)
        }
        bundles?.forEach { bundle ->
            val name = bundle.getString("SCANNER_NAME")
            val connected = bundle.getBoolean("SCANNER_CONNECTION_STATE")
            val index = bundle.getInt("SCANNER_INDEX")
            val id = bundle.getString("SCANNER_IDENTIFIER")
            if (id != null && name != null) {
                DWScannerMap.setScannerInfo(id, name, index, connected)
                Log.d(DataWedgeHelper.TAG, "ScannerInfo: [name: $name, connected: $connected, index: $index, id: $id]")
            }
        }
        Log.d(DataWedgeHelper.TAG, "LIST SCANNER SUCCESS")
        callback(DWScannerMap.getScannerList())
    } else {
        Log.e(DataWedgeHelper.TAG, "LIST SCANNER FAILED: no dw status in intent")
        callback(emptyList())
    }
}

private fun callDWAPIGetSelectedScannerStatusHandler(intent: Intent, callback: (DWAPI.ScannerStatus?) -> Unit) {
    if (intent.action == DWAPI.ResultActionNames.RESULT_ACTION.value &&
        intent.hasExtra(DWAPI.ResultExtraKeys.SCANNER_STATUS.value)
    )
    {
        val status = intent.getStringExtra(DWAPI.ResultExtraKeys.SCANNER_STATUS.value)
        val statusEnum = DWAPI.ScannerStatus.entries.find { it.value == status }
        if (statusEnum != null) {
            callback(statusEnum)
        } else {
            callback(null)
        }
    } else {
        Log.e(DataWedgeHelper.TAG, "LIST SCANNER FAILED: no dw status in intent")
        callback(null)
    }
}