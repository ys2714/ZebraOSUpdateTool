package com.zebra.emdk_kotlin_wrapper.dw

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.Keep
import androidx.core.content.ContextCompat
import com.zebra.emdk_kotlin_wrapper.utils.AssetsReader
import com.zebra.emdk_kotlin_wrapper.utils.FixedSizeQueue
import com.zebra.emdk_kotlin_wrapper.utils.FixedSizeQueueItem
import com.zebra.emdk_kotlin_wrapper.utils.JsonUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date

@Keep
object DataWedgeHelper {

    final val TAG = "DataWedgeHelper"

    @Keep
    interface ScanDataListener: FixedSizeQueueItem {
        fun onData(type: String, value: String, timestamp: String)
    }

    // please return the DWAPI.NotificationType.value in getID() method
    @Keep
    interface SessionStatusListener: FixedSizeQueueItem {
        fun onStatus(type: DWAPI.NotificationType, status: String, profileName: String)
    }

    // this singleton receiver will redirect result to listeners
    private var dataReceiver: DataReceiver? = null
    private var notificationReveiver: NotificationReceiver? = null

    private val scanDataListeners = FixedSizeQueue<ScanDataListener>(50)
    private val sessionStatusListeners = FixedSizeQueue<SessionStatusListener>(50)

    private val backgroundScope = CoroutineScope(Dispatchers.IO + Job())
    private val foregroundScope = CoroutineScope(Dispatchers.Main + Job())

    private fun notifyDataListeners(type: String, value: String, timestamp: String) {
        scanDataListeners.items.forEach {
            it.onData(type, value, timestamp)
        }
    }

    private fun notifyStatusListeners(type: DWAPI.NotificationType, status: String, profileName: String) {
        sessionStatusListeners.items.forEach {
            it.onStatus(type, status, profileName)
        }
    }

    @Keep
    fun addScanDataListener(listener: ScanDataListener) {
        scanDataListeners.enqueue(listener)
    }

    @Keep
    fun removeScanDataListener(listener: ScanDataListener) {
        scanDataListeners.remove(listener)
    }

    @Keep
    fun addSessionStatusListener(listener: SessionStatusListener) {
        sessionStatusListeners.enqueue(listener)
    }

    @Keep
    fun removeSessionStatusListener(listener: SessionStatusListener) {
        sessionStatusListeners.remove(listener)
    }

    @Keep
    fun prepare(context: Context, callback: (Boolean) -> Unit) {
        registerReceiverIfNeeded(context)
        backgroundScope.launch {
            runCatching {
                var enabled = false
                while (!enabled) {
                    val status1 = async { DWAPI.enableDW(context, true) }
                    status1.await()
                    val status2 = async { DWAPI.sendGetDWStatusIntent(context) }
                    enabled = status2.await()
                }
                enabled
            }.onSuccess { enabled ->
                val scannerList = DWAPI.sendEnumerateScannersIntent(context)
                if (scannerList.isEmpty()) {
                    Log.e(TAG, "Scanner List empty. device should at least have one internal camera")
                    foregroundScope.launch {
                        callback(false)
                    }
                } else {
                    foregroundScope.launch {
                        callback(enabled)
                    }
                }
            }.onFailure {
                Log.e(TAG, "DW NOT ENABLED. Exception: ${it.message}")
                foregroundScope.launch {
                    callback(false)
                }
            }
        }
    }

    @Keep
    fun checkDWStatus(context: Context, callback: (enabled: Boolean) -> Unit) {
        backgroundScope.launch {
            runCatching {
                DWAPI.sendGetDWStatusIntent(context)
            }.onSuccess { enabled ->
                delay(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS)
                foregroundScope.launch {
                    callback(enabled)
                }
            }.onFailure {
                Log.e(TAG, "DW NOT ENABLED. Exception: ${it.message}")
                delay(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS)
                foregroundScope.launch {
                    callback(false)
                }
            }
        }
    }

