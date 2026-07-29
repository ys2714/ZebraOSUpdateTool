package com.zebra.emdk_kotlin_wrapper.mx

import android.content.Context
import android.os.Build
import androidx.annotation.Keep
import androidx.annotation.RequiresApi
import com.zebra.emdk_kotlin_wrapper.utils.DeviceInfoUtils
import com.zebra.emdk_kotlin_wrapper.zdm.ZDMConst
import disableApp

@Keep
object MXHelper {

    @Keep
    fun whiteListApproveApp(context: Context, delaySeconds: Long = 0, callback: (Boolean) -> Unit) {
        MXProfileProcessor.getCallServicePermission(
            context,
            ZDMConst.DelegationScope.SCOPE_DW_CONFIG_API.value,
            delaySeconds,
            { error ->
                callback(error == null)
            }
        )
    }

    @Keep
    fun disableApp(context: Context, packageName: String, delaySeconds: Long = 0, callback: (Boolean) -> Unit) {
        MXProfileProcessor.disableApp(
            context,
            packageName,
            delaySeconds
        ) { error ->
            callback(error == null)
        }
    }

    @Keep
    fun setDeviceToSleep(context: Context, delaySeconds: Long = 0) {
        MXProfileProcessor.callPowerManagerFeature(
            context,
            MXBase.PowerManagerOptions.SLEEP_MODE,
            delaySeconds = delaySeconds
        ) {}
    }

    @Keep
    fun setDeviceToReboot(context: Context, delaySeconds: Long = 0) {
        MXProfileProcessor.callPowerManagerFeature(
            context,
            MXBase.PowerManagerOptions.REBOOT,
            delaySeconds = delaySeconds
        ) {}
    }

    @Keep
    fun upgradeOS(context: Context, zipFilePath: String, suppressReboot: Boolean, delaySeconds: Long = 0) {
        val suppressReboot = if (suppressReboot) MXBase.PowerManagerSuppressRebootOptions.TRUE
            else MXBase.PowerManagerSuppressRebootOptions.FALSE
        MXProfileProcessor.callPowerManagerFeature(
            context,
            MXBase.PowerManagerOptions.OS_UPGRADE,
            zipFilePath,
            null,
            suppressReboot,
            delaySeconds
        ) {}
    }

    @Keep
    fun downgradeOS(context: Context, zipFilePath: String, delaySeconds: Long = 0) {
        MXProfileProcessor.callPowerManagerFeature(
            context,
            MXBase.PowerManagerOptions.OS_DOWNGRADE,
            zipFilePath,
            null,
            // SuppressReboot parameter ONLY work on upgrade NOT downgrade.
            MXBase.PowerManagerSuppressRebootOptions.DO_NOTHING,
            delaySeconds
        ) {}
    }

    @Keep
    fun streamUpgradeOS(context: Context, zipFileUrl: String, authInfo: MXBase.AuthInfo, suppressReboot: Boolean, delaySeconds: Long = 0) {
        val suppressReboot = if (suppressReboot) MXBase.PowerManagerSuppressRebootOptions.TRUE
        else MXBase.PowerManagerSuppressRebootOptions.FALSE
        MXProfileProcessor.callPowerManagerFeature(
            context,
            MXBase.PowerManagerOptions.OS_UPGRADE_STREAMING,
            zipFileUrl,
            authInfo,
            suppressReboot,
            delaySeconds
        ) {}
    }

    @Keep
    fun streamDowngradeOS(context: Context, zipFileUrl: String, authInfo: MXBase.AuthInfo, delaySeconds: Long = 0) {
        MXProfileProcessor.callPowerManagerFeature(
            context,
            MXBase.PowerManagerOptions.OS_DOWNGRADE_STREAMING,
            zipFileUrl,
            authInfo,
            // SuppressReboot parameter ONLY work on upgrade NOT downgrade.
            MXBase.PowerManagerSuppressRebootOptions.DO_NOTHING,
            delaySeconds
        ) {}
    }

    @Keep
    fun cancelOngoingUpdate(context: Context) {
        MXProfileProcessor.callPowerManagerFeature(
            context,
            MXBase.PowerManagerOptions.OS_CANCEL_ONGOING,
            "",
            null,
            MXBase.PowerManagerSuppressRebootOptions.DO_NOTHING,
            0
        ) {}
    }

