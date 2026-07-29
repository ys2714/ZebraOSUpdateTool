package com.zebra.emdk_kotlin_wrapper.mx

import android.content.Context

/**
 * https://techdocs.zebra.com/mx/usbmgr/
 *
 * USB Client Mode Default
 * */
@JvmOverloads
internal fun MXProfileProcessor.setUSBClientModeDefault(
    context: Context,
    option: MXBase.UsbClientModeDefaultOptions,
    delaySeconds: Long = 0,
    callback: (MXBase.ErrorInfo?) -> Unit) {
    processProfileWithCallback(
        context,
        MXBase.ProfileXML.UsbClientModeDefault,
        MXBase.ProfileName.UsbClientModeDefault,
        mapOf(
            MXConst.UsbClientModeDefault to option.string
        ),
        delaySeconds,
        callback
    )
}