package com.zebra.emdk_kotlin_wrapper.emdk

import android.content.Context
import androidx.annotation.Keep
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Keep
class EMDKHelper private constructor() {

    private var foregroundScope = CoroutineScope(Dispatchers.Main + Job())
    internal val core = EMDKCore()

    @Keep
    companion object Static {
        private var instance: EMDKHelper? = null

        public val shared: EMDKHelper
            get() {
                if (instance == null) {
                    instance = EMDKHelper()
                }
                return instance!!
            }
    }

    @Keep
    fun getEMDKVersion(): String {
        return core.emdkVersion
    }

    @Keep
    fun getMXVersion(): String {
        return core.mxVersion
    }

    @Keep
    fun getDWVersion(): String {
        return core.dwVersion
    }

    @Keep
    public fun prepare(context: Context, callback: (success: Boolean) -> Unit) {
        core.prepareEMDKManager(context) {
            foregroundScope.launch {
                callback(it)
            }
        }
    }

    @Keep
    fun teardown() {
        core.teardown()
    }
}