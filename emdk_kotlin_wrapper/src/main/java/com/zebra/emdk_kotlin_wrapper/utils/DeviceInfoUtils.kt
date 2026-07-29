package com.zebra.emdk_kotlin_wrapper.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.Keep

@Keep
object DeviceInfoUtils {

    @Keep
    fun hasTelephonyFeature(context: Context): Boolean {
        val pm: PackageManager = context.packageManager
        val hasTelephony = pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)
        return hasTelephony
    }
}