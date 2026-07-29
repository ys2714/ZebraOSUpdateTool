package com.zebra.emdk_kotlin_wrapper.dw

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.core.content.ContextCompat
import com.zebra.emdk_kotlin_wrapper.utils.JsonUtils

internal object DWIntentFactory {

    const val TAG = "DWIntentFactory"

    fun callDWAPI(context: Context, extraKey: DWAPI.ActionExtraKeys, extraValue: Any, callback: (Result<Intent>) -> Unit) {
        fun completion(ctx: Context, receiver: BroadcastReceiver, result: Result<Intent>) {
            callback(result)
            ctx.unregisterReceiver(receiver)
        }
        val ctx = context.applicationContext
        val commandIdentifier = "${extraKey.value}_${(0..99999999).random()}"
        // dynamically register a broadcast receiver to receive the result. unregister it after the result is received.
        ContextCompat.registerReceiver(
            ctx,
            object : BroadcastReceiver() {
                override fun onReceive(
                    context: Context?,
                    intent: Intent?
                ) {
                    intent?.also {
                        // command identifier optional
                        val receivedCommandIdentifier = it.getStringExtra(DWConst.COMMAND_IDENTIFIER)
                        if (receivedCommandIdentifier != null && receivedCommandIdentifier != commandIdentifier) {
                            // please don't call completion() here, because this maybe caused by concurrently calling same API.
                            Log.w(TAG, "SKIP. intent command didn't match $commandIdentifier")
                            return
                        }
                        if (it.action != DWAPI.ResultActionNames.RESULT_ACTION.value) {
                            Log.e(TAG, "intent action is not ${DWAPI.ResultActionNames.RESULT_ACTION.value}")
                            completion(ctx, this, Result.failure(Exception(DWAPI.ResultCodes.ERROR_INTENT_ACTION_NOT_MATCH.value)))
                            return
                        }
                        // result info optional
                        val infoBundle = it.getBundleExtra(DWAPI.Result.RESULT_INFO)
                        if (infoBundle != null) {
                            val code = infoBundle.getString(DWAPI.Result.RESULT_CODE)
                            if (code == null) {
                                completion(ctx, this, Result.success(it))
                            } else {
                                if (code == DWAPI.ResultCodes.PLUGIN_BUNDLE_INVALID.value ||
                                    code == DWAPI.ResultCodes.PLUGIN_NOT_SUPPORTED.value ||
                                    code == DWAPI.ResultCodes.PLUGIN_DISABLED_IN_CONFIG.value||
                                    code == DWAPI.ResultCodes.PARAMETER_INVALID.value
                                    ) {
                                    val bundle = extraValue as Bundle
                                    val dump = JsonUtils.bundleToJson(bundle)
                                    completion(ctx, this, Result.failure(Exception("$code $receivedCommandIdentifier ${dump}")))
                                }
                                else {
                                    completion(ctx, this, Result.failure(Exception(code)))
                                }
                            }
                            return
                        } else {
                            Log.d(TAG, "no RESULT_INFO in intent")
                        }
                        // passed all check
                        completion(ctx, this, Result.success(it))
                    } ?: run {
                        Log.e(TAG, "intent is null")
                        completion(ctx, this, Result.failure(Exception(DWAPI.ResultCodes.ERROR_INTENT_NULL.value)))
                    }
                }
            },
            IntentFilter().apply {
                // please do not set category to the DW API calling intent !!!
                // DW API result intent come with category "android.intent.category.DEFAULT"
                addAction(DWAPI.ResultActionNames.RESULT_ACTION.value)
                addCategory(DWAPI.ResultCategoryNames.CATEGORY_DEFAULT.value)
            },
            ContextCompat.RECEIVER_EXPORTED
        )
        ctx.sendOrderedBroadcast(
            Intent().apply {
                // please do not set category to the DW API calling intent !!!
                // DW API result intent come with category "android.intent.category.DEFAULT"
                setAction(DWAPI.ActionNames.ACTION.value)
                when (extraValue) {
                    is String -> {
                        putExtra(extraKey.value, extraValue as String)
                    }
                    is Boolean -> {
                        putExtra(extraKey.value, extraValue as Boolean)
                    }
                    is Int -> {
                        putExtra(extraKey.value, extraValue as Int)
                    }
                    is Bundle -> {
                        putExtra(extraKey.value, extraValue as Bundle)
                    }
                    is Parcelable -> {
                        putExtra(extraKey.value, extraValue as Parcelable)
                    }
                    is Array<*> -> {
                        if (extraValue.all { it is Parcelable })
                            putExtra(extraKey.value, extraValue as Array<Parcelable>)
                    }
                }
                putExtra(DWConst.SEND_RESULT, DWAPI.SendResultOptions.LAST_RESULT.value)
                putExtra(DWConst.COMMAND_IDENTIFIER, commandIdentifier)
            },
            null
        )
    }

}
