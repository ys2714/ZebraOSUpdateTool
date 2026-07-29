package com.zebra.emdk_kotlin_wrapper.dw

import androidx.annotation.Keep

@Keep
object DWConst {

    // JSON file names
    const val BindProfileJSON = "bind_profile_to_app.json"
    const val ScannerPluginJSON = "plugin_scanner.json"
    const val WorkflowPluginJSON = "plugin_workflow.json"
    const val IntentPluginJSON = "plugin_intent.json"
    const val IntentSecurePluginJSON = "plugin_intent_secure.json"
    const val BDFPluginJSON = "plugin_bdf.json"
    const val KeystrokePluginJSON = "plugin_keystroke.json"

    // Common
    const val SEND_RESULT = "SEND_RESULT"
    const val COMMAND_IDENTIFIER = "COMMAND_IDENTIFIER"
    const val PROFILE_NAME = "PROFILE_NAME"
    const val PROFILE_ENABLED = "PROFILE_ENABLED"
    const val CONFIG_MODE = "CONFIG_MODE"
    const val PLUGIN_CONFIG = "PLUGIN_CONFIG"
    const val PACKAGE_NAME = "PACKAGE_NAME"
    const val SIGNATURE = "SIGNATURE"
    const val PLUGIN_NAME = "PLUGIN_NAME"
    const val SET_CONFIG = "SET_CONFIG"

    /**
     * OUTPUT_PLUGIN_NAME [String]: Applies only to ADF and BDF when specified as the PLUGIN_NAME. Specifies the output plug-in associated with the ADF or BDF parameters:
     * - KEYSTROKE
     * - INTENT
     * - IP
     * */
    const val OUTPUT_PLUGIN_NAME = "OUTPUT_PLUGIN_NAME"
    const val RESET_CONFIG = "RESET_CONFIG"
    const val PARAM_LIST = "PARAM_LIST"

    // Plugin BDF
    const val bdf_enabled = "bdf_enabled"
    const val bdf_send_data = "bdf_send_data"

    // Plugin BARCODE
    const val scanner_input_enabled = "scanner_input_enabled"
    const val scanner_selection = "scanner_selection"
    const val scanner_selection_by_identifier = "scanner_selection_by_identifier"
    const val scanning_mode = "scanning_mode"
    const val barcode_trigger_mode = "barcode_trigger_mode"

    // Plugin Keystroke
    const val keystroke_output_enabled = "keystroke_output_enabled"

    // Plugin Intent
    const val intent_output_enabled = "intent_output_enabled"
    const val intent_action = "intent_action"
    const val intent_category = "intent_category"
    const val intent_delivery = "intent_delivery"
    const val intent_component_info = "intent_component_info"
    const val intent_use_content_provider = "intent_use_content_provider"

