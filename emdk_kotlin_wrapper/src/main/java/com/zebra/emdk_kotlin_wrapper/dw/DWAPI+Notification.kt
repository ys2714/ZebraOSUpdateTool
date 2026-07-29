package com.zebra.emdk_kotlin_wrapper.dw

import android.content.Context
import android.os.Bundle
import com.zebra.emdk_kotlin_wrapper.zdm.ZDMConst
import com.zebra.emdk_kotlin_wrapper.zdm.ZDMTokenStore

/**
 * https://techdocs.zebra.com/datawedge/15-0/guide/api/registerfornotification/
 *
 * @Parameters:
 * ACTION [String]: "com.symbol.datawedge.api.ACTION"
 * EXTRA_DATA [String]: "com.symbol.datawedge.api.REGISTER_FOR_NOTIFICATION"
 * BUNDLE:
 *  - APPLICATION_NAME - Package name of the app to register
 *  - NOTIFICATION_TYPE - Supported types:
 *  - CONFIGURATION_UPDATE
 *  - PROFILE_SWITCH
 *  - SCANNER_STATUS
 *  - WORKFLOW_STATUS - status changes to Workflow Input; also applies to Barcode Highlighting
 *
 * @Return Values:
 * Returns a bundle with status of the requested DataWedge NOTIFICATION_TYPE
 * EXTRA NAME: "com.symbol.datawedge.api.NOTIFICATION"
 * BUNDLE:
 *  - CONFIGURATION_UPDATE [String]:
 *    - "PROFILE_IMPORTED" "FULL_DB_IMPORTED"
 *    - "PROFILE_NAME": "<Application package name>"
 *  - PROFILE_SWITCH:
 *    - "PROFILE_IMPORTED" "FULL_DB_IMPORTED"
 *    - "PROFILE_NAME": "<name of Profile now in use>"
 *  - SCANNER_STATUS:
 *    - WAITING - Scanner is enabled and ready to scan using a physical trigger or SOFT_SCAN_TRIGGER intent.
 *    - SCANNING - Scanner has emitted the scan beam and scanning is in progress. This event does not prevent the application from disabling other controls as necessary.
 *    - CONNECTED - A Bluetooth scanner has connected with the device and can now be enabled (or disabled) by the application. Scanner selection should be set to Auto in the currently active profile.
 *    - DISCONNECTED - A Bluetooth scanner has disconnected from the device. Sending an intent to enable or disable the scanner in this state will enable/disable the current default scanner.
 *    - IDLE - Scanner is in one of the following states: enabled but not yet in the waiting state, in the suspended state by an intent (e.g. SUSPEND_PLUGIN) or disabled due to the hardware trigger.
 *    - DISABLED - Scanner is disabled. This is broadcasted by the scanner plug-in when the active profile becomes disabled manually or the scanner is disabled with an intent (e.g. DISABLE_PLUGIN).
 *    - WORKFLOW_STATUS - "Workflow" refers to usage related to Workflow Input (e.g. scanning identification documents, reading license plates, etc.) or Barcode Highlighting:
 *    - PLUGIN_READY - Workflow Input plug-in or Barcode Highlighting is ready to scan. This state notifies the user that DataWedge is ready to accept Intent API calls related to Workflow Input. Any intent API calls sent to DataWedge before this state will encounter undefined behavior. When a device is rebooted, sometimes it may take few seconds to initialize Workflow Input.
 *    - DISABLED - Selected workflow is disabled. This is broadcasted by the Workflow Input plug-in when the active profile becomes disabled manually or the workflow is disabled with an intent.
 *    - WORKFLOW_READY - Workflow Input plug-in or Barcode Highlighting is enabled and the selected workflow is in the process of being enabled, but not fully enabled yet.
 *    - WORKFLOW_ENABLED - Workflow Input plug-in or Barcode Highlighting is enabled and the selected workflow is enabled, waiting for the session to start.
 *    - SESSION_STARTED - Workflow is ready and waiting for the user to press the trigger to scan.
 *    - CAPTURING_STARTED - Viewfinder has started - the user can place the subject in the field of view to capture data.
 *    - CAPTURING_STOPPED - Viewfinder has stopped and the status immediately returns to the SESSION_STARTED state.
 *
 * Scanner status notifications are sent only if the scanner in the active Profile is enabled.
 * Note: The PROFILE_NAME (of the currently active profile) is returned with SCANNER_STATUS to allow the developer to filter scanner events for the required Profile only.
 * */
internal fun DWAPI.registerNotification(context: Context, notificationType: DWAPI.NotificationType, callback: (Boolean) -> Unit) {
    val packageName = context.applicationContext.packageName
    val token = ZDMTokenStore.getToken(ZDMConst.DelegationScope.SCOPE_DW_NOTIFICATION_API)
    DWIntentFactory.callDWAPI(
        context,
        DWAPI.ActionExtraKeys.REGISTER_FOR_NOTIFICATION,
        Bundle().apply {
            putString("com.symbol.datawedge.api.APPLICATION_NAME", packageName)
            putString("com.symbol.datawedge.api.NOTIFICATION_TYPE", notificationType.value)
            putString("APPLICATION_PACKAGE", packageName)
            putString("TOKEN", token)
        }
    ) { result ->
        result.onSuccess {
            callback(true)
        }.onFailure {
            callback(false)
        }
    }
}

internal fun DWAPI.unregisterNotification(context: Context, notificationType: DWAPI.NotificationType, callback: (Boolean) -> Unit) {
    val packageName = context.applicationContext.packageName
    val token = ZDMTokenStore.getToken(ZDMConst.DelegationScope.SCOPE_DW_NOTIFICATION_API)
    DWIntentFactory.callDWAPI(
        context,
        DWAPI.ActionExtraKeys.UNREGISTER_FOR_NOTIFICATION,
        Bundle().apply {
            putString("com.symbol.datawedge.api.APPLICATION_NAME", context.applicationContext.packageName)
            putString("com.symbol.datawedge.api.NOTIFICATION_TYPE", notificationType.value)
            putString("APPLICATION_PACKAGE", packageName)
            putString("TOKEN", token)
        }
    ) { result ->
        result.onSuccess {
            callback(true)
        }.onFailure {
            callback(false)
        }
    }
}
