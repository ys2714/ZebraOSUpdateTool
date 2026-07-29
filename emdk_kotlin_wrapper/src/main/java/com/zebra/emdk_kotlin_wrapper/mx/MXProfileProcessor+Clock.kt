package com.zebra.emdk_kotlin_wrapper.mx

import android.content.Context
import com.symbol.emdk.ProfileManager

/**
 *
 * https://techdocs.zebra.com/mx/clock/
 *
 *
 * */
internal fun MXProfileProcessor.callClockResetAuto(
    context: Context,
    is24Hours: Boolean,
    ntpServer: String,
    syncInterval: String,
    delaySeconds: Long = 0,
    callback: (MXBase.ErrorInfo?) -> Unit) {
    val militaryTime = if (is24Hours) "1" else "2"
    val map = mapOf(
        MXConst.TimeZone to "GMT+9", // ntp update every 30min, so use GMT+9 as default
        MXConst.NTPServer to ntpServer,
        MXConst.SyncInterval to syncInterval,
        MXConst.MilitaryTime to militaryTime
    )
    processProfileWithCallback(
        context,
        MXBase.ProfileXML.ClockResetAuto,
        MXBase.ProfileName.ClockResetAuto,
        map,
        delaySeconds,
        callback
    )
}

internal fun MXProfileProcessor.callClockSet(
    context: Context,
    is24Hours: Boolean,
    timeZone: String,
    date: String,
    time: String,
    delaySeconds: Long = 0,
    callback: (MXBase.ErrorInfo?) -> Unit) {
    val militaryTime = if (is24Hours) "1" else "2"
    val map = mapOf(
        MXConst.AutoTime to "false",
        MXConst.TimeZone to timeZone,
        MXConst.Date to date,
        MXConst.Time to time,
        MXConst.MilitaryTime to militaryTime
    )
    processProfileWithCallback(
        context,
        MXBase.ProfileXML.ClockSet,
        MXBase.ProfileName.ClockSet,
        map,
        delaySeconds,
        callback
    )
}