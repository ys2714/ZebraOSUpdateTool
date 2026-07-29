package com.zebra.emdk_kotlin_wrapper.dw

import android.content.Context
import android.os.Bundle
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * https://techdocs.zebra.com/datawedge/latest/guide/api/setconfig/
 *
 * - Result Codes:
 * PLUGIN_NOT_SUPPORTED - An attempt was made to configure a plug-in that is not supported by DataWedge intent APIs
 * BUNDLE_EMPTY - The bundle contains no data
 * PROFILE_NAME_EMPTY - An attempt was made to configure a Profile name with no data
 * PROFILE_NOT_FOUND - An attempt was made to perform an operation on a Profile that does not exist
 * PLUGIN_BUNDLE_INVALID - A passed plug-in parameter bundle is empty or contains insufficient information
 * PARAMETER_INVALID - The passed parameters were empty, null or invalid
 * APP_ALREADY_ASSOCIATED - An attempt was made to associate an app that was already associated with another Profile
 * OPERATION_NOT_ALLOWED - An attempt was made to rename or delete a protected Profile or to associate an app with Profile0
 * RESULT_ACTION_RESULT_CODE_EMPTY_RULE_NAME - Rule name empty or undefined in ADF_RULE bundle
 * UNLICENSED_FEATURE - An attempt was made to call SetConfig or Switch Scanner Params API to change the scanning mode to MultiBarcode or NextGen SimulScan on an unlicensed Zebra Professional-series device.
 * */
internal suspend fun DWAPI.sendSetConfigIntent(context: Context, extra: Bundle): Boolean = suspendCancellableCoroutine { continuation ->
    DWIntentFactory.callDWAPI(context, DWAPI.ActionExtraKeys.SET_CONFIG, extra) { result ->
        result.onSuccess {
            continuation.resumeWith(Result.success(true))
        }.onFailure {
            continuation.resumeWith(Result.failure(it))
        }
    }
}

/**
 * https://techdocs.zebra.com/datawedge/15-0/guide/api/getconfig/
 *
 * Parameters
 * ACTION [string]: "com.symbol.datawedge.api.ACTION"
 * EXTRA_DATA [string]: "com.symbol.datawedge.api.GET_CONFIG"
 * EXTRA VALUE [bundle]: "<PROFILE_NAME>", "<PLUGIN_CONFIG>"
 *
 * PLUGIN_CONFIG [bundle] -
 * PLUGIN_NAME [string] - single plug-in name (i.e. "barcode") or ArrayList of plugin names:
 * PROCESS_PLUGIN_NAME [list of bundles] - For example:
 * PLUGIN_NAME - "ADF", "BDF"
 * OUTPUT_PLUGIN_NAME - "KEYSTROKE"
 * ...
 * PLUGIN_NAME - "BDF"
 * OUTPUT_PLUGIN_NAME - "INTENT"
 *
 * Return Values
 * Returns a nested bundle with the Profile name, status and a Profile config bundle containing the PARAM_LIST bundle.
 * EXTRA NAME: "com.symbol.datawedge.api.GET_CONFIG_RESULT"
 * BUNDLE: <mainbundle> (see parameters below)
 *
 * */
suspend fun DWAPI.sendGetConfigIntent(context: Context, extra: Bundle): Bundle = suspendCancellableCoroutine { continuation ->
    DWIntentFactory.callDWAPI(context, DWAPI.ActionExtraKeys.GET_CONFIG, extra) { result ->
        result.onSuccess {
            it.getBundleExtra(DWAPI.ResultExtraKeys.GET_CONFIG.value)?.let { bundle ->
                continuation.resumeWith(Result.success(bundle))
            } ?: run {
                continuation.resumeWith(Result.failure(Exception("No Result")))
            }
        }.onFailure {
            continuation.resumeWith(Result.failure(it))
        }
    }
}

/**
 * https://techdocs.zebra.com/datawedge/latest/guide/api/createprofile/
 *
 * - Parameters:
 * ACTION [String]: com.symbol.datawedge.api.ACTION
 * EXTRA_DATA [String]: com.symbol.datawedge.api.CREATE_PROFILE
 * String <value>: Name of Profile to be created
 *
 * - Result Codes:
 * PROFILE_ALREADY_EXIST - FAILURE
 * PROFILE_NAME_EMPTY - FAILURE
 * */
