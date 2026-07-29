package com.zebra.emdk_kotlin_wrapper.dw

import android.content.Context
import android.os.Bundle
import com.zebra.emdk_kotlin_wrapper.utils.AssetsReader
import com.zebra.emdk_kotlin_wrapper.utils.JsonUtils

internal fun DWProfileProcessor.bundleForIntentPlugin(context: Context,
                                             profileName: String,
                                             intentAction: DWAPI.ResultActionNames,
                                             intentCategory: DWAPI.ResultCategoryNames,
                                             deliveryOptions: DWAPI.IntentDeliveryOptions): Bundle {
    val jsonString = AssetsReader.readFileToStringWithParams(
        context,
        DWConst.IntentPluginJSON,
        mapOf(
            DWConst.PROFILE_NAME to profileName,
            DWConst.PROFILE_ENABLED to "true",
            DWConst.intent_output_enabled to "true",
            DWConst.intent_action to intentAction.value,
            DWConst.intent_category to intentCategory.value,
            DWConst.intent_delivery to deliveryOptions.string
        )
    )
    return JsonUtils.jsonToBundle(jsonString)
}
