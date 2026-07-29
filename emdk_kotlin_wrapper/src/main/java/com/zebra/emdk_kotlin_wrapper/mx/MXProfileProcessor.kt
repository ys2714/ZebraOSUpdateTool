package com.zebra.emdk_kotlin_wrapper.mx

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.util.Log
import android.util.Xml
import androidx.annotation.Keep
import com.symbol.emdk.EMDKResults
import com.symbol.emdk.ProfileManager
import com.zebra.emdk_kotlin_wrapper.emdk.EMDKHelper
import com.zebra.emdk_kotlin_wrapper.utils.AssetsReader
import com.zebra.emdk_kotlin_wrapper.utils.FixedSizeQueue
import com.zebra.emdk_kotlin_wrapper.utils.FixedSizeQueueItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.xmlpull.v1.XmlPullParserException
import java.io.StringReader
import kotlinx.coroutines.*

/**
 * https://techdocs.zebra.com/emdk-for-android/14-0/guide/profile-manager-guides/
 *
 * */
internal object MXProfileProcessor {

    internal val TAG = MXProfileProcessor::class.java.simpleName

    internal var foregroundScope = CoroutineScope(Dispatchers.Main + Job())
    internal var backgroundScope = CoroutineScope(Dispatchers.IO + Job())

    internal val profileManager: ProfileManager
        get() {
            if (EMDKHelper.shared.core.profileManager == null) {
                throw RuntimeException("please call EMDKHelper.prepare() before get profileManager")
            }
            return EMDKHelper.shared.core.profileManager!!
        }

    // handling max 20 profiles at the same time, for the 21th profile, the first will be disposed
    private val listeners = FixedSizeQueue<ProfileDataListener>(20)

    @Keep
    class ProfileDataListener(
        val profileName: MXBase.ProfileName,
        val callback: (ProfileDataListener, Result<MXBase.ErrorInfo?>) -> Unit
    ): ProfileManager.DataListener, FixedSizeQueueItem {

        @Keep
        override fun onData(data: ProfileManager.ResultData?) {
            if (data == null) {
                callback(this, Result.failure(RuntimeException("ProfileDataListener - receive null data !")))
                return
            }
            if (data.profileName == profileName.string) {
                when (data.result.statusCode) {
                    EMDKResults.STATUS_CODE.SUCCESS -> {
                            callback(this, Result.success(null))
                    }
                    EMDKResults.STATUS_CODE.CHECK_XML -> {
                        try {
                            XMLParser.parseXML(
                                Xml.newPullParser().apply {
                                    setInput(StringReader(data.result.statusString))
                                }
                            ).onSuccess {
                                    callback(this, Result.success(null))
                            }.onFailure { error ->
                                    callback(this, Result.failure(error))
                            }
                        } catch (e: XmlPullParserException) {
                            Log.e(MXProfileProcessor.TAG, "Failed to parse XML response", e)
                                val error = MXBase.ErrorInfo(
                                    MXProfileProcessor.TAG,
                                    "XmlPullParserException",
                                    e.message ?: "Unknown parser exception")
                                callback(this, Result.failure(error))
                        }
                    }
                    else -> {
                        Log.e(MXProfileProcessor.TAG, "failed with response: ${data.result.statusString}")
                            val error = MXBase.ErrorInfo(
                                MXProfileProcessor.TAG,
                                data.result.statusString,
                                data.result.extendedStatusMessage)
                            callback(this, Result.failure(error))
                    }
                }
            } else {
                // other profile name, ignore
                return
            }
        }

        override fun getID(): String {
            return profileName.string
        }

        override fun onDisposal() {
            profileManager.removeDataListener(this)
        }
    }

    fun processProfileWithCallback(context: Context,
                                   fileName: MXBase.ProfileXML,
                                   profileName: MXBase.ProfileName,
                                   params: Map<String, String>? = null,
                                   delaySeconds: Long = 0,
                                   callback: (MXBase.ErrorInfo?) -> Unit) {
        backgroundScope.launch {
            try {
                if (fileName == MXBase.ProfileXML.None) {
                    val completion = async { processProfile(context, profileName, null) }
                    val result = completion.await()
                    delay(delaySeconds * 1000)
                    foregroundScope.launch {
                        callback(result)
                    }
                } else {
                    val content = AssetsReader.readFileToStringWithParams(context, fileName.string, params).trimIndent()
                    val completion = async { processProfile(context, profileName, arrayOf(content)) }
                    val result = completion.await()
                    delay(delaySeconds * 1000)
                    foregroundScope.launch {
                        callback(result)
                    }
                }
            } catch (e: Exception) {
                callback(MXBase.ErrorInfo(TAG, "Exception", e.message ?: "Unknown exception"))
            }
        }
    }

    private suspend fun processProfile(context: Context,
                               profileName: MXBase.ProfileName,
                               profileContent: Array<String>?) : MXBase.ErrorInfo? = suspendCancellableCoroutine { continuation ->
        val listener = ProfileDataListener(profileName, { listener, result ->
            if (continuation.isActive) {
                listeners.remove(listener)
                continuation.resumeWith(result)
            }
        })
        listeners.enqueue(listener)
        profileManager.addDataListener(listener)
        profileManager.processProfileAsync(
            profileName.string,
            ProfileManager.PROFILE_FLAG.SET,
            profileContent)
    }

    /**
     * https://developer.android.com/reference/android/app/admin/DeviceAdminReceiver.html#DeviceAdminReceiver()
     *
     * */
    class DevAdminReceiver: DeviceAdminReceiver() {

    }
}
