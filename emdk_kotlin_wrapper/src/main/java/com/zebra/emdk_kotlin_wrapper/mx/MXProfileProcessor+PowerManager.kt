package com.zebra.emdk_kotlin_wrapper.mx

import android.content.Context
import com.zebra.emdk_kotlin_wrapper.mx.MXBase.AuthInfo.Type

/**
 * https://techdocs.zebra.com/emdk-for-android/14-0/mx/powermgr/
 *
 * Reset Action
 *
 * https://techdocs.zebra.com/mx/conditionmgr/
 *
 * It's important to note that PowerMgr actions such as Reboot and Reset, once executed,
 * prevent the execution of subsequent actions submitted by the Request XML document,
 * including the submission of a Result XML to the application sending the original Request.
 * Zebra therefore recommends using Condition Manager in conjunction with PowerMgr to ensure that
 * appropriate conditions exist on a device before attempting to perform "risky" operations such as OS updates,
 * the failure of which can render a device unusable, severely limited or otherwise in need of service.
 * */
@JvmOverloads
internal fun MXProfileProcessor.callPowerManagerFeature(
    context: Context,
    option: MXBase.PowerManagerOptions,
    zipFile: String? = null,
    authInfo: MXBase.AuthInfo? = null,
    suppressReboot: MXBase.PowerManagerSuppressRebootOptions = MXBase.PowerManagerSuppressRebootOptions.DO_NOTHING,
    delaySeconds: Long = 0,
    callback: (MXBase.ErrorInfo?) -> Unit) {
    when (option) {
        MXBase.PowerManagerOptions.SLEEP_MODE,
        MXBase.PowerManagerOptions.REBOOT,
        MXBase.PowerManagerOptions.POWER_OFF,
        MXBase.PowerManagerOptions.OS_CANCEL_ONGOING,
        MXBase.PowerManagerOptions.ENTERPRISE_RESET,
        MXBase.PowerManagerOptions.FACTORY_RESET,
        MXBase.PowerManagerOptions.FULL_DEVICE_WIPE -> {
            processProfileWithCallback(
                context,
                MXBase.ProfileXML.PowerManagerReset,
                MXBase.ProfileName.PowerManagerReset,
                mapOf(
                    MXConst.ResetAction to option.string,
                ),
                delaySeconds,
                callback
            )
        }
        MXBase.PowerManagerOptions.OS_UPDATE,
        MXBase.PowerManagerOptions.OS_UPGRADE,
        MXBase.PowerManagerOptions.OS_DOWNGRADE -> {
            val filePath = zipFile ?: ""
            processProfileWithCallback(
                context,
                MXBase.ProfileXML.PowerManagerResetOSUpdate,
                MXBase.ProfileName.PowerManagerResetOSUpdate,
                mapOf(
                    MXConst.ResetAction to option.string,
                    MXConst.ZipFile to filePath,
                    MXConst.SuppressReboot to suppressReboot.string
                ),
                delaySeconds,
                callback
            )
        }
        MXBase.PowerManagerOptions.OS_UPGRADE_STREAMING,
        MXBase.PowerManagerOptions.OS_DOWNGRADE_STREAMING -> {
            val fileUrl = zipFile ?: ""
            if (authInfo != null) {
                when(authInfo.authorizationType) {
                    Type.NoAuth -> {
                        processProfileWithCallback(
                            context,
                            MXBase.ProfileXML.PowerManagerResetOSStreaming,
                            MXBase.ProfileName.PowerManagerResetOSStreaming,
                            mapOf(
                                MXConst.ResetAction to option.string,
                                MXConst.RemoteZipFile to fileUrl,
                                MXConst.SuppressReboot to suppressReboot.string
                            ),
                            delaySeconds,
                            callback
                        )
                    }
                    Type.ZebraAuth -> {
                        processProfileWithCallback(
                            context,
                            MXBase.ProfileXML.PowerManagerResetOSStreaming,
                            MXBase.ProfileName.PowerManagerResetOSStreaming,
                            mapOf(
                                MXConst.ResetAction to option.string,
                                MXConst.RemoteZipFile to fileUrl,
                                MXConst.AuthorizationType to Type.ZebraAuth.ordinal.toString(),
                                MXConst.ZebraAuthToken to authInfo.zebraAuthToken
                            ),
                            delaySeconds,
                            callback
                        )
                    }
                    Type.BasicAuth -> {
                        processProfileWithCallback(
                            context,
                            MXBase.ProfileXML.PowerManagerResetOSStreaming,
                            MXBase.ProfileName.PowerManagerResetOSStreaming,
                            mapOf(
                                MXConst.ResetAction to option.string,
                                MXConst.RemoteZipFile to fileUrl,
                                MXConst.AuthorizationType to Type.BasicAuth.ordinal.toString(),
                                MXConst.UserName to authInfo.username,
                                MXConst.Password to authInfo.password
                            ),
                            delaySeconds,
                            callback
                        )
                    }
                    Type.CustomHeader -> {
                        processProfileWithCallback(
                            context,
                            MXBase.ProfileXML.PowerManagerResetOSStreaming,
                            MXBase.ProfileName.PowerManagerResetOSStreaming,
                            mapOf(
                                MXConst.ResetAction to option.string,
                                MXConst.RemoteZipFile to fileUrl,
                                MXConst.AuthorizationType to Type.CustomHeader.ordinal.toString(),
                                MXConst.CustomAuthorizationHeader to authInfo.customAuthorizationHeader
                            ),
                            delaySeconds,
                            callback
                        )
                    }
                }
            } else {
                processProfileWithCallback(
                    context,
                    MXBase.ProfileXML.PowerManagerResetOSStreaming,
                    MXBase.ProfileName.PowerManagerResetOSStreaming,
                    mapOf(
                        MXConst.ResetAction to option.string,
                        MXConst.RemoteZipFile to fileUrl,
                        MXConst.SuppressReboot to suppressReboot.string
                    ),
                    delaySeconds,
                    callback
                )
            }
        }
        MXBase.PowerManagerOptions.OS_UPDATE_VERIFY -> {
            val filePath = zipFile ?: ""
            processProfileWithCallback(
                context,
                MXBase.ProfileXML.PowerManagerResetOSVerify,
                MXBase.ProfileName.PowerManagerResetOSVerify,
                mapOf(
                    MXConst.ResetAction to option.string,
                    MXConst.OsupdateVerifyFile to filePath
                ),
                delaySeconds,
                callback
            )
        }
        MXBase.PowerManagerOptions.CREATE_PROFILE,
        MXBase.PowerManagerOptions.DO_NOTHING -> Unit
    }
}

