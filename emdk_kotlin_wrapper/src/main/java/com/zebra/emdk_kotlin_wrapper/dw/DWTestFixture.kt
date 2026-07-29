package com.zebra.emdk_kotlin_wrapper.dw

import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import androidx.annotation.Keep

@Keep
object DWTestFixture {

    private val handlerThread = HandlerThread("DWIntentFactory_background_thread").apply { start() }
    private val handler = Handler(handlerThread.looper)

    @Keep
    fun test(context: Context) {
        DWIntentFactory.callDWAPI(context, DWAPI.ActionExtraKeys.ENABLE_DATAWEDGE, true) { result ->

        }
    }

}