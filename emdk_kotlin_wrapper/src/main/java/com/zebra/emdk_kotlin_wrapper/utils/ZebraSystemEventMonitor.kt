package com.zebra.emdk_kotlin_wrapper.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.annotation.Keep
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner

@Keep
object ZebraSystemEventMonitor: LifecycleEventObserver {

    private var appCreateCallback: (() -> Unit)? = null
    private var appStartCallback: (() -> Unit)? = null
    private var appPauseCallback: (() -> Unit)? = null
    private var appResumeCallback: (() -> Unit)? = null
    private var appStopCallback: (() -> Unit)? = null
    private var appDestroyCallback: (() -> Unit)? = null

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    @Keep
    fun registerScreenOFFListener(context: Context, callback: (Boolean) -> Unit) {
        ContextCompat.registerReceiver(context.applicationContext,
            object : BroadcastReceiver() {
                override fun onReceive(
                    context: Context?,
                    intent: Intent?
                ) {
                    if (intent == null) {
                        throw RuntimeException("intent is null")
                    } else {
                        val action = intent.action
                        if (action == Intent.ACTION_SCREEN_OFF) {
                            callback(true)
                        } else if (action == Intent.ACTION_SCREEN_ON) {
                            callback(false)
                        }
                    }
                    context?.unregisterReceiver(this)
                }
            },
            IntentFilter().apply {
                addAction(Intent.ACTION_SCREEN_OFF)
                addAction(Intent.ACTION_SCREEN_ON)
            },
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    @Keep
    fun registerAppCreateCallback(callback: () -> Unit) {
        this.appCreateCallback = callback
    }

    @Keep
    fun registerAppStartCallback(callback: () -> Unit) {
        this.appStartCallback = callback
    }

    @Keep
    fun registerAppPauseCallback(callback: () -> Unit) {
        this.appPauseCallback = callback
    }

    @Keep
    fun registerAppResumeCallback(callback: () -> Unit) {
        this.appResumeCallback = callback
    }

    @Keep
    fun registerAppStopCallback(callback: () -> Unit) {
        this.appStopCallback = callback
    }

    @Keep
    fun registerAppDestroyCallback(callback: () -> Unit) {
        this.appDestroyCallback = callback
    }

    override fun onStateChanged(
        source: LifecycleOwner,
        event: Lifecycle.Event
    ) {
        if (source == ProcessLifecycleOwner.get()) {
            if (event == Lifecycle.Event.ON_CREATE) {
                appCreateCallback?.invoke()
            }
            if (event == Lifecycle.Event.ON_START) {
                appStartCallback?.invoke()
            }
            if (event == Lifecycle.Event.ON_PAUSE) {
                appPauseCallback?.invoke()
            }
            if (event == Lifecycle.Event.ON_RESUME) {
                appResumeCallback?.invoke()
            }
            if (event == Lifecycle.Event.ON_STOP) {
                appStopCallback?.invoke()
            }
            if (event == Lifecycle.Event.ON_DESTROY) {
                appDestroyCallback?.invoke()
            }
        }
    }
}
