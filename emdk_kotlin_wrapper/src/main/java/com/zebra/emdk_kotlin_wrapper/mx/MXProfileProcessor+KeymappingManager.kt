package com.zebra.emdk_kotlin_wrapper.mx

import android.content.Context
import android.util.Log

/**
 * https://techdocs.zebra.com/emdk-for-android/14-0/mx/keymappingmgr/
 *
 * Send Intent
 * Used to enter attributes of an Intent to send as the Key Behavior for the specified Key in various Key State Mapping Tables.
 *
 * Parm names:
 * For Base Key State Table: BaseSendIntent
 * For Blue Key State Table: BlueSendIntent
 * For Orange Key State Table: OrangeSendIntent
 * For Grey Key State Table: GreySendIntent
 * For Shift Key State Table: ShiftSendIntent
 * For Control Key State Table: ControlSendIntent
 *
 * */

internal fun MXProfileProcessor.remappingKeyToSendIntent(
    context: Context,
    keyIdentifier: MXBase.KeyIdentifiers,
    intentAction: String,
    intentCategory: String,
    delaySeconds: Long,
    callback: (MXBase.ErrorInfo?) -> Unit) {
    MXProfileProcessor.processProfileWithCallback(
        context,
        MXBase.ProfileXML.KeymappingManagerSetKeySendIntent,
        MXBase.ProfileName.KeymappingManagerSetKeySendIntent,
        mapOf(
            MXConst.KeyIdentifier to keyIdentifier.string,
            MXConst.BaseIntentAction to intentAction,
            MXConst.BaseIntentCategory to intentCategory,
            MXConst.BaseIntentStringExtraName to MXConst.EXTRA_KEY_IDENTIFIER,
            MXConst.BaseIntentStringExtraValue to keyIdentifier.string
        ),
        delaySeconds,
        callback
    )
}

internal fun MXProfileProcessor.remappingAllKeyToDefault(
    context: Context,
    delaySeconds: Long = 0,
    callback: (MXBase.ErrorInfo?) -> Unit) {
    MXProfileProcessor.processProfileWithCallback(
        context,
        MXBase.ProfileXML.KeymappingManagerSetAllToDefault,
        MXBase.ProfileName.KeymappingManagerSetAllToDefault,
        null,
        delaySeconds,
        callback
    )
}
