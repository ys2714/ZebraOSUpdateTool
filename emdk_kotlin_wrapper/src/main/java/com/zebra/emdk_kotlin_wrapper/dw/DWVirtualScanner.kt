package com.zebra.emdk_kotlin_wrapper.dw

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.annotation.Keep
import kotlinx.coroutines.suspendCancellableCoroutine

@Keep
abstract class DWVirtualScanner(open val context: Context) {

    val TAG = "DWVirtualScanner"

    abstract val createJSONFileName: String
    abstract val updateJSONFileName: String
    abstract val parameters: Map<String, String>

    val profileName: String?
        get() = parameters["PROFILE_NAME"]

    private var dataListener: DataWedgeHelper.ScanDataListener? = null
    private var statusListener: DataWedgeHelper.SessionStatusListener? = null

    @Keep
    open suspend fun asyncOpen(): Boolean = suspendCancellableCoroutine { continuation ->
        DataWedgeHelper.configWithJSON(
            context,
            createJSONFileName,
            parameters
        ) { success ->
            if (success) {
                if (profileName != null) {
                    continuation.resumeWith(Result.success(true))
                } else {
                    continuation.resumeWith(Result.failure(RuntimeException("$TAG profileName is null")))
                }
            } else {
                continuation.resumeWith(Result.failure(RuntimeException("$TAG open failed: $createJSONFileName")))
            }
        }
    }

    @Keep
    open fun open(completion: ((DWVirtualScanner) -> Unit)? = null): DWVirtualScanner {
        DataWedgeHelper.configWithJSON(
            context,
            createJSONFileName,
            parameters
        ) { success ->
            if (success) {
                if (profileName != null) {
                    completion?.invoke(this)
                } else {
                    throw RuntimeException("$TAG profileName is null")
                }
            } else {
                throw RuntimeException("$TAG open failed: $createJSONFileName")
            }
        }
        return this
    }

    @Keep
    open fun update(params: Map<String, String>, completion: ((DWVirtualScanner) -> Unit)? = null): DWVirtualScanner {
        DataWedgeHelper.configWithJSON(
            context,
            updateJSONFileName,
            params
        ) { success ->
            if (success) {
                if (profileName != null) {
                    completion?.invoke(this)
                } else {
                    throw RuntimeException("$TAG profileName is null")
                }
            } else {
                throw RuntimeException("$TAG open failed: $updateJSONFileName")
            }
        }
        return this
    }

    @Keep
    open fun select(completion: ((DWVirtualScanner) -> Unit)? = null): DWVirtualScanner {
        profileName?.let {
            DataWedgeHelper.switchProfile(context, it) {
                completion?.invoke(this)
            }
        }
        return this
    }

    @Keep
    open fun suspend(): DWVirtualScanner {
        DataWedgeHelper.controlScannerInputPlugin(context, DWAPI.ControlScannerInputPluginCommand.SUSPEND_PLUGIN)
        return this
    }

    @Keep
    open fun resume(): DWVirtualScanner {
        DataWedgeHelper.controlScannerInputPlugin(context, DWAPI.ControlScannerInputPluginCommand.RESUME_PLUGIN)
        return this
    }

    @Keep
    open fun enable(): DWVirtualScanner {
        DataWedgeHelper.controlScannerInputPlugin(context, DWAPI.ControlScannerInputPluginCommand.ENABLE_PLUGIN)
        return this
    }

    @Keep
    open fun disable(): DWVirtualScanner {
        DataWedgeHelper.controlScannerInputPlugin(context, DWAPI.ControlScannerInputPluginCommand.DISABLE_PLUGIN)
        return this
    }

    @Keep
    open fun switchParamenter(key: String, value: String): DWVirtualScanner {
        DataWedgeHelper.switchScannerParams(
            context,
            Bundle().apply {
                putString(key, value)
            }
        )
        return this
    }

    @Keep
    open fun startListen(onData: (String) -> Unit) {
        if (dataListener != null) {
            return
        }
        dataListener = object : DataWedgeHelper.ScanDataListener {
            override fun onData(
                type: String,
                value: String,
                timestamp: String
            ) {
                onData(value)
            }

            override fun getID(): String {
                return hashCode().toString()
            }

            override fun onDisposal() {
                dataListener = null
            }
        }.also {
            DataWedgeHelper.addScanDataListener(it)
        }
    }

    @Keep
    open fun stopListen() {
        if (dataListener != null) {
            DataWedgeHelper.removeScanDataListener(dataListener!!)
            dataListener = null
        }
    }

    @Keep
    open fun startListenStatus(onValue: (DWAPI.NotificationType, String) -> Unit) {
        if (statusListener != null) {
            return
        }
        statusListener = object : DataWedgeHelper.SessionStatusListener {
            override fun onStatus(
                type: DWAPI.NotificationType,
                status: String,
                profileName: String
            ) {
                onValue(type, status)
            }

            override fun getID(): String {
                return hashCode().toString()
            }

            override fun onDisposal() {
                statusListener = null
            }
        }.also {
            DataWedgeHelper.addSessionStatusListener(it)
            DataWedgeHelper.enableScannerStatusNotification(context)
            DataWedgeHelper.enableWorkflowStatusNotification(context)
        }
    }

    @Keep
    open fun stopListenStatus() {
        if (statusListener != null) {
            DataWedgeHelper.removeSessionStatusListener(statusListener!!)
            DataWedgeHelper.disableScannerStatusNotification(context)
            DataWedgeHelper.disableWorkflowStatusNotification(context)
            statusListener = null
        }
    }

    @Keep
    open fun startScan(): DWVirtualScanner {
        DataWedgeHelper.softScanTrigger(
            context,
            DWAPI.SoftScanTriggerOptions.START_SCANNING
        )
        return this
    }

    @Keep
    open fun stopScan(): DWVirtualScanner {
        DataWedgeHelper.softScanTrigger(
            context,
            DWAPI.SoftScanTriggerOptions.STOP_SCANNING
        )
        return this
    }

    @Keep
    open fun close(): DWVirtualScanner {
        profileName?.let {
            dataListener?.onDisposal()
            DataWedgeHelper.deleteProfile(context, it) {
                Log.d(TAG, "close DWVirtualScanner success: $it")
            }
        }
        return this
    }
}