    @Keep
    fun checkOSZipFile(context: Context, manifestFilePath: String, delaySeconds: Long = 0, callback: (Boolean) -> Unit) {
        MXProfileProcessor.callPowerManagerFeature(
            context,
            MXBase.PowerManagerOptions.OS_UPDATE_VERIFY,
            manifestFilePath,
            null,
            MXBase.PowerManagerSuppressRebootOptions.DO_NOTHING,
            delaySeconds
        ) { error ->
            callback(error == null)
        }
    }

    @Keep
    fun setSystemClock(context: Context, timeZone: String, date: String, time: String, delaySeconds: Long = 0, callback: (Boolean) -> Unit) {
        MXProfileProcessor.callClockSet(
            context,
            true,
            timeZone,
            date,
            time,
            delaySeconds,
            { error ->
                callback(error == null)
            }
        )
    }

    @Keep
    fun resetSystemClockToNTP(context: Context, ntpServer: String, syncInterval: String, delaySeconds: Long = 0, callback: (Boolean) -> Unit) {
        MXProfileProcessor.callClockResetAuto(
            context,
            true,
            ntpServer,
            syncInterval,
            delaySeconds,
            { error ->
                callback(error == null)
            }
        )
    }

    @Keep
    fun setScreenLockType(context: Context, lockType: MXBase.ScreenLockType, delaySeconds: Long = 0, callback: (Boolean) -> Unit) {
        MXProfileProcessor.setScreenLockType(
            context,
            lockType,
            delaySeconds,
            { error ->
                callback(error == null)
            }
        )
    }

