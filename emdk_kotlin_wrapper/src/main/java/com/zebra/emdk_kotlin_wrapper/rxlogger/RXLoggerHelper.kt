package com.zebra.emdk_kotlin_wrapper.rxlogger

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.annotation.Keep
import androidx.core.content.ContextCompat

/**
 * https://techdocs.zebra.com/rxlogger/latest/guide/apis/
 *
 * */
@Keep
object RXLoggerHelper {

    val ACTION_ENABLE = "com.symbol.rxlogger.intent.action.ENABLE"
    val ACTION_DISABLE = "com.symbol.rxlogger.intent.action.DISABLE"
    val ACTION_BACKUP_NOW = "com.symbol.rxlogger.intent.action.BACKUP_NOW"

    // This intent informs the third-party app that RxLogger has begun capturing logs. RxLogger sends this acknowledgment intent once it enters the running state.
    val ACTION_ENABLE_STATUS = "com.symbol.rxlogger.intent.action.ENABLE_STATUS"
    val ACTION_DISABLE_STATUS = "com.symbol.rxlogger.intent.action.DISABLE_STATUS"

    // This provides the status of the backup operation. RxLogger sends an acknowledgment intent to convey the backup status, including extras such as status, message and the absolute path of the backup zip file.
    val ACTION_BACKUP_NOW_STATUS = "com.symbol.rxlogger.intent.action.BACKUP_NOW_STATUS"

    /**
     * This initiates data collection for all enabled modules, equivalent to tapping the Start button.
     * */
    @Keep
    fun startRXLogger(context: Context) {
        context.sendOrderedBroadcast(
            Intent().apply {
                action = ACTION_ENABLE
            },
            null
        )
    }

    /**
     * This stops data collection for all modules; the equivalent of tapping the Stop button.
     * */
    @Keep
    fun stopRXLogger(context: Context) {
        context.sendOrderedBroadcast(
            Intent().apply {
                action = ACTION_DISABLE
            },
            null
        )
    }

    /**
     * This initiates a backup of log files in the RxLogger folder,
     * compressing them into a zip file named Backup-<date>-<HHMMSS>.zip,
     * where <date>-<HHMMSS> is time stamp.
     */
    @Keep
    fun dumpRXLogger(context: Context) {
        context.sendOrderedBroadcast(
            Intent().apply {
                action = ACTION_BACKUP_NOW
            },
            null
        )
    }

    /**
     * return value in callback:
     * - true: RXLogger is running
     * - false: RXLogger is stopped
     * */
    @Keep
    fun waitingRXLoggerRunningStatusOneShot(context: Context, completion: (Boolean) -> Unit) {
        ContextCompat.registerReceiver(
            context,
            object : BroadcastReceiver() {
                override fun onReceive(
                    context: Context?,
                    intent: Intent?
                ) {
                    if (intent != null) {
                        if (intent.action == ACTION_ENABLE_STATUS) {
                            completion(true)
                        }
                        if (intent.action == ACTION_DISABLE_STATUS) {
                            completion(false)
                        }
                    }
                    context?.unregisterReceiver(this)
                }
            },
            IntentFilter().apply {
                addAction(ACTION_ENABLE_STATUS)
                addAction(ACTION_DISABLE_STATUS)
            },
            ContextCompat.RECEIVER_EXPORTED
        )
    }

    @Keep
    fun waitingRXLoggerDumpResultOneShot(context: Context, completion: (String, String, String?) -> Unit) {
        ContextCompat.registerReceiver(
            context,
            object : BroadcastReceiver() {
                override fun onReceive(
                    context: Context?,
                    intent: Intent?
                ) {
                    if (intent != null) {
                        if (intent.action == ACTION_BACKUP_NOW_STATUS) {
                            val status = intent.getStringExtra("status")
                            val message = intent.getStringExtra("message")
                            val filepath = intent.getStringExtra("filepath")
                            completion(status ?: "", message ?: "", filepath)
                        }
                    }
                    context?.unregisterReceiver(this)
                }
            },
            IntentFilter().apply {
                addAction(ACTION_BACKUP_NOW_STATUS)
            },
            ContextCompat.RECEIVER_EXPORTED
        )
    }
}