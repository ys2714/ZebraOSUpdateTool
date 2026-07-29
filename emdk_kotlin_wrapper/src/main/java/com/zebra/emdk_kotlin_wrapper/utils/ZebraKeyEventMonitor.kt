package com.zebra.emdk_kotlin_wrapper.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.annotation.Keep
import androidx.core.content.ContextCompat
import com.zebra.emdk_kotlin_wrapper.mx.MXBase
import com.zebra.emdk_kotlin_wrapper.mx.MXConst
import com.zebra.emdk_kotlin_wrapper.mx.MXProfileProcessor
import com.zebra.emdk_kotlin_wrapper.mx.remappingAllKeyToDefault
import com.zebra.emdk_kotlin_wrapper.mx.remappingKeyToSendIntent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

@Keep
object ZebraKeyEventMonitor {

    const val TAG = "ZebraKeyEventMonitor"

    const val KEY_DOWN_ACTION = "com.zebra.emdk_kotlin_wrapper.KEY_DOWN_ACTION"
    const val KEY_DOWN_CATEGORY = "android.intent.category.DEFAULT"

    internal val listeners = FixedSizeQueue<KeyDownListener>(100)

    internal var foregroundScope = CoroutineScope(Dispatchers.Main + Job())
    internal var backgroundScope = CoroutineScope(Dispatchers.IO + Job())

    @Keep
    class KeyDownListener(
        val context: Context,
        val keyIdentifier: MXBase.KeyIdentifiers,
        val keyDownCallback: () -> Unit) : BroadcastReceiver(), FixedSizeQueueItem {

        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent == null) {
                throw RuntimeException("intent is null")
            }
            if (intent.hasExtra(MXConst.EXTRA_KEY_IDENTIFIER)) {
                val identifier = intent.getStringExtra(MXConst.EXTRA_KEY_IDENTIFIER)
                if (identifier == keyIdentifier.string) {
                    keyDownCallback()
                }
            }
        }

        override fun getID(): String {
            return keyIdentifier.string
        }

        override fun onDisposal() {
            context.unregisterReceiver(this)
        }
    }

    @Keep
    fun resetAllKeyDownToDefault(context: Context, delaySeconds: Long = 0, completion: () -> Unit) {
        MXProfileProcessor.remappingAllKeyToDefault(context, delaySeconds) { errorInfo ->
            if (errorInfo == null) {
                completion()
            } else {
                throw errorInfo
            }
        }
    }

    @Keep
    fun registerKeyDownListener(
        context: Context,
        key: MXBase.KeyIdentifiers,
        delaySeconds: Long = 0,
        keyDownCallback: () -> Unit) {
        MXProfileProcessor.remappingKeyToSendIntent(
            context,
            key,
            KEY_DOWN_ACTION,
            KEY_DOWN_CATEGORY,
            delaySeconds
        ) { error ->
            if (error != null) {
                Log.e(TAG, "remap key ${key} to send intent failed")
                throw RuntimeException("remap key ${key} to send intent failed")
            } else {
                ContextCompat.registerReceiver(context.applicationContext,
                    KeyDownListener(
                        context.applicationContext,
                        key,
                        keyDownCallback).apply {
                        listeners.enqueue(this)
                    }, IntentFilter().apply {
                        addAction(KEY_DOWN_ACTION)
                        addCategory(KEY_DOWN_CATEGORY)
                    }, ContextCompat.RECEIVER_NOT_EXPORTED
                )
            }
        }
    }
}