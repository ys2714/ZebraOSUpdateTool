package com.zebra.emdk_kotlin_wrapper.zdm

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log
import androidx.annotation.Keep

@Keep
object ZDMAuthHelper {

    const val COLUMN_QUERY_RESULT: String = "query_result"

    @Keep
    fun acquireToken(context: Context, delegation_scope: ZDMConst.DelegationScope): String? {
        var AUTHORITY_URI: Uri? = Uri.parse("content://com.zebra.devicemanager.zdmcontentprovider")
        var ACQUIRE_TOKEN_URI: Uri? = Uri.withAppendedPath(AUTHORITY_URI, "AcquireToken")

        if (ACQUIRE_TOKEN_URI == null) { return null }

        var token: String? = ""
        try {
            val cursor: Cursor? = context.getContentResolver().query(
                ACQUIRE_TOKEN_URI, null as Array<String?>?,
                "delegation_scope=?", arrayOf<String?>(delegation_scope.value), null as String?
            )
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst()
                token = cursor.getString(cursor.getColumnIndex(COLUMN_QUERY_RESULT))
                cursor.close()
            }
        } catch (var3: Exception) {
            if (var3 is SecurityException) {
                Log.e("BroadcastProtection", "Invalid Token/Caller")
            } else {
                Log.e("BroadcastProtection", "Unknown Caller to acquire token")
            }
        }

        ZDMTokenStore.saveToken(delegation_scope, token)
        return token
    }
}