package com.zebra.emdk_kotlin_wrapper.dw

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log

/**
 * https://techdocs.zebra.com/datawedge/latest/guide/api/softscantrigger/
 *
 * DATAWEDGE_DISABLED - FAILURE
 * INPUT_NOT_ENABLED - FAILURE
 * PARAMETER_INVALID - FAILURE
 * PROFILE_DISABLED - FAILURE
 */
internal fun DWAPI.softScanTrigger(context: Context, option: DWAPI.SoftScanTriggerOptions) {
    DWIntentFactory.callDWAPI(context,DWAPI.ActionExtraKeys.SOFT_SCAN_TRIGGER, option.value) { result ->
        result.onSuccess {
            Log.d(TAG, "softScanTrigger success")
        }.onFailure {
            Log.e(TAG, "softScanTrigger failed: ${it.message}")
        }
    }
}

internal fun DWAPI.sendSetDCPButtonIntent(context: Context, name: String, enabled: Boolean) {
    // DCP
    val dcpParams = Bundle()
    if (enabled) {
        dcpParams.putString(DWAPI.DCPParams.ENABLED, DWAPI.StringBoolean.TRUE.value)
    } else {
        dcpParams.putString(DWAPI.DCPParams.ENABLED, DWAPI.StringBoolean.FALSE.value)
    }
    dcpParams.putString(DWAPI.DCPParams.DOCK, DWAPI.DCPParams.DockOptions.BOTH)
    dcpParams.putString(DWAPI.DCPParams.MODE, DWAPI.DCPParams.ModeOptions.BUTTON)
    dcpParams.putString(DWAPI.DCPParams.HIGH_POS, "30")
    dcpParams.putString(DWAPI.DCPParams.LOW_POS, "70")
    dcpParams.putString(DWAPI.DCPParams.DRAG, "501")
    val dcpPluginBundle = Bundle()
    dcpPluginBundle.putString(DWAPI.Plugin.NAME, DWAPI.Plugin.Utilities.DCP.value)
    dcpPluginBundle.putString(DWAPI.BundleParams.RESET_CONFIG, DWAPI.StringBoolean.TRUE.value)
    dcpPluginBundle.putBundle(DWAPI.BundleParams.PARAM_LIST, dcpParams)
    // Main Config Bundle
    val bundle = Bundle()
    bundle.putString(DWAPI.Profile.NAME, name)
    bundle.putString(DWAPI.Profile.CONFIG_MODE, DWAPI.ConfigModeOptions.UPDATE.value)
    bundle.putString(DWAPI.Profile.ENABLED, DWAPI.StringBoolean.TRUE.value)
    if (enabled) {
        bundle.putBundle(DWAPI.Plugin.Utilities.DCP.value, dcpPluginBundle)
    } else {
        bundle.putBundle(DWAPI.Plugin.Utilities.DCP.value, null)
    }
    // Send Broadcast
    val intent = Intent(DWAPI.ActionNames.ACTION.value)
    intent.putExtra(DWConst.SET_CONFIG, bundle)
    context.sendOrderedBroadcast(intent, null)
}

internal fun DWAPI.setDCPButton(context: Context, name: String, enabled: Boolean) {
    sendSetDCPButtonIntent(context, name, enabled)
}