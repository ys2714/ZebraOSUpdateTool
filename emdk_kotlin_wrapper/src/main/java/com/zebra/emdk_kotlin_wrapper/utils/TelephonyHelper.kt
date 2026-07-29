package com.zebra.emdk_kotlin_wrapper.utils

import android.content.Context
import android.content.Intent

/***
 * https://developer.android.com/training/app-links
 *
 * */
object TelephonyHelper {

    /***
     * intent:#Intent;action=android.settings.NETWORK_OPERATOR_SETTINGS;package=com.android.settings;end
     *
     * This launches the standard SIM and Mobile Network settings page, where the user can click "Preferred network type"
     * by intent. Standard & Safe way
     * */
    fun openMobileNetworkSettings(context: Context, simSubscriptionId: String = "") {
        val intent = Intent(android.provider.Settings.ACTION_NETWORK_OPERATOR_SETTINGS)
        // Optional (for Android 10+): If you want to specify a particular SIM slot
        if (!simSubscriptionId.isEmpty()) {
            intent.putExtra("android.provider.extra.SUB_ID", simSubscriptionId)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /***
     * intent:#Intent;action=android.settings.NETWORK_OPERATOR_SETTINGS;package=com.android.settings;component=com.android.settings/.Settings$MobileNetworkActivity;end
     *
     * This bypasses generic routing and launches the concrete settings class directly
     * */
    fun openMobileNetworkSettingsByActivity(context: Context) {
        val intent = Intent()
        intent.setClassName(
            "com.android.settings",
            "com.android.settings.Settings\$MobileNetworkActivity"
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /***
     * intent:#Intent;action=android.intent.action.MAIN;package=com.android.settings;component=com.android.settings/.RadioInfo;end
     *
     * This opens the system's hidden testing menu (traditionally accessed via *#*#4636#*#*)
     * This screen contains the Set Preferred Network Type dropdown and works even if your carrier has hidden the setting from standard menu screens
     * */
    fun openHiddenRadioInfoMenu(context: Context) {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.setClassName(
            "com.android.settings",
            "com.android.settings.RadioInfo")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}