    // Decoder
    const val decoder_australian_postal = "decoder_australian_postal"
    const val decoder_aztec = "decoder_aztec"
    const val decoder_canadian_postal = "decoder_canadian_postal"
    const val decoder_chinese_2of5 = "decoder_chinese_2of5"
    const val decoder_codabar = "decoder_codabar"
    const val decoder_code11 = "decoder_code11"
    const val decoder_code32 = "decoder_code32"
    const val decoder_code39 = "decoder_code39"
    const val decoder_code93 = "decoder_code93"
    const val decoder_code128 = "decoder_code128"
    const val decoder_composite_ab = "decoder_composite_ab"
    const val decoder_composite_c = "decoder_composite_c"
    const val decoder_datamatrix = "decoder_datamatrix"
    const val decoder_signature = "decoder_signature"
    const val decoder_d2of5 = "decoder_d2of5"
    const val decoder_dotcode = "decoder_dotcode"
    const val decoder_dutch_postal = "decoder_dutch_postal"
    const val decoder_ean8 = "decoder_ean8"
    const val decoder_ean13 = "decoder_ean13"
    const val decoder_finnish_postal_4s = "decoder_finnish_postal_4s"
    const val decoder_grid_matrix = "decoder_grid_matrix"
    const val decoder_gs1_databar = "decoder_gs1_databar"
    const val decoder_gs1_databar_lim = "decoder_gs1_databar_lim"
    const val decoder_gs1_databar_exp = "decoder_gs1_databar_exp"
    const val decoder_gs1_datamatrix = "decoder_gs1_datamatrix"
    const val decoder_gs1_qrcode = "decoder_gs1_qrcode"
    const val decoder_hanxin = "decoder_hanxin"
    const val decoder_i2of5 = "decoder_i2of5"
    const val decoder_japanese_postal = "decoder_japanese_postal"
    const val decoder_korean_3of5 = "decoder_korean_3of5"
    const val decoder_mailmark = "decoder_mailmark"
    const val decoder_matrix_2of5 = "decoder_matrix_2of5"
    const val decoder_maxicode = "decoder_maxicode"
    const val decoder_micr_e13b = "decoder_micr_e13b"
    const val decoder_micropdf = "decoder_micropdf"
    const val decoder_microqr = "decoder_microqr"
    const val decoder_msi = "decoder_msi"
    const val decoder_ocr_a = "decoder_ocr_a"
    const val decoder_ocr_b = "decoder_ocr_b"
    const val decoder_pdf417 = "decoder_pdf417"
    const val decoder_qrcode = "decoder_qrcode"
    const val decoder_tlc39 = "decoder_tlc39"
    const val decoder_trioptic39 = "decoder_trioptic39"
    const val decoder_uk_postal = "decoder_uk_postal"
    const val decoder_us_currency = "decoder_us_currency"
    const val decoder_usplanet = "decoder_usplanet"
    const val decoder_us_postal = "decoder_us_postal"
    const val decoder_uspostnet = "decoder_uspostnet"
    const val decoder_upca = "decoder_upca"
    const val decoder_upce0 = "decoder_upce0"
    const val decoder_upce1 = "decoder_upce1"
    const val decoder_us4state = "decoder_us4state"
    const val decoder_us4state_fics = "decoder_us4state_fics"

    // Decoder Params
    const val decoder_codabar_length1 = "decoder_codabar_length1"
    const val decoder_codabar_length2 = "decoder_codabar_length2"
    const val decoder_codabar_redundancy = "decoder_codabar_redundancy"
    const val decoder_codabar_clsi_editing = "decoder_codabar_clsi_editing"
    const val decoder_codabar_notis_editing = "decoder_codabar_notis_editing"
    const val decoder_code39_length1 = "decoder_code39_length1"
    const val decoder_code39_length2 = "decoder_code39_length2"
    const val decoder_code39_redundancy = "decoder_code39_redundancy"
    const val decoder_code39_verify_check_digit = "decoder_code39_verify_check_digit"
    const val decoder_code39_report_check_digit = "decoder_code39_report_check_digit"
    const val decoder_code39_full_ascii = "decoder_code39_full_ascii"
    const val decoder_code39_convert_to_code32 = "decoder_code39_convert_to_code32"
    const val decoder_code39_report_code32_prefix = "decoder_code39_report_code32_prefix"
    const val code39_enable_marginless_decode = "code39_enable_marginless_decode"
    const val decoder_code39_security_level = "decoder_code39_security_level"
    const val decoder_code128_length1 = "decoder_code128_length1"
    const val decoder_code128_length2 = "decoder_code128_length2"
    const val decoder_code128_redundancy = "decoder_code128_redundancy"
    const val decoder_code128_enable_ean128 = "decoder_code128_enable_ean128"
    const val decoder_code128_enable_isbt128 = "decoder_code128_enable_isbt128"
    const val decoder_code128_enable_plain = "decoder_code128_enable_plain"
    const val decoder_code128_isbt128_concat_mode = "decoder_code128_isbt128_concat_mode"
    const val decoder_code128_check_isbt_table = "decoder_code128_check_isbt_table"
    const val decoder_code128_security_level = "decoder_code128_security_level"
    const val code128_enable_marginless_decode = "code128_enable_marginless_decode"
    const val code128_ignore_fnc4 = "code128_ignore_fnc4"
    const val decoder_composite_ab_ucc_link_mode = "decoder_composite_ab_ucc_link_mode"
    const val decoder_upca_report_check_digit = "decoder_upca_report_check_digit"
    const val decoder_upca_preamble = "decoder_upca_preamble"
    const val decoder_upce0_report_check_digit = "decoder_upce0_report_check_digit"
    const val decoder_upce0_convert_to_upca = "decoder_upce0_convert_to_upca"
}