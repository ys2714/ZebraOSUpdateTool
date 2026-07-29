package com.zebra.emdk_kotlin_wrapper.mx

import android.content.Context

/**
 * https://techdocs.zebra.com/mx/batterymgr/
 *
 * */
internal fun MXProfileProcessor.setBatteryCriticalLowThreshold(context: Context, threshold: Int, callback: (MXBase.ErrorInfo?) -> Unit) {
    processProfileWithCallback(
        context,
        MXBase.ProfileXML.BatteryManagerSetCriticalLowThreshold,
        MXBase.ProfileName.BatteryManagerSetCriticalLowThreshold,
        mapOf(
            MXConst.SetCriticalLowThreshold to threshold.toString()
        ),
        0,
        callback
    )
}