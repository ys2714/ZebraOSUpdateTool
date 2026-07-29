package com.zebra.emdk_kotlin_wrapper.dw

import android.content.Context
import android.os.Bundle
import com.zebra.emdk_kotlin_wrapper.utils.AssetsReader
import com.zebra.emdk_kotlin_wrapper.utils.JsonUtils
import com.zebra.emdk_kotlin_wrapper.utils.PackageUtils

internal fun DWProfileProcessor.bundleForIntentSecurePlugin(context: Context,
                                profileName: String,
                                intentAction: String,
                                deliveryOptions: DWAPI.IntentDeliveryOptions): Bundle {
    val jsonString = AssetsReader.readFileToStringWithParams(
        context,
        DWConst.IntentSecurePluginJSON,
        mapOf(
            DWConst.PROFILE_NAME to profileName,
            DWConst.PROFILE_ENABLED to "true",
            DWConst.PACKAGE_NAME to context.packageName,
            DWConst.intent_output_enabled to "true",
            DWConst.intent_action to intentAction,
            DWConst.intent_category to "android.intent.category.DEFAULT",
            DWConst.intent_delivery to deliveryOptions.string,
            DWConst.intent_use_content_provider to "false",
            DWConst.SIGNATURE to PackageUtils.getPackageSignature(context)
        )
    )
    return JsonUtils.jsonToBundle(jsonString)
}