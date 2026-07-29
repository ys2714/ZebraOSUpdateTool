package com.zebra.emdk_kotlin_wrapper.dw

import android.content.Context
import android.os.Bundle
import com.zebra.emdk_kotlin_wrapper.utils.AssetsReader
import com.zebra.emdk_kotlin_wrapper.utils.JsonUtils

internal fun DWProfileProcessor.bundleForBDFPlugin(context: Context,
                       profileName: String,
                       outputPlugin: DWAPI.Plugin.Output,
                       sendDate: Boolean): Bundle {
    val sendDateStr = if (sendDate) "true" else "false"
    val jsonString = AssetsReader.readFileToStringWithParams(
        context,
        DWConst.BDFPluginJSON,
        mapOf(
            DWConst.PROFILE_NAME to profileName,
            DWConst.PROFILE_ENABLED to "true",
            DWConst.PACKAGE_NAME to context.packageName,
            DWConst.OUTPUT_PLUGIN_NAME to outputPlugin.value,
            DWConst.bdf_enabled to "true",
            DWConst.bdf_send_data to sendDateStr
        )
    )
    return JsonUtils.jsonToBundle(jsonString)
}