suspend fun DWAPI.sendCreateProfileIntent(context: Context, name: String): Boolean = suspendCancellableCoroutine { continuation ->
    DWIntentFactory.callDWAPI(context, DWAPI.ActionExtraKeys.CREATE_PROFILE,name) { result ->
        result.onSuccess {
            continuation.resumeWith(Result.success(true))
        }.onFailure {
            continuation.resumeWith(Result.failure(it))
        }
    }
}

suspend fun DWAPI.sendDeleteProfileIntent(context: Context, name: String): Boolean = suspendCancellableCoroutine { continuation ->
    DWIntentFactory.callDWAPI(context, DWAPI.ActionExtraKeys.DELETE_PROFILE, name) { result ->
        result.onSuccess {
            continuation.resumeWith(Result.success(true))
        }.onFailure {
            if (it.message == DWAPI.ResultCodes.PROFILE_NOT_FOUND.value) {
                continuation.resumeWith(Result.success(true))
            } else {
                continuation.resumeWith(Result.failure(it))
            }
        }
    }
}

/**
 * https://techdocs.zebra.com/datawedge/latest/guide/api/switchtoprofile/
 *
 * - Parameters:
 * ACTION [String]: "com.symbol.datawedge.api.ACTION"
 * EXTRA_DATA [String]: "com.symbol.datawedge.api.SWITCH_TO_PROFILE"
 * <profile name>: The Profile name (a case-sensitive string) to set as the active Profile.
 *
 * - Result Codes:
 * DataWedge returns the following error codes if the app includes the intent extras SEND_RESULT and COMMAND_IDENTIFIER to enable the app to get results using the DataWedge result intent mechanism. See Example, below.
 *
 * PROFILE_HAS_APP_ASSOCIATION - FAILURE
 * PROFILE_NOT_FOUND - FAILURE
 * PROFILE_ALREADY_SET - FAILURE
 * PROFILE_NAME_EMPTY - FAILURE
 * DATAWEDGE_DISABLED - FAILURE
 *
 * */
suspend fun DWAPI.sendSwitchProfileIntent(context: Context, name: String): Boolean = suspendCancellableCoroutine { continuation ->
    DWIntentFactory.callDWAPI(context, DWAPI.ActionExtraKeys.SWITCH_TO_PROFILE, name) { result ->
        result.onSuccess {
            continuation.resumeWith(Result.success(true))
        }.onFailure {
            if (it.message == DWAPI.ResultCodes.PROFILE_ALREADY_SET.value) {
                continuation.resumeWith(Result.success(true))
            } else {
                continuation.resumeWith(Result.failure(it))
            }
        }
    }
}

/**
 * https://techdocs.zebra.com/datawedge/latest/guide/api/switchscannerparams/
 *
 * - Parameters:
 * ACTION [String]: "com.symbol.datawedge.api.ACTION"
 * EXTRA_DATA [String]: "com.symbol.datawedge.api.SWITCH_SCANNER_PARAMS"
 * EXTRA_DATA [bundle]: "<name, value>" - Accepts scanner parameters as name-value pairs
 *
 * - Result Codes:
 * DATAWEDGE_DISABLED - DataWedge is disabled
 * PROFILE_DISABLED - Profile is disabled
 * PLUGIN_DISABLED - Scanner plug-in is disabled
 * SCANNER_DISABLED - Scanner is disabled
 * PARAMETER_INVALID - Given scanner parameter is invalid
 * PARAMETER_NOT_SUPPORTED - Given scanner parameter is not supported
 * VALUE_INVALID - Given value for a scanner parameter is invalid
 * VALUE_NOT_SUPPORTED - Given value for a scanner parameter is not supported
 *
 */
suspend fun DWAPI.sendSwitchScannerParamsIntent(context: Context, bundle: Bundle): Boolean = suspendCancellableCoroutine { continuation ->
    DWIntentFactory.callDWAPI(context, DWAPI.ActionExtraKeys.SWITCH_SCANNER_PARAMS, bundle) { result ->
        result.onSuccess {
            continuation.resumeWith(Result.success(true))
        }.onFailure {
            continuation.resumeWith(Result.failure(it))
        }
    }
}