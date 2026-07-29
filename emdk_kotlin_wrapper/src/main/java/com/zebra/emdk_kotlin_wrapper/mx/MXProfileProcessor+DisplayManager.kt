package com.zebra.emdk_kotlin_wrapper.mx

import android.content.Context

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
internal fun MXProfileProcessor.setScreenShotUsage(context: Context, usage: MXBase.ScreenShotUsage, delaySeconds: Long = 0, callback: (MXBase.ErrorInfo?) -> Unit) {
    processProfileWithCallback(
        context,
        MXBase.ProfileXML.DisplayManagerDisableScreenShot,
        MXBase.ProfileName.DisplayManagerDisableScreenShot,
        mapOf(
            MXConst.ScreenShotUsage to usage.string
        ),
        delaySeconds,
        callback
    )
}

internal fun MXProfileProcessor.enableScreenShotFeature(context: Context, delaySeconds: Long = 0, callback: (MXBase.ErrorInfo?) -> Unit) {
    processProfileWithCallback(
        context,
        MXBase.ProfileXML.DisplayManagerDisableScreenShot,
        MXBase.ProfileName.DisplayManagerDisableScreenShot,
        mapOf(
            MXConst.ScreenShotUsage to MXBase.ScreenShotUsage.ENABLE.string
        ),
        delaySeconds,
        callback
    )
}