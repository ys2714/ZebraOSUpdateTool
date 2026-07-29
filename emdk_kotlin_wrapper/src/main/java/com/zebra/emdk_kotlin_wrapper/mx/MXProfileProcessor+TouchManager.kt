package com.zebra.emdk_kotlin_wrapper.mx

import android.content.Context

/**
 * https://techdocs.zebra.com/mx/touchmgr/
 *
 * CAUTION!!!
 * please use following strings instead of int value
 * the EMDK Android Studio plugin exported xml showing these strings.
 *
 * Do not change
 * Finger
 * Glove and Finger
 * Stylus and Finger
 * Stylus and Glove and Finger
 * [TC201] All Purpose
 * [TC201] Reduced Sensitivity (Rain)
 * */
@JvmOverloads
internal fun MXProfileProcessor.configTouchPanelSensitivity(
    context: Context,
    option: MXBase.TouchPanelSensitivityOptions,
    delaySeconds: Long = 0,
    callback: (MXBase.ErrorInfo?) -> Unit) {
    processProfileWithCallback(
        context,
        MXBase.ProfileXML.TouchPanelSensitivity,
        MXBase.ProfileName.TouchPanelSensitivity,
        mapOf(
            MXConst.TouchActionAny to option.xmlValue
        ),
        delaySeconds,
        callback
    )
}