    @Keep
    fun setScreenShotUsage(context: Context, usage: MXBase.ScreenShotUsage, delaySeconds: Long = 0, callback: (Boolean) -> Unit) {
        MXProfileProcessor.setScreenShotUsage(
            context,
            usage,
            delaySeconds,
            { error ->
                callback(error == null)
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @Keep
    fun setPowerKeyMenuEnablePowerOffButton(context: Context, enable: Boolean, delaySeconds: Long = 0, callback: (Boolean) -> Unit) {
        MXProfileProcessor.powerKeyMenuEnablePowerOffButton(context, enable, delaySeconds) {
            callback(it == null)
        }
    }

    @Keep
    fun setWakeUpKey(context: Context, keyIdentifier: MXBase.KeyIdentifiers, action: MXBase.WakeUpIndividualAction, delaySeconds: Long = 0, callback: (Boolean) -> Unit) {
        MXProfileProcessor.callPowerManagerSetWakeUpKey(context, keyIdentifier, action, delaySeconds) {
            val message = it?.message
            callback(it == null)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Keep
    fun setRecoveryModeAccess(context: Context, enable: Boolean, delaySeconds: Long = 0, callback: (Boolean) -> Unit) {
        val option = if (enable) MXBase.PowerManagerRecoveryModeAccessOptions.FULL
            else MXBase.PowerManagerRecoveryModeAccessOptions.PARTIAL
        MXProfileProcessor.callPowerManagerRecoveryModeControlFeature(context, option, delaySeconds) {
            callback(it == null)
        }
    }

    @Keep
    fun setUSBClientModeChargingOnly(context: Context, delaySeconds: Long = 0, callback: (Boolean) -> Unit) {
        MXProfileProcessor.setUSBClientModeDefault(context, MXBase.UsbClientModeDefaultOptions.CHARGING_ONLY, delaySeconds) {
            callback(it == null)
        }
    }

    @Keep
    fun setUSBClientModeFileTransfer(context: Context, delaySeconds: Long = 0, callback: (Boolean) -> Unit) {
        MXProfileProcessor.setUSBClientModeDefault(context, MXBase.UsbClientModeDefaultOptions.FILE_TRANSFER, delaySeconds) {
            callback(it == null)
        }
    }

    @Keep
    fun setUSBClientModeTethering(context: Context, delaySeconds: Long = 0, callback: (Boolean) -> Unit) {
        MXProfileProcessor.setUSBClientModeDefault(context, MXBase.UsbClientModeDefaultOptions.USB_TETHERING, delaySeconds) {
            callback(it == null)
        }
    }

    @Keep
    fun setTouchPanelSensitivity(context: Context, option: MXBase.TouchPanelSensitivityOptions, delaySeconds: Long = 0, callback: (Boolean) -> Unit) {
        MXProfileProcessor.configTouchPanelSensitivity(context, option, delaySeconds) {
            callback(it == null)
        }
    }

    @Keep
    fun powerKeyTriggerAutoScreenLock(context: Context, enable: Boolean, delaySeconds: Long = 0, callback: (Boolean) -> Unit) {
        MXProfileProcessor.powerKeyTriggerAutoScreenLock(context, enable, delaySeconds) {
            callback(it == null)
        }
    }

    @Keep
    fun powerKeyAutoScreenLockSettingsOptionEnable(context: Context, enable: Boolean, delaySeconds: Long = 0, callback: (Boolean) -> Unit) {
        MXProfileProcessor.powerKeyAutoScreenLockSettingsOptionEnable(context, enable, delaySeconds) {
            callback(it == null)
        }
    }

    @Keep
    fun fetchProductModel(context: Context, delaySeconds: Long = 0, callback: (String) -> Unit) {
        MXProfileProcessor.fetchProductModelInBackground(
            context,
            delaySeconds,
            callback
        )
    }

    /**
     *
     * @param context
     * @param isDevDevice indicate get PPID for development device or production device
     * @param callback: return empty string "" if failed
     * */
    @Keep
    fun fetchSerialNumber(context: Context, delaySeconds: Long = 0, callback: (String) -> Unit) {
        MXProfileProcessor.fetchSerialNumberInBackground(
            context,
            delaySeconds,
            callback
        )
    }

    /**
     *
     * @param context
     * @param isDevDevice indicate get PPID for development device or production device
     * @param callback: return empty string "" if failed
     * */
    @Keep
    fun fetchPPID(context: Context, isDevDevice: Boolean, delaySeconds: Long = 0, callback: (String) -> Unit) {
        MXProfileProcessor.fetchSerialNumberInBackground(context, delaySeconds) { result ->
            if (result.isEmpty()) {
                callback("")
                return@fetchSerialNumberInBackground
            }
            val suffix = result.takeLast(5)
            val prefix = if (isDevDevice) "619" else "610"
            val yppid = "$prefix$suffix"
            callback(yppid)
        }
    }

    @Keep
    fun fetchIMEI(context: Context, delaySeconds: Long = 0, callback: (String) -> Unit) {
        if (!DeviceInfoUtils.hasTelephonyFeature(context)) {
            callback("")
        }
        MXProfileProcessor.fetchIMEIInBackground(context, delaySeconds) { result ->
            callback(result)
        }
    }

    @Keep
    fun fetchTouchMode(context: Context, delaySeconds: Long = 0, callback: (String) -> Unit) {
        MXProfileProcessor.fetchTouchModeInBackground(context, delaySeconds) { result ->
            callback(result)
        }
    }

    @Keep
    fun fetchVendorTouchMode(context: Context, delaySeconds: Long = 0, callback: (String) -> Unit) {
        MXProfileProcessor.fetchVendorTouchModeInBackground(context, delaySeconds) { result ->
            callback(result)
        }
    }

    @Keep
    fun fetchOSUpdateStatus(context: Context, callback: (MXBase.OSUpdateStatus, String, String) -> Unit) {
        MXProfileProcessor.fetchOSUpdateStatusInBackground(context) { status, detail, timestamp ->
            callback(status, detail, timestamp)
        }
    }

    @Keep
    fun setKeyMappingToSendIntent(context: Context, keyIdentifier: MXBase.KeyIdentifiers, intentAction: String, intentCategory: String, delaySeconds: Long = 0, callback: (Boolean) -> Unit) {
        MXProfileProcessor.remappingKeyToSendIntent(
            context,
            keyIdentifier,
            intentAction,
            intentCategory,
            delaySeconds
        ) { error ->
            callback(error == null)
        }
    }

    @Keep
    fun setKeyMappingToDefault(context: Context, delaySeconds: Long = 0, callback: (Boolean) -> Unit) {
        MXProfileProcessor.remappingAllKeyToDefault(context, delaySeconds) { error ->
            callback(error == null)
        }
    }

    @Keep
    fun copyAndImportFreeFormOCRProfile(context: Context, delaySeconds: Long = 0, callback: (Boolean) -> Unit) {
        val target = "/data/tmp/public/dwprofile_ocr_workflow.db"
        val name = "dwprofile_ocr_workflow.db"
        MXProfileProcessor.copyEmbeddedFreeFormOCRProfile(context, target, delaySeconds) { errorInfo1 ->
            if (errorInfo1 != null) {
                callback(false)
            } else {
                MXProfileProcessor.importProfile(context, target, delaySeconds) { errorInfo2 ->
                    callback(errorInfo2 == null)
                }
            }
        }
    }

    @Keep
    fun setBatteryCriticalLowThreshold(context: Context, threshold: Int, callback: (Boolean) -> Unit) {
        MXProfileProcessor.setBatteryCriticalLowThreshold(context, threshold) { error ->
            callback(error == null)
        }
    }
}