    @Keep
    fun enableDW(context: Context, callback: ((success: Boolean) -> Unit)? = null) {
        backgroundScope.launch {
            runCatching {
                DWAPI.enableDW(context, true)
            }.onSuccess {
                delay(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS)
                foregroundScope.launch {
                     callback?.invoke(true)
                }
            }.onFailure {
                delay(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS)
                foregroundScope.launch {
                    if (it.message == DWAPI.ResultCodes.DATAWEDGE_ALREADY_ENABLED.value) {
                        Log.d(TAG, "DATAWEDGE ALREADY ENABLED")
                        callback?.invoke(true)
                    } else {
                        Log.e(TAG, "ENABLE DW FAIL. Exception: ${it.message}")
                        callback?.invoke(false)
                    }
                }
            }
        }
    }

    @Keep
    fun disableDW(context: Context, callback: ((success: Boolean) -> Unit)? = null) {
        backgroundScope.launch {
            runCatching {
                DWAPI.enableDW(context, false)
            }.onSuccess {
                delay(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS)
                foregroundScope.launch {
                    callback?.invoke(true)
                }
            }.onFailure {
                delay(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS)
                foregroundScope.launch {
                    if (it.message == DWAPI.ResultCodes.DATAWEDGE_ALREADY_DISABLED.value) {
                        Log.d(TAG, "DATAWEDGE ALREADY DISABLED")
                        callback?.invoke(true)
                    } else {
                        Log.e(TAG, "DISABLE DW FAIL. Exception: ${it.message}")
                        callback?.invoke(false)
                    }
                }
            }
        }
    }

    @Keep
    fun enableScannerStatusNotification(context: Context, callback: ((success: Boolean) -> Unit)? = null) {
        DWAPI.registerNotification(context, DWAPI.NotificationType.SCANNER_STATUS) {
            callback?.invoke(it)
        }
    }

    @Keep
    fun disableScannerStatusNotification(context: Context, callback: ((success: Boolean) -> Unit)? = null) {
        DWAPI.unregisterNotification(context, DWAPI.NotificationType.SCANNER_STATUS) {
            callback?.invoke(it)
        }
    }

    @Keep
    fun enableWorkflowStatusNotification(context: Context, callback: ((success: Boolean) -> Unit)? = null) {
        DWAPI.registerNotification(context, DWAPI.NotificationType.WORKFLOW_STATUS) {
            callback?.invoke(it)
        }
    }

    @Keep
    fun disableWorkflowStatusNotification(context: Context, callback: ((success: Boolean) -> Unit)? = null) {
        DWAPI.unregisterNotification(context, DWAPI.NotificationType.WORKFLOW_STATUS) {
            callback?.invoke(it)
        }
    }

    @Keep
    fun createProfile(context: Context, name: String, callback: ((Boolean) -> Unit)? = null) {
        backgroundScope.launch {
            runCatching {
                DWAPI.sendCreateProfileIntent(context, name)
            }.onSuccess { success ->
                delay(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS)
                foregroundScope.launch {
                    callback?.invoke(success)
                }
            }.onFailure {
                delay(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS)
                foregroundScope.launch {
                    if (it.message == DWAPI.ResultCodes.PROFILE_NOT_FOUND.value) {
                        Log.d(TAG, "PROFILE ALREADY DELETED")
                        callback?.invoke(true)
                    }
                    if (it.message == DWAPI.ResultCodes.PROFILE_ALREADY_EXISTS.value) {
                        Log.d(TAG, "PROFILE ALREADY EXISTS")
                        callback?.invoke(true)
                    } else {
                        Log.e(TAG, "CREATE PROFILE FAIL. Exception: ${it.message}")
                        callback?.invoke(false)
                    }
                }
            }
        }
    }

    @Keep
    fun getProfile(context: Context, name: String, callback: ((Bundle) -> Unit)? = null) {
        backgroundScope.launch {
            runCatching {
                DWAPI.sendGetConfigIntent(context, Bundle().apply {
                    putString("PROFILE_NAME", name)
                    putBundle("PLUGIN_CONFIG",
                        Bundle().apply {
                            putStringArrayList("PLUGIN_NAME", arrayListOf<String>(
                                "BARCODE",
                                "MSR",
                                "RFID",
                                "SERIAL",
                                "VOICE",
                                "WORKFLOW",
                                "INTENT",
                                "KEYSTROKE",
                                "IP",
                                "DCP",
                                "EKB"
                            ))
                        }
                    )
                })
            }.onSuccess { bundle ->
                callback?.invoke(bundle)
            }.onFailure {
                callback?.invoke(Bundle())
            }
        }
    }

