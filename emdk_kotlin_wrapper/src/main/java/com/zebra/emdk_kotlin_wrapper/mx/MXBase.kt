package com.zebra.emdk_kotlin_wrapper.mx

import android.text.TextUtils
import androidx.annotation.Keep


@Keep
class MXBase {

    @Keep
    companion object {
        private val TAG = MXBase::class.java.simpleName
    }

    @Keep
    interface FetchOEMInfoCallback {
        fun onSuccess(result: String)
        fun onError()
    }

    @Keep
    interface EventListener {
        fun onEMDKSessionOpened()
        fun onEMDKSessionClosed()
        fun onEMDKError(errorInfo: ErrorInfo)
    }

    @Keep
    data class ErrorInfo(
        // Contains the parm-error name (sub-feature that has error)
        var errorName: String = "",
        // Contains the characteristic-error type (Root feature that has error)
        var errorType: String = "",
        // contains the error description for parm or characteristic error.
        var errorDescription: String = ""
    ) : Throwable() {
        fun buildFailureMessage(): String {
            return when {
                !TextUtils.isEmpty(errorName) && !TextUtils.isEmpty(errorType) -> {
                    "$errorName :\n$errorType :\n$errorDescription"
                }
                !TextUtils.isEmpty(errorName) -> {
                    "$errorName :\n$errorDescription"
                }
                else -> {
                    "$errorType :\n$errorDescription"
                }
            }
        }
    }

    /**
     * https://techdocs.zebra.com/mx/powermgr/#authorization-type
     *
     * AuthorizationType:
     *
     * 0.No Authorization	Selects no authorization for Upgrade/Downgrade Streaming server connection.
     * 1.Zebra Authentication Token	Selects Zebra Authentication Token for Upgrade/Downgrade Streaming server connection.
     * 2.Basic Authentication	Selects Basic Authentication for Upgrade/Downgrade Streaming server connection.
     * 3.Custom Authorization Header	Selects Custom Authentication Header for Upgrade/Downgrade Streaming server connection. Requires a value to be entered in the CustomAuthorizationHeader parameter.
     *
     * TokenKey1:
     * 1.ZebraAuthToken
     * 2.UserName
     * 3.CustomAuthorizationHeader
     *
     * TokenKey2:
     * 2.Password
     *
     * */
    @Keep
    data class AuthInfo(
        val authorizationType: AuthInfo.Type,
        val zebraAuthToken: String,
        val username: String,
        val password: String,
        val customAuthorizationHeader: String
    ) {
        enum class Type(value: Int) {
            NoAuth(0),
            ZebraAuth(1),
            BasicAuth(2),
            CustomHeader(3);
        }

        @Keep
        companion object {

            @Keep
            fun createWithNoAuthType(): AuthInfo {
                return AuthInfo(
                    authorizationType = Type.NoAuth,
                    zebraAuthToken = MXConst.ignoredValue,
                    username = MXConst.ignoredValue,
                    password = MXConst.ignoredValue,
                    customAuthorizationHeader = MXConst.ignoredValue
                )
            }

            @Keep
            fun createWithZebraAuth(tokenValue: String): AuthInfo {
                return AuthInfo(
                    authorizationType = Type.ZebraAuth,
                    zebraAuthToken = tokenValue,
                    username = MXConst.ignoredValue,
                    password = MXConst.ignoredValue,
                    customAuthorizationHeader = MXConst.ignoredValue
                )
            }

            @Keep
            fun createWithBasicAuth(username: String, password: String): AuthInfo {
                return AuthInfo(
                    authorizationType = Type.BasicAuth,
                    zebraAuthToken = MXConst.ignoredValue,
                    username = username,
                    password = password,
                    customAuthorizationHeader = MXConst.ignoredValue
                )
            }

            @Keep
            fun createWithCustomHeader(token: String): AuthInfo {
                return AuthInfo(
                    authorizationType = Type.CustomHeader,
                    zebraAuthToken = MXConst.ignoredValue,
                    username = MXConst.ignoredValue,
                    password = MXConst.ignoredValue,
                    customAuthorizationHeader = "Bearer " + token
                )
            }

            @Keep
            fun createWithAuthType(type: Type, param1: String, param2: String = ""): AuthInfo {
                when(type) {
                    Type.NoAuth -> {
                        return createWithNoAuthType()
                    }
                    Type.ZebraAuth -> {
                        return createWithZebraAuth(param1)
                    }
                    Type.BasicAuth -> {
                        return createWithBasicAuth(param1, param2)
                    }
                    Type.CustomHeader -> {
                        return createWithCustomHeader(param1)
                    }
                }
            }
        }
    }

