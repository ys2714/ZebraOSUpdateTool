package com.zebra.emdk_kotlin_wrapper.dw

import android.os.Bundle

private fun configOCRParams(bundle: Bundle): Bundle {
    // Decoder
    bundle.putString(DWAPI.OCRParams.Decoder.ENABLE_OCR_A, DWAPI.StringBoolean.TRUE.value)
    bundle.putString(DWAPI.OCRParams.Decoder.ENABLE_OCR_B, DWAPI.StringBoolean.TRUE.value)
    bundle.putString(DWAPI.OCRParams.Decoder.ENABLE_MICR, DWAPI.StringBoolean.TRUE.value)
    bundle.putString(DWAPI.OCRParams.Decoder.ENABLE_US_CURRENCY, DWAPI.StringBoolean.TRUE.value)
    bundle.putString(DWAPI.OCRParams.Decoder.OCR_A_VARIANT, DWAPI.OCRParams.OCRAVariant.FULL_ASCII.string)
    bundle.putString(DWAPI.OCRParams.Decoder.OCR_B_VARIANT, DWAPI.OCRParams.OCRBVariant.ISBN_1.string)
    // Orientation
    bundle.putString(DWAPI.OCRParams.ORIENTATION, DWAPI.OCRParams.OrientationOptions.DEGREE_0.string)
    // Lines
    bundle.putString(DWAPI.OCRParams.LINES, DWAPI.OCRParams.LinesOptions.LINE_1.string)
    // Chars
    bundle.putString(DWAPI.OCRParams.Chars.MIN_CHARS, "3")
    bundle.putString(DWAPI.OCRParams.Chars.MAX_CHARS, "100")
    bundle.putString(DWAPI.OCRParams.Chars.CHAR_SUBSET, DWAPI.OCRParams.Chars.DEFAULT_SUBSET)
    bundle.putString(DWAPI.OCRParams.Chars.QUIET_ZONE, "60")
    // Template
    bundle.putString(DWAPI.OCRParams.TEMPLATE, "99999999")
    // Check Digit
    bundle.putString(DWAPI.OCRParams.CheckDigit.MODULUS, "10")
    bundle.putString(DWAPI.OCRParams.CheckDigit.MULTIPLIER, "121212121212")
    bundle.putString(DWAPI.OCRParams.CheckDigit.VALIDATION, DWAPI.OCRParams.CheckDigit.ValidationOptions.PRODUCT_ADD_LR.string)
    // Inverse
    bundle.putString(DWAPI.OCRParams.INVERSE, DWAPI.OCRParams.InverseOptions.AUTO.string)
    return bundle
}