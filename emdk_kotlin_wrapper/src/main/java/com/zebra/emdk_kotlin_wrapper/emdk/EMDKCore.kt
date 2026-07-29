package com.zebra.emdk_kotlin_wrapper.emdk

import android.content.Context
import androidx.annotation.Keep
import com.symbol.emdk.EMDKBase
import com.symbol.emdk.EMDKManager
import com.symbol.emdk.ProfileManager
import com.symbol.emdk.VersionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine

internal class EMDKCore {

    private var backgroundScope = CoroutineScope(Dispatchers.IO + Job())

    private var _manager: EMDKManager? = null
    private var _profileManager: ProfileManager? = null
    private var _versionManager: VersionManager? = null

    internal val versionManager: VersionManager?
        get() {
            return _versionManager
        }

    internal val profileManager: ProfileManager?
        get() {
            return _profileManager
        }

    val emdkVersion: String
        get() {
            if (_versionManager == null) {
                throw RuntimeException("please call this after EMDKHelper.shared.prepare() return")
            }
            return _versionManager!!.getVersion(VersionManager.VERSION_TYPE.EMDK)
        }

    val mxVersion: String
        get() {
            if (_versionManager == null) {
                throw RuntimeException("please call this after EMDKHelper.shared.prepare() return")
            }
            return _versionManager!!.getVersion(VersionManager.VERSION_TYPE.MX)
        }

    val dwVersion: String
        get() {
            if (_versionManager == null) {
                throw RuntimeException("please call this after EMDKHelper.shared.prepare() return")
            }
            return _versionManager!!.getVersion(VersionManager.VERSION_TYPE.BARCODE)
        }

    @Keep
    internal fun prepareEMDKManager(context: Context, complete: (Boolean) -> Unit) {
        if (_manager != null) {
            complete(true)
            return
        }
        EMDKManager.getEMDKManager(
            context.applicationContext,
            object : EMDKManager.EMDKListener {

                @Keep
                override fun onOpened(manager: EMDKManager?) {
                    if (manager == null) {
                        complete(false)
                        return
                    }
                    this@EMDKCore._manager = manager

                    backgroundScope.launch {
                        val versionResult = async { prepareVersionManager(manager) }
                        _versionManager = versionResult.await()

                        val profileResult = async { prepareProfileManager(manager) }
                        _profileManager = profileResult.await()

                        complete(true)
                    }
                }

                @Keep
                override fun onClosed() {
                    this@EMDKCore.teardown()
                }
            }
        )
    }

    internal fun teardown() {
        _versionManager = null
        _profileManager = null
        _manager?.release()
        _manager = null
    }

    @Keep
    private suspend fun prepareVersionManager(emdkManager: EMDKManager): VersionManager? = suspendCancellableCoroutine { continuation ->
        emdkManager.getInstanceAsync(
            EMDKManager.FEATURE_TYPE.VERSION,
            object : EMDKManager.StatusListener {

                @Keep
                override fun onStatus(status: EMDKManager.StatusData?, base: EMDKBase?) {
                    if (status == null) {
                        continuation.resumeWith(Result.failure(Exception("status null")))
                        return
                    }
                    if (status.featureType != EMDKManager.FEATURE_TYPE.VERSION) {
                        continuation.resumeWith(Result.failure(Exception("wrong feature type")))
                        return
                    }
                    val manager = base as? VersionManager
                    if (manager == null) {
                        continuation.resumeWith(Result.failure(Exception("get null manager")))
                        return
                    }
                    // passed all checks
                    continuation.resumeWith(Result.success(manager))
                }
            }
        )
    }

    @Keep
    private suspend fun prepareProfileManager(emdkManager: EMDKManager): ProfileManager? = suspendCancellableCoroutine { continuation ->
        emdkManager.getInstanceAsync(
            EMDKManager.FEATURE_TYPE.PROFILE,
            object : EMDKManager.StatusListener {

                @Keep
                override fun onStatus(status: EMDKManager.StatusData?, base: EMDKBase?) {
                    if (status == null) {
                        continuation.resumeWith(Result.failure(Exception("status null")))
                        return
                    }
                    if (status.featureType != EMDKManager.FEATURE_TYPE.PROFILE) {
                        continuation.resumeWith(Result.failure(Exception("wrong feature type")))
                        return
                    }
                    val manager = base as? ProfileManager
                    if (manager == null) {
                        continuation.resumeWith(Result.failure(Exception("get null manager")))
                        return
                    }
                    // passed all checks
                    continuation.resumeWith(Result.success(manager))
                }
            }
        )
    }
}