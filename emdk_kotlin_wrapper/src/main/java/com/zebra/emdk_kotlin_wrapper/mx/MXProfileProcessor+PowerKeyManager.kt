package com.zebra.emdk_kotlin_wrapper.mx

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * https://techdocs.zebra.com/emdk-for-android/14-0/mx/powerkeymgr/
 *
 * Main Functionality
 * Enable/Disable the following Power-off Menu Options:
 *  - Power-off Button
 *  - Airplane Mode
 *  - Touch Panel
 *  - Safe Mode
 * Enable/Disable Automatic Screen Lock Settings panel option
 * Enable/Disable "Lock screen instantly with power key" Settings panel option
 * */

/**
 *  https://techdocs.zebra.com/emdk-for-android/14-0/mx/powerkeymgr/
 *
 *  Power-off Button Show/Hide (MX11.4+)
 *  Parm Name: PowerOffState
 *  Options:
 *  0 - Do not change: This value (or the absence of this parm from the XML) causes no change to the Power-off Menu; any previously selected setting is retained.
 *  1 - Show: Enables the Power-off button to be shown after the device power key is long-pressed.
 *  2 - Hide: Prevents the Power-off button from being shown after the device power key is long-pressed.
 * */
@RequiresApi(Build.VERSION_CODES.R)
internal fun MXProfileProcessor.powerKeyMenuEnablePowerOffButton(context: Context, enable: Boolean, delaySeconds: Long, callback: (MXBase.ErrorInfo?) -> Unit) {
    val enableValue = if (enable) MXBase.ShowHideState.SHOW.string else MXBase.ShowHideState.HIDE.string
    processProfileWithCallback(
        context,
        MXBase.ProfileXML.PowerKeyManagerSetPowerOffState,
        MXBase.ProfileName.PowerKeyManagerSetPowerOffState,
        mapOf(
            MXConst.PowerOffState to enableValue
        ),
        delaySeconds,
        callback
    )
}

/**
 * https://techdocs.zebra.com/emdk-for-android/14-0/mx/powerkeymgr/
 *
 * Airplane Mode Show/Hide (MX4.3+) Note: Not supported in devices running Android 8.x Oreo or later
 * Parm Name: AirPlaneMode
 * Options:
 * 0 - Do not change: This value (or the absence of this parm from the XML) causes no change to Power-off Menu settings; any previously selected setting is retained.
 * 1 - Show Menu Option: Enables Airplane Mode to be controlled from the Power Off menu.
 * 2 - Do not show Menu Option: Prevents Airplane Mode from being controlled from the Power Off menu (see NOTE, above).
 * */
internal fun MXProfileProcessor.powerKeyMenuEnableAirplanModeButton(context: Context, enable: Boolean, delaySeconds: Long, callback: (MXBase.ErrorInfo?) -> Unit) {
    val enableValue = if (enable) MXBase.ShowHideState.SHOW.string else MXBase.ShowHideState.HIDE.string
    processProfileWithCallback(
        context,
        MXBase.ProfileXML.PowerKeyManagerSetPowerOffState,
        MXBase.ProfileName.PowerKeyManagerSetPowerOffState,
        mapOf(
            MXConst.AirPlaneMode to enableValue
        ),
        delaySeconds,
        callback
    )
}

/**
 * https://techdocs.zebra.com/emdk-for-android/14-0/mx/powerkeymgr/
 *
 * Touch Panel Show/Hide (MX5.0+) Note: this is for glove mode
 * Parm Name: TouchPanel
 * Options:
 * 0 - Do not change: This value (or the absence of this parm from the XML) causes no change to the Power-off Menu; any previously selected setting is retained.
 * 1 - Show Menu Option: Enables the Touch Panel to be controlled from the Power-off Menu (if the device supports this option).
 * 2 - Do not show Menu Option: Prevents the Touch Panel from being controlled from the Power-off Menu (if the device supports this option).
 * */
internal fun MXProfileProcessor.powerKeyMenuEnableTouchPanel(context: Context, enable: Boolean, delaySeconds: Long, callback: (MXBase.ErrorInfo?) -> Unit) {
    val enableValue = if (enable) MXBase.ShowHideState.SHOW.string else MXBase.ShowHideState.HIDE.string
    processProfileWithCallback(
        context,
        MXBase.ProfileXML.PowerKeyManagerSetPowerOffState,
        MXBase.ProfileName.PowerKeyManagerSetPowerOffState,
        mapOf(
            MXConst.TouchPanel to enableValue
        ),
        delaySeconds,
        callback
    )
}