    @Keep
    fun deleteProfile(context: Context, name: String, callback: ((Boolean) -> Unit)? = null) {
        backgroundScope.launch {
            runCatching {
                DWAPI.sendDeleteProfileIntent(context, name)
            }.onSuccess { success ->
                delay(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS)
                foregroundScope.launch {
                    callback?.invoke(success)
                }
            }.onFailure {
                delay(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS)
                foregroundScope.launch {
                    if (it.message == DWAPI.ResultCodes.PROFILE_NOT_FOUND.value) {
                        callback?.invoke(true)
                    } else {
                        Log.e(TAG, "DELETE PROFILE FAIL. Exception: ${it.message}")
                        callback?.invoke(false)
                    }
                }
            }
        }
    }

    @Keep
    fun switchProfile(context: Context, name: String, callback: ((Boolean) -> Unit)? = null) {
        backgroundScope.launch {
            runCatching {
                DWAPI.sendSwitchProfileIntent(context, name)
            }.onSuccess { success ->
                foregroundScope.launch {
                    callback?.invoke(success)
                }
            }.onFailure {
                foregroundScope.launch {
                    Log.e(TAG, "SWITCH PROFILE FAIL. Exception: ${it.message}")
                    callback?.invoke(false)
                }
            }
        }
    }

    @Keep
    fun bindProfileToApp(context: Context, name: String, packageName: String, callback: ((Boolean) -> Unit)? = null) {
        backgroundScope.launch {
            runCatching {
                val bundle = DWProfileProcessor.bundleForBindProfile(context, name, packageName)
                DWAPI.sendSetConfigIntent(context, bundle)
            }.onSuccess {
                delay(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS)
                foregroundScope.launch {
                    callback?.invoke(true)
                }
            }.onFailure {
                if (it.message == "APP_ALREADY_ASSOCIATED") {
                    delay(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS)
                    foregroundScope.launch {
                        callback?.invoke(true)
                    }
                } else {
                    delay(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS)
                    foregroundScope.launch {
                        callback?.invoke(false)
                    }
                }
            }
        }
    }

    @Keep
    fun configBarcodePlugin(context: Context, name: String, enable: Boolean, hardTrigger: Boolean,callback: ((Boolean) -> Unit)? = null) {
        backgroundScope.launch {
            runCatching {
                val bundle = DWProfileProcessor.bundleForBarcodePlugin(
                    context,
                    name,
                    enable,
                    hardTrigger,
                    DWAPI.ScanInputModeOptions.SINGLE)
                DWAPI.sendSetConfigIntent(context, bundle)
            }.onSuccess {
                delay(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS)
                foregroundScope.launch {
                    callback?.invoke(true)
                }
            }.onFailure {
                delay(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS)
                foregroundScope.launch {
                    callback?.invoke(false)
                }
            }
        }
    }

    @Keep
    fun configWorkflowPlugin(context: Context, name: String, enable: Boolean,callback: ((Boolean) -> Unit)? = null) {
        backgroundScope.launch {
            runCatching {
                val bundle = DWProfileProcessor.bundleForWorkflowPlugin(
                    context,
                    name, enable)
                DWAPI.sendSetConfigIntent(context, bundle)
            }.onSuccess {
                delay(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS)
                foregroundScope.launch {
                    callback?.invoke(true)
                }
            }.onFailure {
                delay(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS)
                foregroundScope.launch {
                    callback?.invoke(false)
                }
            }
        }
    }

