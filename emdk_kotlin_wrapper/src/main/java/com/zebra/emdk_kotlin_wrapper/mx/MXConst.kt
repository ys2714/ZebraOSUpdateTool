package com.zebra.emdk_kotlin_wrapper.mx

import androidx.annotation.Keep

@Keep
object MXConst {
    private val TAG = MXConst::class.java.simpleName

    // Content Provider Keys
    private const val IMEI = "imei"
    private const val BUILD_SERIAL = "build_serial"

    const val OSUPDATE_STATUS_URI = "content://oem_info/oem.zebra.osupdate/status"
    const val OSUPDATE_DETAIL_URI = "content://oem_info/oem.zebra.osupdate/detail"
    const val OSUPDATE_TIMESTAMP_URI = "content://oem_info/oem.zebra.osupdate/ts"

    const val PRODUCT_MODEL_URI = "content://oem_info/oem.zebra.secure/ro_product_model"
    const val SERIAL_URI = "content://oem_info/oem.zebra.secure/build_serial"
    const val IMEI_URI = "content://oem_info/wan/imei"
    const val TOUCH_MODE_URI = "content://oem_info/oem.zebra.software/persist.sys.touch_mode"
    const val VENDOR_TOUCH_MODE_URI = "content://oem_info/oem.zebra.software/persist.vendor.sys.touch_mode"

    // DW Access Control
    const val AUTHORITY_URI = "content://com.zebra.devicemanager.zdmcontentprovider"

    // Profile Parameters
    const val ResetAction = "ResetAction"
    const val RecoveryModeAccess = "RecoveryModeAccess"

    const val WakeUpIndividualAction = "WakeUpIndividualAction"
    const val WakeUpKeyIdentifierCustom = "WakeUpKeyIdentifierCustom"
    const val UsbClientModeDefault = "UsbClientModeDefault"
    const val TouchPanelSensitivity = "TouchPanelSensitivity"
    const val TouchActionAny = "TouchActionAny"
    const val ZipFile = "ZipFile"
    const val RemoteZipFile = "RemoteZipFile"
    const val OsupdateVerifyFile = "OsupdateVerifyFile"

    const val AuthorizationType = "AuthorizationType"
    const val ZebraAuthToken = "ZebraAuthToken"
    const val UserName = "UserName"
    const val Password = "Password"
    const val CustomAuthorizationHeader = "CustomAuthorizationHeader"
    const val SuppressReboot = "SuppressReboot"
    const val ServiceIdentifier = "ServiceIdentifier"
    const val CallerPackageName = "CallerPackageName"
    const val CallerSignature = "CallerSignature"
    const val APK = "APK"
    const val Package = "Package"
    const val Class = "Class"
    const val AutoTime = "AutoTime"
    const val NTPServer = "NTPServer"
    const val SyncInterval = "SyncInterval"
    const val TimeZone = "TimeZone"
    const val Date = "Date"
    const val Time = "Time"
    const val MilitaryTime = "MilitaryTime" // 0: do not change
    const val militaryTimeON = "1" // 1: turn ON
    const val militaryTimeOFF = "2" // 2: turn OFF
    const val DevAdminPkg = "DevAdminPkg"
    const val DevAdminClass = "DevAdminClass"
    const val ScreenLockType = "ScreenLockType"
    const val ScreenShotUsage = "ScreenShotUsage"
    const val PowerOffState = "PowerOffState"
    const val AirPlaneMode = "AirPlaneMode"
    const val SafeMode = "SafeMode"
    const val TouchPanel = "TouchPanel"
    const val TouchPanelMode = "TouchPanelMode"
    const val AutoScreenLockOption = "AutoScreenLockOption"
    const val AutoScreenLockState = "AutoScreenLockState"
    const val KeyIdentifier = "KeyIdentifier"
    const val BaseIntentAction = "BaseIntentAction"
    const val BaseIntentCategory = "BaseIntentCategory"
    const val BaseIntentStringExtraName = "BaseIntentStringExtraName"
    const val BaseIntentStringExtraValue = "BaseIntentStringExtraValue"
    const val TargetPathAndFileName = "TargetPathAndFileName"
    const val ConfigurationFile = "ConfigurationFile"
    const val SetCriticalLowThreshold = "SetCriticalLowThreshold"
    const val EXTRA_KEY_IDENTIFIER = "EXTRA_KEY_IDENTIFIER"

    // Ignored Value
    const val ignoredValue = "ignoredValue"

    /**
     * Defines the action to be taken for a permission request.
     * - **0: Do Nothing** - No change; prior settings are retained. (MX 10.0+, Android API 26+)
     * - **1: Allow** - Grants the permission to the app. (MX 10.0+, Android API 26+)
     * - **2: Deny** - Denies the permission to the app. (MX 10.0+, Android API 26+)
     * - **3: Allow User to choose** - Prompts the user to grant or deny the permission. This is the Android default. (MX 10.0+, Android API 26+)
     * - **4: Verify** - Verifies whether the permission is granted.
     */
    const val PermissionAccessAction = "PermissionAccessAction"
    const val PermissionAccessPackageName = "PermissionAccessPackageName"
    const val ApplicationClassName = "ApplicationClassName"
    const val PermissionAccessPermissionName = "PermissionAccessPermissionName"
    const val PermissionAccessSignature = "PermissionAccessSignature"
}
