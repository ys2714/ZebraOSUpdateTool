package com.zebra.osupdatetool

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zebra.emdk_kotlin_wrapper.emdk.EMDKHelper
import com.zebra.emdk_kotlin_wrapper.mx.MXBase
import com.zebra.emdk_kotlin_wrapper.mx.MXHelper
import com.zebra.osupdatetool.util.StorageUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * All OS update work is done by the MX Power Manager through the
 * emdk_kotlin_wrapper module:
 *
 * MXHelper.checkOSZipFile()  -> ResetAction 9  (verify package)
 * MXHelper.upgradeOS()       -> ResetAction 10 (upgrade)
 * MXHelper.downgradeOS()     -> ResetAction 11 (downgrade)
 * MXHelper.cancelOngoingUpdate() -> ResetAction 14
 * MXHelper.factoryResetDevice()  -> ResetAction 6  (factory reset)
 * MXHelper.fetchOSUpdateStatus() -> oem_info content provider
 */
class MainViewModel : ViewModel() {

    enum class UpdateMode(val title: String) {
        UPGRADE("Upgrade"),
        DOWNGRADE("Downgrade")
    }

    // EMDK / MX
    val emdkReady: MutableState<Boolean> = mutableStateOf(false)
    val emdkVersion: MutableState<String> = mutableStateOf("")
    val mxVersion: MutableState<String> = mutableStateOf("")

    // selected BSP package
    val bspFilePath: MutableState<String> = mutableStateOf("")
    val bspFileInfo: MutableState<String> = mutableStateOf("")

    // options
    val suppressReboot: MutableState<Boolean> = mutableStateOf(true)
    val verifyBeforeUpdate: MutableState<Boolean> = mutableStateOf(true)

    // OS update status (oem_info content provider)
    val osUpdateStatus: MutableState<String> = mutableStateOf(MXBase.OSUpdateStatus.UNKNOWN.string)
    val osUpdateDetail: MutableState<String> = mutableStateOf("")
    val osUpdateTimestamp: MutableState<String> = mutableStateOf("")

    // UI flags
    val busy: MutableState<Boolean> = mutableStateOf(false)
    val pendingConfirmMode: MutableState<UpdateMode?> = mutableStateOf(null)
    val shouldShowRebootDialog: MutableState<Boolean> = mutableStateOf(false)
    val shouldShowFactoryResetDialog: MutableState<Boolean> = mutableStateOf(false)
    val logs = mutableStateListOf<String>()

    private var statusPollingJob: Job? = null

    fun prepareEMDK(context: Context) {
        if (emdkReady.value) {
            return
        }
        log("connecting to EMDK ...")
        EMDKHelper.shared.prepare(context) { success ->
            emdkReady.value = success
            if (success) {
                emdkVersion.value = EMDKHelper.shared.getEMDKVersion()
                mxVersion.value = EMDKHelper.shared.getMXVersion()
                log("EMDK ready (EMDK ${emdkVersion.value} / MX ${mxVersion.value})")
                startStatusPolling(context)
            } else {
                log("EMDK connection failed, is this a Zebra device with EMDK service ?")
            }
        }
    }

    fun teardownEMDK() {
        stopStatusPolling()
        EMDKHelper.shared.teardown()
        emdkReady.value = false
    }

    // ---------------------------------------------------------------- file selection

    fun selectBSPFile(path: String) {
        bspFilePath.value = path
        refreshBSPFileInfo()
        if (path.isNotEmpty()) {
            log("BSP package selected: $path")
        }
    }

    fun refreshBSPFileInfo() {
        val path = bspFilePath.value
        if (path.isEmpty()) {
            bspFileInfo.value = ""
            return
        }
        val file = File(path)
        bspFileInfo.value = when {
            !file.exists() -> "file not found (check the path or grant all files access)"
            !file.isFile -> "not a file"
            !StorageUtils.isZipFile(file) -> "warning: not a .zip file (${StorageUtils.humanReadableSize(file.length())})"
            else -> "${StorageUtils.humanReadableSize(file.length())} - ready"
        }
    }

    fun isBSPFileUsable(): Boolean {
        val path = bspFilePath.value
        return path.isNotEmpty() && File(path).isFile
    }

    // ---------------------------------------------------------------- MX operations

    /** Asks for confirmation first, the real work is done by [startUpdate]. */
    fun requestUpdate(mode: UpdateMode) {
        pendingConfirmMode.value = mode
    }

    fun cancelConfirm() {
        pendingConfirmMode.value = null
    }

