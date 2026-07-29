package com.zebra.emdk_kotlin_wrapper.utils

import android.media.AudioManager
import android.media.ToneGenerator
import androidx.annotation.Keep

@Keep
object BeepUtils {

    enum class BeepType(val value: Int) {
        // DTMF
        DTMF_0(ToneGenerator.TONE_DTMF_0),
        DTMF_1(ToneGenerator.TONE_DTMF_1),
        DTMF_2(ToneGenerator.TONE_DTMF_2),
        DTMF_3(ToneGenerator.TONE_DTMF_3),
        DTMF_4(ToneGenerator.TONE_DTMF_4),
        DTMF_5(ToneGenerator.TONE_DTMF_5),
        DTMF_6(ToneGenerator.TONE_DTMF_6),
        DTMF_7(ToneGenerator.TONE_DTMF_7),
        DTMF_8(ToneGenerator.TONE_DTMF_8),
        DTMF_9(ToneGenerator.TONE_DTMF_9),

        DTMF_A(ToneGenerator.TONE_DTMF_A),
        DTMF_B(ToneGenerator.TONE_DTMF_B),
        DTMF_C(ToneGenerator.TONE_DTMF_C),
        DTMF_D(ToneGenerator.TONE_DTMF_D),
        DTMF_P(ToneGenerator.TONE_DTMF_P),
        DTMF_S(ToneGenerator.TONE_DTMF_S),
        // PROP
        PROP_ACK(ToneGenerator.TONE_PROP_ACK),
        PROP_NACK(ToneGenerator.TONE_PROP_NACK),
        PROP_BEEP(ToneGenerator.TONE_PROP_BEEP),
        PROP_BEEP2(ToneGenerator.TONE_PROP_BEEP2),
        PROP_PROMPT(ToneGenerator.TONE_PROP_PROMPT),
        // SUP
        SUP_RIP(ToneGenerator.TONE_SUP_PIP),
        SUP_ERROR(ToneGenerator.TONE_SUP_ERROR),
        SUP_BUSY(ToneGenerator.TONE_SUP_BUSY),
        SUP_DIAL(ToneGenerator.TONE_SUP_DIAL),
        SUP_CONFIRM(ToneGenerator.TONE_SUP_CONFIRM),
        SUP_CALL_WAITING(ToneGenerator.TONE_SUP_CALL_WAITING),
        SUP_CALL_CONGESTION(ToneGenerator.TONE_SUP_CONGESTION),
        SUP_CALL_CONGESTION_ABBREV(ToneGenerator.TONE_SUP_CONGESTION_ABBREV),
        SUP_CALL_INTERCEPT(ToneGenerator.TONE_SUP_INTERCEPT),
        SUP_CALL_INTERCEPT_ABBREV(ToneGenerator.TONE_SUP_INTERCEPT_ABBREV),
        SUP_RADIO_ACK(ToneGenerator.TONE_SUP_RADIO_ACK),
        SUP_RADIO_NOTAVAIL(ToneGenerator.TONE_SUP_RADIO_NOTAVAIL),
        SUP_RINGTONE(ToneGenerator.TONE_SUP_RINGTONE),
        // CDMA
        CDMA_PIP(ToneGenerator.TONE_CDMA_PIP),
        CDMA_ANSWER(ToneGenerator.TONE_CDMA_ANSWER),
        CDMA_CONFIRM(ToneGenerator.TONE_CDMA_CONFIRM),
        CDMA_REORDER(ToneGenerator.TONE_CDMA_REORDER),
        CDMA_INTERCEPT(ToneGenerator.TONE_CDMA_INTERCEPT),
        // CDMA ABBR
        CDMA_ABBR_ALERT(ToneGenerator.TONE_CDMA_ABBR_ALERT),
        CDMA_ABBR_INTERCEPT(ToneGenerator.TONE_CDMA_ABBR_INTERCEPT),
        CDMA_ABBR_REORDER(ToneGenerator.TONE_CDMA_ABBR_REORDER),
        // CDMA ALERT
        CDMA_ALERT_CALL_GUARD(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD),
        CDMA_ALERT_INCALL_LITE(ToneGenerator.TONE_CDMA_ALERT_INCALL_LITE),
        CDMA_ALERT_NETWORK_LITE(ToneGenerator.TONE_CDMA_ALERT_NETWORK_LITE),
        CDMA_ALERT_AUTOREDIAL_LITE(ToneGenerator.TONE_CDMA_ALERT_AUTOREDIAL_LITE),
        // CDMA CALL SIGNAL ISDN
        CDMA_CALL_SIGNAL_ISDN_INTERGROUP(ToneGenerator.TONE_CDMA_CALL_SIGNAL_ISDN_INTERGROUP),
        CDMA_CALL_SIGNAL_ISDN_NORMAL(ToneGenerator.TONE_CDMA_CALL_SIGNAL_ISDN_NORMAL),
        CDMA_CALL_SIGNAL_ISDN_SP_PRI(ToneGenerator.TONE_CDMA_CALL_SIGNAL_ISDN_SP_PRI),
        CDMA_CALL_SIGNAL_ISDN_PING_RING(ToneGenerator.TONE_CDMA_CALL_SIGNAL_ISDN_PING_RING),
        CDMA_CALL_SIGNAL_ISDN_PAT3(ToneGenerator.TONE_CDMA_CALL_SIGNAL_ISDN_PAT3),
        CDMA_CALL_SIGNAL_ISDN_PAT5(ToneGenerator.TONE_CDMA_CALL_SIGNAL_ISDN_PAT5),
        CDMA_CALL_SIGNAL_ISDN_PAT6(ToneGenerator.TONE_CDMA_CALL_SIGNAL_ISDN_PAT6),
        CDMA_CALL_SIGNAL_ISDN_PAT7(ToneGenerator.TONE_CDMA_CALL_SIGNAL_ISDN_PAT7),
        CDMA_CALLDROP_LITE(ToneGenerator.TONE_CDMA_CALLDROP_LITE),
        CDMA_DIAL_TONE_LITE(ToneGenerator.TONE_CDMA_DIAL_TONE_LITE),
        CDMA_EMERGENCY_RINGBACK(ToneGenerator.TONE_CDMA_EMERGENCY_RINGBACK),
        CDMA_KEYPAD_VOLUME_KEY_LITE(ToneGenerator.TONE_CDMA_KEYPAD_VOLUME_KEY_LITE),
        // CDMA HIGH
        CDMA_HIGH_L(ToneGenerator.TONE_CDMA_HIGH_L),
        CDMA_HIGH_S_X4(ToneGenerator.TONE_CDMA_HIGH_S_X4),
        CDMA_HIGH_SS(ToneGenerator.TONE_CDMA_HIGH_SS),
        CDMA_HIGH_SLS(ToneGenerator.TONE_CDMA_HIGH_SLS),
        CDMA_HIGH_SSL(ToneGenerator.TONE_CDMA_HIGH_SSL),
        CDMA_HIGH_SS_2(ToneGenerator.TONE_CDMA_HIGH_SS_2),
        // CDMA LOW
        CDMA_LOW_L(ToneGenerator.TONE_CDMA_LOW_L),
        CDMA_LOW_SS(ToneGenerator.TONE_CDMA_LOW_SS),
        CDMA_LOW_SLS(ToneGenerator.TONE_CDMA_LOW_SLS),
        CDMA_LOW_SSL(ToneGenerator.TONE_CDMA_LOW_SSL),
        CDMA_LOW_SS_2(ToneGenerator.TONE_CDMA_LOW_SS_2),
        CDMA_LOW_S_X4(ToneGenerator.TONE_CDMA_LOW_S_X4),
        // CDMA MED
        CDMA_MED_L(ToneGenerator.TONE_CDMA_MED_L),
        CDMA_MED_SS(ToneGenerator.TONE_CDMA_MED_SS),
        CDMA_MED_SLS(ToneGenerator.TONE_CDMA_MED_SLS),
        CDMA_MED_SSL(ToneGenerator.TONE_CDMA_MED_SSL),
        CDMA_MED_SS_2(ToneGenerator.TONE_CDMA_MED_SS_2),
        // CDMA MED PBX
        CDMA_MED_PBX_L(ToneGenerator.TONE_CDMA_MED_PBX_L),
        CDMA_MED_PBX_SS(ToneGenerator.TONE_CDMA_MED_PBX_SS),
        CDMA_MED_PBX_SLS(ToneGenerator.TONE_CDMA_MED_PBX_SLS),
        CDMA_MED_PBX_SSL(ToneGenerator.TONE_CDMA_MED_PBX_SSL),
        CDMA_MED_PBX_S_X4(ToneGenerator.TONE_CDMA_MED_PBX_S_X4),
        // CDMA NETWORK
        CDMA_NETWORK_BUSY(ToneGenerator.TONE_CDMA_NETWORK_BUSY),
        CDMA_NETWORK_CALLWAITING(ToneGenerator.TONE_CDMA_NETWORK_CALLWAITING),
        CDMA_NETWORK_USA_RINGBACK(ToneGenerator.TONE_CDMA_NETWORK_USA_RINGBACK),
        CDMA_NETWORK_BUSY_ONE_SHOT(ToneGenerator.TONE_CDMA_NETWORK_BUSY_ONE_SHOT)
    }

    private var toneGenerator: ToneGenerator? = null

    init {
        setupBeep()
    }

    @Keep
    private fun setupBeep() {
        // 1. Initialize ToneGenerator once (Stream type and Volume 0-100)
        // STREAM_MUSIC is usually preferred for industrial scanning apps
        // to ensure it honors the media volume slider.
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100)
        } catch (e: RuntimeException) {
            e.printStackTrace()
        }
    }

    @Keep
    fun playBeep(type: BeepType = BeepType.SUP_ERROR) {
        // 2. Start the tone
        // TONE_PROP_BEEP: Standard short beep
        // 150ms: Common duration for scan feedback
        toneGenerator?.startTone(type.value, 150)
    }
}