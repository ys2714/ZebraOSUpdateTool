package com.zebra.emdk_kotlin_wrapper.dw

import android.content.Context
import android.os.Bundle
import com.zebra.emdk_kotlin_wrapper.utils.AssetsReader
import com.zebra.emdk_kotlin_wrapper.utils.JsonUtils

internal fun DWProfileProcessor.bundleForKeystrokePlugin(context: Context, profileName: String, enable: Boolean): Bundle {
    val jsonString = AssetsReader.readFileToStringWithParams(
        context,
        DWConst.KeystrokePluginJSON,
        mapOf(
            DWConst.PROFILE_NAME to profileName,
            DWConst.PROFILE_ENABLED to "true",
            DWConst.keystroke_output_enabled to if (enable) "true" else "false"
        )
    )
    return JsonUtils.jsonToBundle(jsonString)
}