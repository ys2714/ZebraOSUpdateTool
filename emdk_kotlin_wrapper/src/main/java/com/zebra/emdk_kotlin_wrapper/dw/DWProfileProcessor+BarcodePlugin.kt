package com.zebra.emdk_kotlin_wrapper.dw

import android.content.Context
import android.os.Bundle
import com.zebra.emdk_kotlin_wrapper.utils.AssetsReader
import com.zebra.emdk_kotlin_wrapper.utils.JsonUtils

/**
(*) Notes related to scanner_selection_by_identifier:

Sending "auto" as the scanner identifier in the multiple scanner bundle returns error code "PARAMETER_INVALID" with more detailed error code "AUTO_NOT_SUPPORTED_IN_MULTI_SCANNER_MODE".
Sending an unsupported trigger does not return any error code.
If the same trigger is assigned to a different scanner in a different scanner category, the scanner that is processed last gets the priority. Processing order of the plugins cannot be guaranteed.
Only one internal scanner can be added. If an attempt is made to add another internal scanner, the scanner that is processed last gets the priority. Processing order of the plugins cannot be guaranteed.
Although triggers can be set that are not supported by that device, only supported triggers are displayed in the UI.
When using multiple scanners, the parameter scanner_selection_by_identifier must be used with DataWedge APIs such as SWITCH_SCANNER_PARAMS, SOFT_SCAN_TRIGGER, etc. Otherwise error COMMAND_NOT_SUPPORTED is encountered.
*/
internal fun DWProfileProcessor.bundleForBarcodePlugin(context: Context,
                                              profileName: String,
                                              enable: Boolean,
                                              enableHardTrigger: Boolean,
                                              scanningMode: DWAPI.ScanInputModeOptions): Bundle {
    val pluginEnabled = if (enable) "true" else "false"
    val hardTriggerEnabled = if (enableHardTrigger) DWAPI.BarcodeTriggerMode.ENABLED.string else DWAPI.BarcodeTriggerMode.DISABLED.string

    val jsonString = AssetsReader.readFileToStringWithParams(
        context,
        DWConst.ScannerPluginJSON,
        mapOf(
            DWConst.PROFILE_NAME to profileName,
            DWConst.PROFILE_ENABLED to "true",

            DWConst.scanner_input_enabled to pluginEnabled,
            DWConst.scanner_selection to "auto",
            DWConst.scanner_selection_by_identifier to DWAPI.ScannerIdentifiers.AUTO.value,
            DWConst.scanning_mode to scanningMode.string,
            DWConst.barcode_trigger_mode to hardTriggerEnabled,
            // Code128
            DWConst.decoder_code128 to "true",
            // Others
            DWConst.decoder_ean13 to "true",
            DWConst.decoder_pdf417 to "true",
            DWConst.decoder_qrcode to "true",
            // UPC
            DWConst.decoder_upca to "true",
            // OCR
            "ocr_check_digit_multiplier" to "121212121212",
            "ocr_lines" to "1",
            "ocr_b_variant" to "0",
            "ocr_quiet_zone" to "50",
            "decoder_ocr_a" to "false",
            "decoder_ocr_b" to "false",
            "ocr_subset" to "!\\\"#\$%()*+,-.\\/0123456789<>ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz\\\\^|",
            "ocr_check_digit_modulus" to "1",
            "ocr_orientation" to "0",
            "ocr_a_variant" to "0",
            "ocr_b_variant" to "0",
            "ocr_check_digit_validation" to "0",
            "ocr_max_chars" to "100",
            "ocr_template" to "99999999",
            "ocr_min_chars" to "3"
        )
    )
    val bundle = JsonUtils.jsonToBundle(jsonString)
    return bundle
}



/*
OCR settings for BARCODE

"ocr_orientation": "0",
"ocr_lines": "1",
"ocr_min_chars": "3",
"ocr_max_chars": "100",
"ocr_subset": "!\"#$%()*+,-./0123456789<>ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz\\^|",
"ocr_quiet_zone": "60",
"ocr_template": "99999999",
"ocr_check_digit_modulus": "10",
"ocr_check_digit_multiplier": "121212121212",
"ocr_check_digit_validation": "3",
"inverse_ocr": "2",
*/
