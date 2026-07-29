package com.zebra.emdk_kotlin_wrapper.dw

import android.content.Context
import android.os.Bundle
import androidx.annotation.Keep

/**
 * https://techdocs.zebra.com/datawedge/7-5/guide/api/setconfig/#scannerinputparameters
 * */
@Keep
open class DWBarcodeScanner(override val context: Context): DWVirtualScanner(context) {

    @Keep
    enum class DecoderType(val key: String) {
        AZTEC("decoder_aztec"),
        CODE39("decoder_code39"),
        CODE_128("decoder_code128"),
        NW7("decoder_codabar"),
        QR("decoder_qrcode"),
        ITF("decoder_i2of5"),
        PDF_417("decoder_pdf417"),
        MICRO_PDF("decoder_micropdf"),
        DATA_MATRIX("decoder_datamatrix"),
        JAN_EAN_8("decoder_ean8"),
        JAN_EAN_13("decoder_ean13"),
        UPCA("decoder_upca"),
        UPCE0("decoder_upce0"),
        UPCE1("decoder_upce1"),
        MAILMARK("decoder_mailmark"),
        MAXICODE("decoder_maxicode"),
        GS1_DATABAR("decoder_gs1_databar"),
        GS1_DATABAR_EXP("decoder_gs1_databar_exp"),
        CODABAR("decoder_codabar")
    }

    /**
     * aim_type:
     *
     * 0 - Trigger
     * 1 - Timed Hold
     * 2 - Timed Release
     * 3 - Press And Release
     * 4 - Presentation
     * 5 - Continuous Read
     * 6 - Press and Sustain
     * 7 – Press and Continue
     * 8 - Timed Continuous
     * */
    @Keep
    enum class AimType(val value: Int) {
        TRIGGER(0),
        TIMED_HOLD(1),
        TIMED_RELEASE(2),
        PRESS_AND_RELEASE(3),
        PRESENTATION(4),
        CONTINUOUS_READ(5),
        PRESS_AND_SUSTAIN(6),
        PRESS_AND_CONTINUE(7),
        TIMED_CONTINUOUS(8);

        val string: String = value.toString()
    }

    override val createJSONFileName: String
        get() = "barcode_intent_advanced_create.json"

    override val updateJSONFileName: String
        get() = "barcode_intent_advanced_update.json"

    // DataWedge built-in beep has significant impact on response speed of API calling.
    // so I suggest to turn OFF the built-in beep and manually call ToneGenerator on app side.
    override val parameters: Map<String, String>
        get() = mapOf(
            "PROFILE_NAME" to "DWBarcodeScanner",
            "scanner_input_enabled" to "true",
            "workflow_input_enabled" to "false",
            "barcode_trigger_mode" to "0",
            "decode_audio_feedback" to "false",
            "decode_audio_feedback_uri" to "",
            "aim_type" to "0",
            "aim_timer" to "6000",
            "beam_timer" to "6000"
        )

    @Keep
    fun switchAimType(aimType: AimType): DWBarcodeScanner {
        DataWedgeHelper.switchScannerParams(
            context,
            Bundle().apply {
                putString("aim_type", aimType.string)
            }
        )
        return this
    }

    @Keep
    fun switchDecoderType(decoderTypes: Array<DecoderType>): DWBarcodeScanner {
        val bundle = Bundle()
        for (type in DecoderType.entries) {
            bundle.putString(type.key, "false")
        }
        for (type in decoderTypes) {
            bundle.putString(type.key, "true")
        }
        DataWedgeHelper.switchScannerParams(
            context,
            bundle
        )
        return this
    }
}
