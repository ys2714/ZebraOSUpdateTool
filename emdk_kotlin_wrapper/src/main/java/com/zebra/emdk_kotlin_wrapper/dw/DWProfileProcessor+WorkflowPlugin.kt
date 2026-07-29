package com.zebra.emdk_kotlin_wrapper.dw

import android.content.Context
import android.os.Bundle
import com.zebra.emdk_kotlin_wrapper.utils.AssetsReader
import com.zebra.emdk_kotlin_wrapper.utils.JsonUtils

internal fun DWProfileProcessor.bundleForWorkflowPlugin(context: Context,
                      profileName: String, enabled: Boolean): Bundle {
    val enabledString = if (enabled) "true" else "false"
    val jsonString = AssetsReader.readFileToStringWithParams(
        context,
        DWConst.WorkflowPluginJSON,
        mapOf(
            DWConst.PROFILE_NAME to profileName,
            DWConst.PROFILE_ENABLED to "true",
            "workflow_input_enabled" to enabledString
        )
    )
    return JsonUtils.jsonToBundle(jsonString)
}