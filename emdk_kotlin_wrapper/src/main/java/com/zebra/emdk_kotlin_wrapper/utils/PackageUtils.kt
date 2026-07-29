package com.zebra.emdk_kotlin_wrapper.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Build
import android.util.Base64
import android.util.Log
import androidx.annotation.Keep
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * A utility object for retrieving package information, such as the application's signature.
 */
@Keep
object PackageUtils {

    private val TAG = PackageUtils::class.java.simpleName

    @Keep
    fun getPackageSignatureSHA1(context: Context): String {
        return try {
            val signatures = getSigningCertificates(context)
            // A package must be signed, so the signature array should not be empty.
            if (signatures.isNotEmpty()) {
                // We are interested in the first signature.
                val signature = signatures[0]
                getSHA1(signature.toByteArray())
            } else {
                Log.w(TAG, "No signatures found for package: " + context.packageName)
                ""
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "Failed to get package signature. Package not found.", e)
            ""
        }
    }

    /**
     * Retrieves the Base64-encoded signature of the application package.
     *
     * @param context The context to use for accessing the package manager.
     * @return The Base64-encoded signature as a String, or an empty string if an error occurs.
     */
    @Keep
    fun getPackageSignature(context: Context): String {
        return try {
            val signatures = getSigningCertificates(context)
            // A package must be signed, so the signature array should not be empty.
            if (signatures.isNotEmpty()) {
                // We are interested in the first signature.
                val signature = signatures[0]
                Base64.encodeToString(signature.toByteArray(), Base64.NO_WRAP)
            } else {
                Log.w(TAG, "No signatures found for package: " + context.packageName)
                ""
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "Failed to get package signature. Package not found.", e)
            ""
        }
    }

    /**
     * Retrieves the signing certificates for the current application package.
     * This method handles the differences between modern (API 28+) and legacy Android versions.
     *
     * @param context The context to use for accessing the package manager.
     * @return An array of [Signature] objects.
     * @throws PackageManager.NameNotFoundException if the package name is not found.
     */
    @SuppressLint("PackageManagerGetSignatures")
    @Throws(PackageManager.NameNotFoundException::class)
    private fun getSigningCertificates(context: Context): Array<Signature> {
        val packageName = context.packageName
        val pm = context.packageManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // For Android P (API 28) and above, use GET_SIGNING_CERTIFICATES
            val packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
            packageInfo.signingInfo?.apkContentsSigners ?: emptyArray()
        } else {
            // For older versions, use the deprecated GET_SIGNATURES
            @Suppress("DEPRECATION")
            val packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            @Suppress("DEPRECATION")
            packageInfo.signatures ?: emptyArray()
        }
    }

    private fun getSHA1(sig: ByteArray): String {
        var digest: MessageDigest? = null
        try {
            digest = MessageDigest.getInstance("SHA1")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        digest!!.update(sig)
        val hashtext = digest.digest()
        return bytesToHex(hashtext)
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val hexArray = charArrayOf(
            '0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'A', 'B', 'C', 'D', 'E', 'F'
        )
        val hexChars = CharArray(bytes.size * 2)
        var v: Int
        for (j in bytes.indices) {
            v = bytes[j].toInt() and 0xFF
            hexChars[j * 2] = hexArray[v ushr 4]
            hexChars[j * 2 + 1] = hexArray[v and 0x0F]
        }
        return String(hexChars)
    }
}