/**
 * https://techdocs.zebra.com/emdk-for-android/14-0/mx/powerkeymgr/
 *
 * Touch Panel Mode Show/Hide (MX10.2+) Note: this is for glove mode
 * Parm Name: TouchPanelMode
 * Options:
 * 0 - Do not change: This value (or the absence of this parm from the XML) causes no change to the Power-off Menu; any previously selected setting is retained.
 * 1 - Show: Enables the Touch Panel Mode to be controlled from the Power-off Menu (if the device supports this option).
 * 2 - Do not show: Prevents the Touch Panel Mode from being controlled from the Power-off Menu (if the device supports this option).
 */
internal fun MXProfileProcessor.powerKeyMenuEnableTouchPanelMode(context: Context, enable: Boolean, delaySeconds: Long, callback: (MXBase.ErrorInfo?) -> Unit) {
    val enableValue = if (enable) MXBase.ShowHideState.SHOW.string else MXBase.ShowHideState.HIDE.string
    processProfileWithCallback(
        context,
        MXBase.ProfileXML.PowerKeyManagerSetPowerOffState,
        MXBase.ProfileName.PowerKeyManagerSetPowerOffState,
        mapOf(
            MXConst.TouchPanelMode to enableValue
        ),
        delaySeconds,
        callback
    )
}

/**
 * Safe Mode Show/Hide (MX4.3+)
 * Parm Name: SafeMode
 * Options:
 * 0 - Do not change: This value (or the absence of this parm from the XML) causes no change to the Power-off Menu; any previously selected setting is retained.
 * 1 - Show Menu Option: Enables Safe Mode to be controlled from the Power-off Menu.
 * 2 - Do not show Menu Option: Prevents Safe Mode from being controlled from the Power-off Menu (see NOTE, above).
 * */
internal fun MXProfileProcessor.powerKeyMenuEnableSafeMode(context: Context, enable: Boolean, delaySeconds: Long, callback: (MXBase.ErrorInfo?) -> Unit) {
    val enableValue = if (enable) MXBase.ShowHideState.SHOW.string else MXBase.ShowHideState.HIDE.string
    processProfileWithCallback(
        context,
        MXBase.ProfileXML.PowerKeyManagerSetPowerOffState,
        MXBase.ProfileName.PowerKeyManagerSetPowerOffState,
        mapOf(
            MXConst.SafeMode to enableValue
        ),
        delaySeconds,
        callback
    )
}

/**
 * Automatic Screen Lock Enable/Disable (MX4.3+)
 * Parm Name: AutoScreenLockState
 *
 * Options:
 * 0 - Do not change: This value (or the absence of this parm from the XML) causes no change to screen lock settings; any previously selected setting is retained.
 * 1 - Turn on: Forces the screen to lock whenever the screen is turned off using the power key.
 * 2 - Turn off: Prevents the screen from locking when turned off using the power key unless the Screen-lock Timeout Interval was exceeded.
 * */
internal fun MXProfileProcessor.powerKeyTriggerAutoScreenLock(context: Context, enable: Boolean, delaySeconds: Long, callback: (MXBase.ErrorInfo?) -> Unit) {
    val enableValue = if (enable) MXBase.ShowHideState.SHOW.string else MXBase.ShowHideState.HIDE.string
    processProfileWithCallback(
        context,
        MXBase.ProfileXML.PowerKeyManagerSetPowerOffState,
        MXBase.ProfileName.PowerKeyManagerSetPowerOffState,
        mapOf(
            MXConst.AutoScreenLockState to enableValue
        ),
        delaySeconds,
        callback
    )
}

/**
 * Automatic Screen Lock Show/Hide in Settings (MX4.3+)
 * Parm Name: AutoScreenLockOption
 *
 * Options:
 * 0 - Do not change: This value (or the absence of this parm from the XML) causes no change to Power-off Menu settings; any previously selected setting is retained.
 * 1 - Turn on: Enables the Auto Screen Lock option to be controlled from the Android Settings panel.
 * 2 - Turn off: Prevents the Auto Screen Lock option from being controlled from the Android Settings panel.
 * */
internal fun MXProfileProcessor.powerKeyAutoScreenLockSettingsOptionEnable(context: Context, enable: Boolean, delaySeconds: Long, callback: (MXBase.ErrorInfo?) -> Unit) {
    val enableValue = if (enable) MXBase.ShowHideState.SHOW.string else MXBase.ShowHideState.HIDE.string
    processProfileWithCallback(
        context,
        MXBase.ProfileXML.PowerKeyManagerSetPowerOffState,
        MXBase.ProfileName.PowerKeyManagerSetPowerOffState,
        mapOf(
            MXConst.AutoScreenLockOption to enableValue
        ),
        delaySeconds,
        callback
    )
}