    @Keep
    enum class ProfileXML(val value: String) {
        None("None"),
        AccessManagerAllowPermission("profile_access_manager_allow_permission.xml"),
        AccessManagerAllowCallService("profile_access_manager_allow_call_service.xml"),
        AppManagerInstallAndStart("profile_app_manager_install_and_start.xml"),
        AppManagerDisable("profile_app_manager_disable.xml"),
        BatteryManagerSetCriticalLowThreshold("profile_battery_manager_set_critical_low_threshold.xml"),
        PowerManagerReset("profile_power_manager_reset.xml"),
        PowerManagerResetOSUpdate("profile_power_manager_reset_os_update.xml"),
        PowerManagerResetOSVerify("profile_power_manager_reset_os_verify.xml"),
        PowerManagerResetOSStreaming("profile_power_manager_reset_os_streaming.xml"),
        PowerManagerRecoveryModeAccess("profile_power_manager_recovery_mode_access.xml"),
        PowerManagerSetWakeUpKey("profile_power_manager_set_wakeup_key.xml"),
        ClockSet("profile_clock_set.xml"),
        ClockResetAuto("profile_clock_reset_auto.xml"),
        DevAdminManagerDisableLockScreen("profile_dev_admin_manager_disable_lock_screen.xml"),
        DisplayManagerDisableScreenShot("profile_display_manager_disable_screenshot.xml"),
        PowerKeyManagerSetPowerOffState("profile_powerkey_manager_set_poweroff_state.xml"),
        KeymappingManagerSetKeySendIntent("profile_keymapping_manager_set_key_send_intent.xml"),
        KeymappingManagerSetAllToDefault("profile_keymapping_manager_set_all_to_default.xml"),
        DataWedgeManagerImportProfile("profile_datawedge_manager_import_profile.xml"),
        UsbClientModeDefault("profile_usb_manager_client_mode_default.xml"),
        TouchPanelSensitivity("profile_touch_panel_sensitivity.xml"),
        FileManagerCopyEmbeddedFreeFormOCR("profile_file_manager_copy_embedded_free_form_ocr.xml");

        @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.WARNING)
        override fun toString(): String {
            // throw RuntimeException("Not Implemented")
            return value
        }