    @Keep
    fun configKeystrokePlugin(context: Context, name: String, enable: Boolean, callback: ((Boolean) -> Unit)? = null) {
        backgroundScope.launch {
            runCatching {
                val bundle = DWProfileProcessor.bundleForKeystrokePlugin(context, name, enable)
                DWAPI.sendSetConfigIntent(context, bundle)
            }.onSuccess {
                delay(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS)
                foregroundScope.launch {
                    callback?.invoke(true)
                }
            }.onFailure {
                delay(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS)
                foregroundScope.launch {
                    callback?.invoke(false)
                }
            }
        }
    }

    @Keep
    fun configIntentPlugin(context: Context, name: String, callback: ((Boolean) -> Unit)? = null) {
        backgroundScope.launch {
            runCatching {
                val bundle = DWProfileProcessor.bundleForIntentPlugin(
                    context,
                    name,
                    DWAPI.ResultActionNames.SCAN_RESULT_ACTION,
                    DWAPI.ResultCategoryNames.CATEGORY_DEFAULT,
                    DWAPI.IntentDeliveryOptions.BROADCAST
                )
                DWAPI.sendSetConfigIntent(context, bundle)
            }.onSuccess {
                delay(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS)
                foregroundScope.launch {
                    callback?.invoke(true)
                }
            }.onFailure {
                delay(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS)
                foregroundScope.launch {
                    callback?.invoke(false)
                }
            }
        }
    }

    @Keep
    fun configWithJSON(context: Context, fileName: String, params: Map<String, String>, callback: ((Boolean) -> Unit)? = null) {
        backgroundScope.launch {
            runCatching {
                val jsonString = AssetsReader.readFileToStringWithParams(
                    context,
                    fileName,
                    params
                )
                val bundle = JsonUtils.jsonToBundle(jsonString)
                DWAPI.sendSetConfigIntent(context, bundle)
            }.onSuccess {
                delay(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS)
                foregroundScope.launch {
                    callback?.invoke(true)
                }
            }.onFailure {
                delay(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS)
                foregroundScope.launch {
                    callback?.invoke(false)
                }
            }
        }
    }

    @Keep
    fun switchScannerParams(context: Context, bundle: Bundle, callback: ((Boolean) -> Unit)? = null) {
        backgroundScope.launch {
            runCatching {
                DWAPI.sendSwitchScannerParamsIntent(context, bundle)
            }.onSuccess {
                foregroundScope.launch {
                    callback?.invoke(true)
                }
            }.onFailure {
                foregroundScope.launch {
                    callback?.invoke(false)
                }
            }
        }
    }

    @Keep
    fun controlScannerInputPlugin(context: Context, command: DWAPI.ControlScannerInputPluginCommand, callback: ((Boolean) -> Unit)? = null) {
        backgroundScope.launch {
            runCatching {
                DWAPI.sendControlScannerInputPluginIntent(context, command)
            }.onSuccess {
                foregroundScope.launch {
                    callback?.invoke(true)
                }
            }.onFailure {
                foregroundScope.launch {
                    callback?.invoke(false)
                }
            }
        }
    }

    @Keep
    fun softScanTrigger(context: Context, option: DWAPI.SoftScanTriggerOptions) {
        DWAPI.softScanTrigger(context, option)
    }

    @Keep
    fun getScannerInfo(context: Context, id: String): DWScannerMap.DWScannerInfo? {
        return DWScannerMap.getScannerInfo(id)
    }

    @Keep
    fun getScannerStatus(context: Context, delaySeconds: Int, callback: (DWAPI.ScannerStatus) -> Unit) {
        backgroundScope.launch {
            runCatching {
                delay(delaySeconds * 1000L)
                DWAPI.sendGetSelectedScannerStatusIntent(context)
            }.onSuccess {
                foregroundScope.launch {
                    callback(it)
                }
            }.onFailure {
                Log.e(TAG, "GET SCANNER STATUS FAIL. Exception: ${it.message}")
                foregroundScope.launch {
                    callback(DWAPI.ScannerStatus.UNKNOWN)
                }
            }
        }
    }

