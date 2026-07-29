package com.zebra.emdk_kotlin_wrapper.dw

import android.content.Context
import android.os.Bundle

internal fun DWIntentFactory.simpleCreateProfileBundle(context: Context, profileName: String): Bundle {
    val profileConfig1 = Bundle().apply {
        putString("PROFILE_NAME", "$profileName")
        putString("PROFILE_ENABLED", "true")
        putString("CONFIG_MODE", "CREATE_IF_NOT_EXIST")
        putParcelableArray("APP_LIST", arrayOf(
            Bundle().apply {
                putString("PACKAGE_NAME", context.packageName)
                putStringArray("ACTIVITY_LIST", arrayOf<String>("*"))
            }
        ))
    }
    return profileConfig1
}

internal fun DWIntentFactory.simpleBarcodePluginBundle(context: Context, profileName: String): Bundle {
    val profileConfig1 = Bundle().apply {
        putString("PROFILE_NAME", "$profileName")
        putString("PROFILE_ENABLED", "true")
        putString("CONFIG_MODE", "UPDATE")
        putBundle("PLUGIN_CONFIG",
            Bundle().apply {
                putString("PLUGIN_NAME", "BARCODE")
                putString("RESET_CONFIG", "true") //  This is the default
                putBundle("PARAM_LIST", Bundle().apply {
                    putString("scanner_selection", "auto")
                    putString("scanner_input_enabled", "true")
                    putString("decoder_code128", "true")
                    putString("decoder_code39", "true")
                    putString("decoder_ean13", "true")
                    putString("decoder_upca", "true")
                })
            }
        )
    }
    return profileConfig1
}

internal fun DWIntentFactory.simpleKeystrokePluginBundle(context: Context, profileName: String): Bundle {
    val profileConfig1 = Bundle().apply {
        putString("PROFILE_NAME", "$profileName")
        putString("PROFILE_ENABLED", "true")
        putString("CONFIG_MODE", "UPDATE")
        putBundle("PLUGIN_CONFIG",
            Bundle().apply {
                putString("PLUGIN_NAME", "KEYSTROKE")
                putString("RESET_CONFIG", "true") //  This is the default
                putBundle("PARAM_LIST", Bundle().apply {
                    putString("keystroke_output_enabled", "false")
                })
            }
        )
    }
    return profileConfig1
}

internal fun DWIntentFactory.simpleIntentPluginBundle(context: Context, profileName: String, intentAction: String): Bundle {
    val profileConfig1 = Bundle().apply {
        putString("PROFILE_NAME", "$profileName")
        putString("PROFILE_ENABLED", "true")
        putString("CONFIG_MODE", "UPDATE")
        putBundle("PLUGIN_CONFIG",
            Bundle().apply {
                putString("PLUGIN_NAME", "INTENT")
                putString("RESET_CONFIG", "true")
                putBundle("PARAM_LIST", Bundle().apply {
                    putString("intent_output_enabled", "true")
                    putString("intent_action", "$intentAction")
                    putString("intent_category", "android.intent.category.DEFAULT")
                    putString("intent_delivery", DWAPI.IntentDeliveryOptions.BROADCAST.string)
                })
            }
        )
    }
    return profileConfig1
}

internal fun DWIntentFactory.barcodeInputIntentOutputBundle(context: Context, profileName: String, intentAction: String): Bundle {
    val profileConfig = Bundle().apply {
        putString("PROFILE_NAME", "$profileName")
        putString("PROFILE_ENABLED", "true")
        putString("CONFIG_MODE", "CREATE_IF_NOT_EXIST")
        putParcelableArrayList("PLUGIN_CONFIG", arrayListOf(
            Bundle().apply {
                putString("PLUGIN_NAME", "BARCODE")
                putString("RESET_CONFIG", "true") //  This is the default
                putBundle("PARAM_LIST", Bundle().apply {
                    putString("scanner_selection", "auto")
                    putString("scanner_selection_by_identifier", "AUTO")
                    putString("scanner_input_enabled", "true")
                    putString("decoder_code128", "true")
                    putString("decoder_ean13", "true")
                })
            },
            Bundle().apply {
                putString("PLUGIN_NAME", "KEYSTROKE")
                putString("RESET_CONFIG", "true") //  This is the default
                putBundle("PARAM_LIST", Bundle().apply {
                    putString("keystroke_output_enabled", "false")
                })
            },
            Bundle().apply {
                putString("PLUGIN_NAME", "INTENT")
                putString("RESET_CONFIG", "true")
                putBundle("PARAM_LIST", Bundle().apply {
                    putString("intent_output_enabled", "true")
                    putString("intent_action", "$intentAction")
                    putString("intent_category", "android.intent.category.DEFAULT")
                    putString("intent_delivery", DWAPI.IntentDeliveryOptions.BROADCAST.string)
                })
            }
        ))
    }
    return profileConfig
}