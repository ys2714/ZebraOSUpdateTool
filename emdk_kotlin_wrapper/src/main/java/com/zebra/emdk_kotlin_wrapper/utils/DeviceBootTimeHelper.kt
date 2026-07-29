package com.zebra.zsdk_java_wrapper.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import kotlin.concurrent.thread

class DeviceBootTimeHelper private constructor() {

    private var bootCompleted = false
    private var oemInfoUpdated = false

    val isBootCompleted: Boolean
        get() = if (bootCompleted) true else isUptimeExceedNormalBootTime(0)

    val isOEMInfoUpdated: Boolean
        get() = if (oemInfoUpdated) true else isUptimeExceedNormalBootTime(OEMINFO_UPDATE_TIME_SINCE_BOOT_COMPLETED_MILLI)

    fun waitBootCompletedOneShot(callback: Runnable) {
        bootCompletedCallback = callback
    }

    fun waitOEMInfoUpdateCompletedOneShot(callback: Runnable) {
        oemInfoUpdatedCallback = callback
    }

    private fun isUptimeExceedNormalBootTime(delta: Long): Boolean {
        val uptime = SystemClock.uptimeMillis()
        // The Java logic used 60 seconds for all cases; keeping logic consistent
        return when (Build.DEVICE) {
            "TC26", "TC27" -> uptime > 60 * 1000 + delta
            else -> uptime > 60 * 1000 + delta
        }
    }

    class BootCompletedReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
                val helper = shared
                helper.bootCompleted = true
                
                bootCompletedCallback?.run()
                bootCompletedCallback = null

                thread {
                    try {
                        Thread.sleep(OEMINFO_UPDATE_TIME_SINCE_BOOT_COMPLETED_MILLI)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    
                    helper.oemInfoUpdated = true
                    mainThreadHandler.post {
                        oemInfoUpdatedCallback?.run()
                    }
                }
            }
        }
    }

    companion object {
        private const val OEMINFO_UPDATE_TIME_SINCE_BOOT_COMPLETED_MILLI: Long = 30 * 1000
        
        private var bootCompletedCallback: Runnable? = null
        private var oemInfoUpdatedCallback: Runnable? = null
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        @JvmStatic
        val shared: DeviceBootTimeHelper by lazy { DeviceBootTimeHelper() }
    }
}