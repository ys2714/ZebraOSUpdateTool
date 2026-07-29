package com.zebra.emdk_kotlin_wrapper.dw

import android.content.Context
import androidx.annotation.Keep

@Keep
class DWWorkflowFreeFormScanner(override val context: Context): DWVirtualScanner(context) {

    override val createJSONFileName: String
        get() = "barcode_intent_advanced_create.json"

    override val updateJSONFileName: String
        get() = "barcode_intent_advanced_update.json"

    override val parameters: Map<String, String>
        get() = mapOf(
            "PROFILE_NAME" to "DWWorkflowFreeFormScanner",
            "scanner_input_enabled" to "false",
            "workflow_input_enabled" to "true",
            "barcode_trigger_mode" to "0",
            "decode_audio_feedback" to "false",
            "decode_audio_feedback_uri" to "",
            "aim_type" to "8",
            "aim_timer" to "6000",
            "beam_timer" to "6000"
        )
}