    fun startUpdate(context: Context, mode: UpdateMode) {
        pendingConfirmMode.value = null
        val path = bspFilePath.value
        if (!isBSPFileUsable()) {
            log("no readable BSP package selected")
            return
        }
        if (!emdkReady.value) {
            log("EMDK is not ready yet")
            return
        }
        busy.value = true
        if (!verifyBeforeUpdate.value) {
            executeUpdate(context, mode, path)
            return
        }
        log("verifying package ...")
        MXHelper.checkOSZipFile(context, path) { success ->
            if (success) {
                log("package verified")
                executeUpdate(context, mode, path)
            } else {
                busy.value = false
                log("package verification failed, ${mode.title} aborted")
            }
        }
    }

    private fun executeUpdate(context: Context, mode: UpdateMode, path: String) {
        when (mode) {
            UpdateMode.UPGRADE -> {
                log("starting upgrade (SuppressReboot=${suppressReboot.value}) ...")
                MXHelper.upgradeOS(context, path, suppressReboot.value)
            }
            UpdateMode.DOWNGRADE -> {
                // SuppressReboot is only honored by MX on upgrade, not on downgrade
                log("starting downgrade ...")
                MXHelper.downgradeOS(context, path)
            }
        }
        busy.value = false
        startStatusPolling(context)
    }

    fun verifyOnly(context: Context) {
        if (!isBSPFileUsable()) {
            log("no readable BSP package selected")
            return
        }
        if (!emdkReady.value) {
            log("EMDK is not ready yet")
            return
        }
        busy.value = true
        log("verifying package ...")
        MXHelper.checkOSZipFile(context, bspFilePath.value) { success ->
            busy.value = false
            log(if (success) "package verified" else "package verification failed")
        }
    }

    fun cancelOngoingUpdate(context: Context) {
        log("cancelling ongoing update ...")
        MXHelper.cancelOngoingUpdate(context)
    }

    fun rebootDevice(context: Context) {
        shouldShowRebootDialog.value = false
        log("rebooting device ...")
        MXHelper.setDeviceToReboot(context)
    }

    fun dismissRebootDialog() {
        shouldShowRebootDialog.value = false
    }

    // ---------------------------------------------------------------- factory reset

    /** Asks for confirmation first, the real work is done by [factoryResetDevice]. */
    fun requestFactoryReset() {
        shouldShowFactoryResetDialog.value = true
    }

    fun cancelFactoryReset() {
        shouldShowFactoryResetDialog.value = false
    }

    /**
     * Wipes the device back to its out-of-the-box state, the device reboots on its
     * own and MX never sends a result back, so there is nothing to wait for here.
     */
    fun factoryResetDevice(context: Context) {
        shouldShowFactoryResetDialog.value = false
        if (!emdkReady.value) {
            log("EMDK is not ready yet")
            return
        }
        log("factory reset requested, the device will wipe and reboot ...")
        stopStatusPolling()
        MXHelper.factoryResetDevice(context)
    }

    // ---------------------------------------------------------------- status polling

    fun startStatusPolling(context: Context) {
        if (statusPollingJob?.isActive == true) {
            return
        }
        statusPollingJob = viewModelScope.launch {
            while (isActive) {
                fetchStatusOnce(context)
                delay(POLLING_INTERVAL_MS)
            }
        }
    }

    fun stopStatusPolling() {
        statusPollingJob?.cancel()
        statusPollingJob = null
    }

    fun fetchStatusOnce(context: Context) {
        MXHelper.fetchOSUpdateStatus(context) { status, detail, timestamp ->
            val changed = status.string != osUpdateStatus.value
            osUpdateStatus.value = status.string
            osUpdateDetail.value = detail
            osUpdateTimestamp.value = timestamp
            if (changed) {
                log("OS update status: ${status.string}${if (detail.isEmpty()) "" else " ($detail)"}")
            }
            if (status == MXBase.OSUpdateStatus.WAITING_FOR_REBOOT && !shouldShowRebootDialog.value) {
                shouldShowRebootDialog.value = true
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopStatusPolling()
    }

    // ---------------------------------------------------------------- logging

    fun log(message: String) {
        val stamp = SimpleDateFormat("HH:mm:ss", Locale.US).format(Date())
        logs.add(0, "$stamp  $message")
        if (logs.size > MAX_LOG_LINES) {
            logs.removeAt(logs.lastIndex)
        }
    }

    companion object {
        private const val POLLING_INTERVAL_MS = 5_000L
        private const val MAX_LOG_LINES = 100
    }
}
