package com.zebra.emdk_kotlin_wrapper.dw

import android.content.Context
import android.util.Log
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Enables or disables the DataWedge service.
 *
 * See [DataWedge API documentation](https://techdocs.zebra.com/datawedge/latest/guide/api/enabledatawedge/) for more details.
 *
 * The function sends an intent with the following properties:
 * - **ACTION**: `com.symbol.datawedge.api.ACTION`
 * - **EXTRA_DATA**: `com.symbol.datawedge.api.ENABLE_DATAWEDGE`
 * - **Value**: `true` or `false`
 *
 * https://techdocs.zebra.com/datawedge/latest/guide/api/enabledatawedge/
 * @param context The application context.
 * @param enabled `true` to enable DataWedge, `false` to disable it.
 * @return `true` if the operation was successful (including cases where DataWedge was already in the desired state), `false` on failure.
 * @throws Exception with a message from [DWAPI.ResultCodes] on failure. Note that `DATAWEDGE_ALREADY_ENABLED` and `DATAWEDGE_ALREADY_DISABLED` are treated as success conditions.
 */
internal suspend fun DWAPI.enableDW(context: Context, enabled: Boolean): Boolean = suspendCancellableCoroutine { continuation ->
    DWIntentFactory.callDWAPI(context, DWAPI.ActionExtraKeys.ENABLE_DATAWEDGE, enabled) { result ->
        result.onSuccess {
            continuation.resumeWith(Result.success(true))
        }.onFailure {
            if (enabled && it.message == DWAPI.ResultCodes.DATAWEDGE_ALREADY_ENABLED.value) {
                continuation.resumeWith(Result.success(true))
            }
            else if (!enabled && it.message == DWAPI.ResultCodes.DATAWEDGE_ALREADY_DISABLED.value) {
                continuation.resumeWith(Result.success(true))
            }
            else {
                continuation.resumeWith(Result.failure(it))
            }
        }
    }
}

/**
 * https://techdocs.zebra.com/datawedge/latest/guide/api/getdatawedgestatus/
 *
 * - Parameters:
 * ACTION [String]: "com.symbol.datawedge.api.ACTION"
 * EXTRA_DATA [String]: "com.symbol.datawedge.api.GET_DATAWEDGE_STATUS"
 *
 * - Result Codes:
 * EXTRA VALUE: Empty string
 * Returns the status of DataWedge as "enabled" or "disabled" as a string extra.
 * EXTRA NAME: "com.symbol.datawedge.api.RESULT_GET_DATAWEDGE_STATUS"
 * EXTRA TYPE: Bundle
 * */
internal suspend fun DWAPI.sendGetDWStatusIntent(context: Context): Boolean = suspendCancellableCoroutine { continuation ->
    DWIntentFactory.callDWAPI(context, DWAPI.ActionExtraKeys.GET_DATAWEDGE_STATUS, "") { result ->
        result.onSuccess {
            if (it.action == DWAPI.ResultActionNames.RESULT_ACTION.value &&
                it.hasExtra(DWAPI.ResultExtraKeys.GET_DATAWEDGE_STATUS.value))
            {
                val result = it.getStringExtra(DWAPI.ResultExtraKeys.GET_DATAWEDGE_STATUS.value) as String
                val enabled = result.equals(DWAPI.StringEnabled.ENABLED.value, ignoreCase = true)
                Log.d(DataWedgeHelper.TAG, "CHECK DW STATUS PROFILE RESULT: $enabled")
                continuation.resumeWith(Result.success(enabled))
            } else {
                Log.e(DataWedgeHelper.TAG, "CHECK DW STATUS PROFILE FAILED: no dw status in intent")
                continuation.resumeWith(Result.failure(Exception(DWAPI.ResultCodes.ERROR_NO_DATAWEDGE_STATUS_IN_RESULT.value)))
            }
        }.onFailure {
            continuation.resumeWith(Result.failure(it))
        }
    }
}