/**
 * https://techdocs.zebra.com/emdk-for-android/14-0/mx/powermgr/
 *
 * Recovery Mode Access
 *
 * IMPORTANT PERSISTENCE NOTE: This setting persists on the device ONLY if the Enterprise Reset / Factory Reset is initiated by a barcode.
 * Resetting the device through the Android Settings panel or by any other means removes all settings created for this feature.
 * */
@JvmOverloads
internal fun MXProfileProcessor.callPowerManagerRecoveryModeControlFeature(
    context: Context,
    option: MXBase.PowerManagerRecoveryModeAccessOptions,
    delaySeconds: Long = 0,
    callback: (MXBase.ErrorInfo?) -> Unit) {
    processProfileWithCallback(
        context,
        MXBase.ProfileXML.PowerManagerRecoveryModeAccess,
        MXBase.ProfileName.PowerManagerRecoveryModeAccess,
        mapOf(
            MXConst.RecoveryModeAccess to option.string,
        ),
        delaySeconds,
        callback
    )
}

/***
 * https://techdocs.zebra.com/mx/powermgr/#wake-up-key-identifier
 *
 * Used to select an individual device button to allow or prevent the device from waking from power-saving modes when the button (if so equipped) is pressed. Use this parameter when selecting an Action using the Wake-up Individual Action parameter.
 *
 *
 *
 *
 * */
internal fun MXProfileProcessor.callPowerManagerSetWakeUpKey(
    context: Context,
    keyIdentifier: MXBase.KeyIdentifiers,
    action: MXBase.WakeUpIndividualAction,
    delaySeconds: Long = 0,
    callback: (MXBase.ErrorInfo?) -> Unit
) {
    processProfileWithCallback(
        context,
        MXBase.ProfileXML.PowerManagerSetWakeUpKey,
        MXBase.ProfileName.PowerManagerSetWakeUpKey,
        mapOf(
            MXConst.WakeUpIndividualAction to action.string,
            MXConst.WakeUpKeyIdentifierCustom to keyIdentifier.string,
        ),
        delaySeconds,
        callback
    )
}