    @Keep
    fun getScannerList(context: Context, callback: (List<DWScannerMap.DWScannerInfo>) -> Unit) {
        backgroundScope.launch {
            runCatching {
                DWAPI.sendEnumerateScannersIntent(context)
            }.onSuccess {
                delay(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS)
                foregroundScope.launch {
                    callback(it)
                }
            }.onFailure {
                Log.e(TAG, "GET SCANNER LIST FAIL. Exception: ${it.message}")
                delay(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS)
                foregroundScope.launch {
                    callback(emptyList())
                }
            }
        }
    }

    private fun registerReceiverIfNeeded(context: Context) {
        if (dataReceiver != null) {
            Log.d(TAG, "DataReceiver already registered. skip")
            return
        }
        dataReceiver = DataReceiver()
        ContextCompat.registerReceiver(
            context.applicationContext,
            dataReceiver,
            IntentFilter().apply {
                addCategory(DWAPI.ResultCategoryNames.CATEGORY_DEFAULT.value)
                addAction(DWAPI.ResultActionNames.SCAN_RESULT_ACTION.value)
            },
            ContextCompat.RECEIVER_EXPORTED
        )
        if (notificationReveiver != null) {
            Log.d(TAG, "NotificationReceiver already registered. skip")
            return
        }
        notificationReveiver = NotificationReceiver()
        ContextCompat.registerReceiver(
            context.applicationContext,
            notificationReveiver,
            IntentFilter().apply {
                addCategory(DWAPI.ResultCategoryNames.CATEGORY_DEFAULT.value)
                addAction("com.symbol.datawedge.api.NOTIFICATION_ACTION")
            },
            ContextCompat.RECEIVER_EXPORTED
        )
    }

    @Keep
    class DataReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent == null) {
                Toast.makeText(context, "intent is null", Toast.LENGTH_LONG).show()
                return
            }
            if (intent.action == DWAPI.ResultActionNames.SCAN_RESULT_ACTION.value) {
                handleDWScanOutputAction(intent)
            } else {
                Toast.makeText(context, "Unknown action", Toast.LENGTH_LONG).show()
            }
        }

        fun handleDWScanOutputAction(intent: Intent) {
            if (intent.action != DWAPI.ResultActionNames.SCAN_RESULT_ACTION.value) {
                return
            }
            intent.extras?.let {
                val type = it.getString(DWAPI.ScanResult.TYPE) ?: ""
                val data = it.getString(DWAPI.ScanResult.DATA) ?: ""
                val timestamp: Long = it.getLong(DWAPI.ScanResult.TIME)
                val date = Date(timestamp)
                notifyDataListeners(type, data, date.toString())
            }
        }
    }

    @Keep
    class NotificationReceiver: BroadcastReceiver() {
        override fun onReceive(
            context: Context?,
            intent: Intent?
        ) {
            if (intent == null) {
                return
            }
            if (intent.action != "com.symbol.datawedge.api.NOTIFICATION_ACTION") {
                return
            }
            if (!intent.hasExtra("com.symbol.datawedge.api.NOTIFICATION")) {
                return
            }
            val bundle = intent.getBundleExtra("com.symbol.datawedge.api.NOTIFICATION") ?: return
            val typeString = bundle.getString("NOTIFICATION_TYPE") ?: return
            val type = DWAPI.NotificationType.valueOf(typeString)
            when (type) {
                DWAPI.NotificationType.CONFIGURATION_UPDATE -> {
                    notifyStatusListeners(type, "", "")
                }
                DWAPI.NotificationType.PROFILE_SWITCH -> {
                    val enabled = bundle.getString("PROFILE_ENABLED") ?: ""
                    val profile = bundle.getString("PROFILE_NAME") ?: ""
                    notifyStatusListeners(type, enabled, profile)
                }
                DWAPI.NotificationType.SCANNER_STATUS -> {
                    val status = bundle.getString("STATUS") ?: ""
                    val profile = bundle.getString("PROFILE_NAME") ?: ""
                    notifyStatusListeners(type, status, profile)
                }
                DWAPI.NotificationType.WORKFLOW_STATUS -> {
                    val status = bundle.getString("STATUS") ?: ""
                    val profile = bundle.getString("PROFILE_NAME") ?: ""
                    notifyStatusListeners(type, status, profile)
                }
            }
        }
    }
}