        val string: String
            get() = value
    }

    @Keep
    enum class ProfileName(private val value: String) {
        AccessManagerAllowPermission("AccessManagerAllowPermission"),
        AccessManagerAllowCallService("AccessManagerAllowCallService"),
        AppManagerInstallAndStart("AppManagerInstallAndStart"),
        AppManagerDisable("AppManagerDisable"),
        BatteryManagerSetCriticalLowThreshold("BatteryManagerSetCriticalLowThreshold"),
        PowerManagerReset("PowerManagerReset"),
        PowerManagerResetOSUpdate("PowerManagerResetOSUpdate"),
        PowerManagerResetOSVerify("PowerManagerResetOSVerify"),
        PowerManagerResetOSStreaming("PowerManagerResetOSStreaming"),
        PowerManagerRecoveryModeAccess("PowerManagerRecoveryModeAccess"),
        PowerManagerSetWakeUpKey("PowerManagerSetWakeUpKey"),
        ClockSet("ClockSet"),
        ClockResetAuto("ClockResetAuto"),
        DevAdminManagerDisableLockScreen("DevAdminManagerDisableLockScreen"),
        DisplayManagerDisableScreenShot("DisplayManagerDisableScreenShot"),
        PowerKeyManagerSetPowerOffState("PowerKeyManagerSetPowerOffState"),
        KeymappingManagerSetKeySendIntent("KeymappingManagerSetKeySendIntent"),
        KeymappingManagerSetAllToDefault("KeymappingManagerSetAllToDefault"),
        DataWedgeManagerImportProfile("DataWedgeManagerImportProfile"),
        UsbClientModeDefault("UsbClientModeDefault"),
        TouchPanelSensitivity("TouchPanelSensitivity"),
        FileManagerCopyEmbeddedFreeFormOCR("FileManagerCopyEmbeddedFreeFormOCR");

        @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.WARNING)
        override fun toString(): String {
            // throw RuntimeException("Not Implemented")
            return value
        }

        val string: String
            get() = value
    }

    /**
     * Specifies the Power Manager action to be performed. The values correspond
     * to options available in the MX Power Manager profile.
     *
     * - `CREATE_PROFILE` (-1): Creates the profile without taking action.
     * - `DO_NOTHING` (0): No power management action is taken.
     * - `SLEEP_MODE` (1): Puts the device into sleep mode.
     * - `REBOOT` (4): Reboots the device.
     * - `ENTERPRISE_RESET` (5): Performs an enterprise reset.
     * - `FACTORY_RESET` (6): Performs a factory reset.
     * - `FULL_DEVICE_WIPE` (7): Performs a full device wipe.
     * - `OS_UPDATE` (8): Initiates an OS update.
     * - `OS_UPDATE_VERIFY` (9): Initiates an OS update.
     * - `OS_UPGRADE` (10): Initiates an OS update.
     * - `OS_DOWNGRADE` (11): Initiates an OS update.
     * - `OS_UPGRADE_STREAMING` (12): Initiates an OS update.
     * - `OS_DOWNGRADE_STREAMING` (13): Initiates an OS update.
     * - `OS_CANCEL_ONGOING` (14): Initiates an OS update.
     * - `POWER_OFF` (15): Initiates an OS update.
     */
    @Keep
    enum class PowerManagerOptions(val value: Int) {
        CREATE_PROFILE(-1),
        DO_NOTHING(0),
        SLEEP_MODE(1),
        REBOOT(4),
        ENTERPRISE_RESET(5),
        FACTORY_RESET(6),
        FULL_DEVICE_WIPE(7),
        OS_UPDATE(8),
        OS_UPDATE_VERIFY(9),
        OS_UPGRADE(10),
        OS_DOWNGRADE(11),
        OS_UPGRADE_STREAMING(12),
        OS_DOWNGRADE_STREAMING(13),
        OS_CANCEL_ONGOING(14),
        POWER_OFF(15);

        @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
        override fun toString(): String {
            throw RuntimeException("Not Implemented")
        }

        val string: String
            get() = value.toString()
    }

    @Keep
    enum class PowerManagerSuppressRebootOptions(val value: Int) {
        DO_NOTHING(0),
        TRUE(1),
        FALSE(2);

        @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
        override fun toString(): String {
            throw RuntimeException("Not Implemented")
        }

        val string: String
            get() = value.toString()
    }

    /**
     * Used to select whether to display Upgrade and Downgrade actions for selection while the device is in Recovery Mode. Selection of such options could lead to unwanted changes to device behavior, removal of theft deterrent measures and/or other unwanted activities. Selecting "Partial" (option 2) displays only "Reboot system now," "View recovery logs" and "Power off" functions for selection.
     *
     * Do not change (0): This value (or the absence of this parm from the XML) will cause no action to be performed on the device; any previously selected setting will be retained.
     * Full (default): Displays all Recovery Mode actions for selection, including Upgrade and Downgrade.
     * Partial (blocks sensitive operations): Displays only "Reboot system now," "View recovery logs" and "Power off" functions for selection.
     * */
    @Keep
    enum class PowerManagerRecoveryModeAccessOptions(val value: Int) {
        DO_NOTHING(0),
        FULL(1),
        PARTIAL(2);

        @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
        override fun toString(): String {
            throw RuntimeException("Not Implemented")
        }

        val string: String
            get() = value.toString()
    }

    enum class PersistTouchMode(val value: String) {
        STYLUS_AND_FINGER("stylus_and_finger"),
        GLOVE_AND_FINGER("glove_and_finger"),
        FINGER_ONLY("finger"),
        STYLUS_GLOVE_AND_FINGER("stylus_and_glove_and_finger"),

        ALL_PURPOSE("all_purpose"), // for TC201

        REDUCED_SENSITIVITY_RAIN("reduced_sensitivity_rain"); // for TC201

        fun convert(): TouchPanelSensitivityOptions {
            when (this) {
                PersistTouchMode.STYLUS_AND_FINGER -> return TouchPanelSensitivityOptions.STYLUS_AND_FINGER
                PersistTouchMode.GLOVE_AND_FINGER -> return TouchPanelSensitivityOptions.GLOVE_AND_FINGER
                PersistTouchMode.FINGER_ONLY -> return TouchPanelSensitivityOptions.FINGER_ONLY
                PersistTouchMode.STYLUS_GLOVE_AND_FINGER -> return TouchPanelSensitivityOptions.STYLUS_GLOVE_AND_FINGER
                PersistTouchMode.ALL_PURPOSE -> return TouchPanelSensitivityOptions.ALL_PURPOSE
                PersistTouchMode.REDUCED_SENSITIVITY_RAIN -> return TouchPanelSensitivityOptions.REDUCED_SENSITIVITY_RAIN
            }
            throw java.lang.RuntimeException("PersistTouchMode covert failed: " + this.value)
        }

        companion object {
            fun fromValue(value: String?): PersistTouchMode {
                if (PersistTouchMode.STYLUS_AND_FINGER.value == value) {
                    return PersistTouchMode.STYLUS_AND_FINGER
                } else if (PersistTouchMode.GLOVE_AND_FINGER.value == value) {
                    return PersistTouchMode.GLOVE_AND_FINGER
                } else if (PersistTouchMode.FINGER_ONLY.value == value) {
                    return PersistTouchMode.FINGER_ONLY
                } else if (PersistTouchMode.STYLUS_GLOVE_AND_FINGER.value == value) {
                    return PersistTouchMode.STYLUS_GLOVE_AND_FINGER
                } else if (PersistTouchMode.ALL_PURPOSE.value == value) {
                    return PersistTouchMode.ALL_PURPOSE
                } else if (PersistTouchMode.REDUCED_SENSITIVITY_RAIN.value == value) {
                    return PersistTouchMode.REDUCED_SENSITIVITY_RAIN
                } else {
                    throw java.lang.RuntimeException("PersistTouchMode invalid value: " + value)
                }
            }
        }
    }

    /**
     * https://techdocs.zebra.com/mx/touchmgr/
     *
     * 0	Do not change
     * This value (or the absence of this parm from the XML) causes no change to the Touch Mode;
     * any previously selected setting is retained.
     *
     * 1	Stylus and Finger
     * Adjusts touch-screen sensitivity for input with a stylus or bare finger.
     *
     * 2	Glove and Finger
     * Adjusts touch-screen sensitivity for input with a bare or gloved finger.
     *
     * 3	Finger
     * Adjusts touch-screen sensitivity for input with a bare finger only.
     *
     * 4	Stylus and Glove and Finger
     * Adjusts touch-screen sensitivity for input with a stylus, or bare or gloved finger.
     * */
    @Keep
    enum class TouchPanelSensitivityOptions(val value: Int, val xmlValue: String) {
        DO_NOTHING(0, "Do not change"),
        STYLUS_AND_FINGER(1, "Stylus and Finger"),
        GLOVE_AND_FINGER(2, "Glove and Finger"),
        FINGER_ONLY(3, "Finger"),
        STYLUS_GLOVE_AND_FINGER(4, "Stylus and Glove and Finger"),

        ALL_PURPOSE(5, "All Purpose"),

        REDUCED_SENSITIVITY_RAIN(6, "Reduced Sensitivity (Rain)");

        @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
        override fun toString(): String {
            throw RuntimeException("Not Implemented")
        }

        val string: String
            get() = value.toString()
    }

    @Keep
    enum class UsbClientModeDefaultOptions(val value: Int) {
        DO_NOTHING(86),
        CHARGING_ONLY(0),
        FILE_TRANSFER(4),
        MIDI(8),
        PTP(16),
        USB_TETHERING(32);

        @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
        override fun toString(): String {
            throw RuntimeException("Not Implemented")
        }

        val string: String
            get() = value.toString()
    }

    @Keep
    enum class EPermissionType(private val value: String) {
        ACCESS_NOTIFICATIONS("android.permission.ACCESS_NOTIFICATIONS"),
        PACKAGE_USAGE_STATS("android.permission.PACKAGE_USAGE_STATS"),
        SYSTEM_ALERT_WINDOW("android.permission.SYSTEM_ALERT_WINDOW"),
        GET_APP_OPS_STATS("android.permission.GET_APP_OPS_STATS"),
        BATTERY_STATS("android.permission.BATTERY_STATS"),
        MANAGE_EXTERNAL_STORAGE("android.permission.MANAGE_EXTERNAL_STORAGE"),
        BIND_NOTIFICATION_LISTENER("android.permission.BIND_NOTIFICATION_LISTENER"),
        READ_LOGS("android.permission.READ_LOGS"),
        ALL_DANGEROUS_PERMISSIONS("ALL_DANGEROUS_PERMISSIONS"),
        ACCESS_RX_LOGGER("com.zebra.permission.ACCESS_RXLOGGER"),
        SCHEDULE_EXACT_ALARM("android.permission.SCHEDULE_EXACT_ALARM"),
        WRITE_SETTINGS("android.permission.WRITE_SETTINGS"),
        ACCESSIBILITY_SERVICE("ACCESSIBILITY_SERVICE_ACCESS");

        @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
        override fun toString(): String {
            throw RuntimeException("Not Implemented")
        }

        val string: String
            get() = value.toString()

        companion object {
            private val lookup = EPermissionType.entries.associateBy { it.value }
            fun fromString(permissionType: String): EPermissionType? {
                return lookup[permissionType]
            }
        }
    }

    /**
     * Specifies the type of screen lock to be used.
     *
     * - `DO_NOT_CHANGE` (0): This value (or the absence of this parm from the XML) will cause no change to the Screen Lock Type; any previously selected setting will be retained.
     * - `SWIPE` (1): Causes the Swipe screen-lock to be displayed whenever the Lock Screen is invoked.
     * - `PATTERN` (2): Causes the Pattern screen-lock to be displayed whenever the Lock Screen is invoked.
     * - `PIN` (3): Causes the numerical "Pin" screen-lock to be displayed whenever the Lock Screen is invoked.
     * - `PASSWORD` (4): Causes the Password screen-lock to be displayed whenever the Lock Screen is invoked.
     * - `NONE` (5): Prevents the display of any Lock Screen at any time.
     */
    @Keep
    enum class ScreenLockType(val value: Int) {
        DO_NOT_CHANGE(0),
        SWIPE(1),
        PATTERN(2),
        PIN(3),
        PASSWORD(4),
        NONE(5);

        @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
        override fun toString(): String {
            throw RuntimeException("Not Implemented")
        }

        val string: String
            get() = value.toString()
    }

    /**
     * https://techdocs.zebra.com/emdk-for-android/14-0/mx/displaymgr/
     *
     * Screen Shot Enable/Disable
     * Parm Name: ScreenShotUsage
     *
     * Options:
     * 0 - Do Nothing
     * 1 - Enable
     * 2 - Disable
     * */
    @Keep
    enum class ScreenShotUsage(val value: Int) {
        DO_NOTHING(0),
        ENABLE(1),
        DISABLE(2);

        @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
        override fun toString(): String {
            throw RuntimeException("Not Implemented")
        }

        val string: String
            get() = value.toString()
    }

    /**
     * https://techdocs.zebra.com/emdk-for-android/14-0/mx/powerkeymgr/
     *
     * Power-off Button Show/Hide
     * Parm Name: PowerOffState
     *
     * Options:
     * 0 - Do not change: This value (or the absence of this parm from the XML) causes no change to the Power-off Menu; any previously selected setting is retained.
     * 1 - Show: Enables the Power-off button to be shown after the device power key is long-pressed.
     * 2 - Hide: Prevents the Power-off button from being shown after the device power key is long-pressed.
     * */
    @Keep
    enum class ShowHideState(val value: Int) {
        DO_NOT_CHANGE(0),
        SHOW(1),
        HIDE(2);

        @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
        override fun toString(): String {
            throw RuntimeException("Not Implemented")
        }

        val string: String
            get() = value.toString()
    }

    /**
     * https://techdocs.zebra.com/mx/keymappingmgr/#key-identifier
     *
     * */
    @Keep
    enum class KeyIdentifiers(val value: String) {
        VOLUMEUP("VOLUMEUP"),
        VOLUMEDOWN("VOLUMEDOWN"),
        SCAN("SCAN"),
        GRIP_TRIGGER("GRIP_TRIGGER"),
        GRIP_TRIGGER_2("GRIP_TRIGGER_2"),
        LEFT_TRIGGER_1("LEFT_TRIGGER_1"),
        LEFT_TRIGGER_2("LEFT_TRIGGER_2"),
        RIGHT_TRIGGER_1("RIGHT_TRIGGER_1"),
        RIGHT_TRIGGER_2	("RIGHT_TRIGGER_2"),
        LEFT_TRIGGER("LEFT_TRIGGER"),
        RIGHT_TRIGGER("RIGHT_TRIGGER"),
        CENTER_TRIGGER("CENTER_TRIGGER"),
        GUN_TRIGGER("GUN_TRIGGER"),
        HEADSET_HOOK("HEADSET_HOOK"),
        BACK("BACK"),
        HOME("HOME"),
        MENU("MENU"),
        RECENT("RECENT"),
        POWER("POWER"),
        REAR_BUTTON("REAR_BUTTON"),
        LEFT_EXTERNAL_TRIGGER("LEFT_EXTERNAL_TRIGGER"),
        RIGHT_EXTERNAL_TRIGGER("RIGHT_EXTERNAL_TRIGGER"),
        BLUETOOTH_REMOTE_TRIGGER_1("BLUETOOTH_REMOTE_TRIGGER_1"),
        BLUETOOTH_REMOTE_TRIGGER_2("BLUETOOTH_REMOTE_TRIGGER_2"),
        DO_NOT_DISTURB("DO_NOT_DISTURB"), //WS50
        TOP_BUTTON("TOP_BUTTON"), //EM45
        RIGHT_BUTTON("RIGHT_BUTTON"), //EM45
        CHANNEL_SWITCH("CHANNEL_SWITCH"), //FR55
        ALERT_BUTTON("ALERT_BUTTON"), //FR55
        DURESS("DURESS"); //emergency call button

        override fun toString(): String {
            return string
        }

        val string: String
            get() = value.toString()
    }

    /**
     * https://techdocs.zebra.com/mx/keymappingmgr/#key-code
     *
     * */
    @Keep
    enum class KeyCodes(val value: Int) {
        VOLUMEUP(24),
        VOLUMEDOWN(25),
        SCAN(10036),
        LEFT_TRIGGER_1(102),
        RIGHT_TRIGGER_1(103),
        LEFT_TRIGGER_2(104),
        RIGHT_TRIGGER_2	(105),
        HEADSET_HOOK(79),
        BACK(4),
        HOME(3),
        MENU(82),
        POWER(26),
        DO_NOT_DISTURB(10043), //WS50
        CHANNEL_SWITCH(10044), //FR55
        DURESS(10045); //emergency call button

        @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
        override fun toString(): String {
            throw RuntimeException("Not Implemented")
        }

        val string: String
            get() = value.toString()
    }

    @Keep
    enum class OSUpdateStatus(val value: String) {
        UNKNOWN("UNKNOWN"),
        PASSED("PASSED"),
        FAILED("FAILED"),
        CANCELLED("CANCELLED"),
        IN_PROGRESS("IN_PROGRESS"),
        IN_SUSPEND("IN_SUSPEND"),
        WAITING_FOR_REBOOT("WAITING_FOR_REBOOT");

        val string: String
            get() = value.toString()
    }

    /**
     * https://techdocs.zebra.com/mx/powermgr/#wake-up-individual-action-enabledisable
     *
     * */
    @Keep
    enum class WakeUpIndividualAction(val value: Int) {
        DO_NOT_CHANGE(0),
        ON(1),
        OFF(2);

        @Deprecated("please use .string instead", ReplaceWith("string"), level = DeprecationLevel.ERROR)
        override fun toString(): String {
            throw RuntimeException("Not Implemented")
        }

        val string: String
            get() = value.toString()
    }

}
