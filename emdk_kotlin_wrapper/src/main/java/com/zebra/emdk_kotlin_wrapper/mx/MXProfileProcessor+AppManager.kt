import android.content.Context
import com.zebra.emdk_kotlin_wrapper.mx.MXBase
import com.zebra.emdk_kotlin_wrapper.mx.MXConst
import com.zebra.emdk_kotlin_wrapper.mx.MXProfileProcessor

internal fun MXProfileProcessor.disableApp(
    context: Context,
    packageName: String,
    delaySeconds: Long = 0,
    callback: (MXBase.ErrorInfo?) -> Unit) {
    processProfileWithCallback(
        context,
        MXBase.ProfileXML.AppManagerDisable,
        MXBase.ProfileName.AppManagerDisable,
        mapOf(
            MXConst.Package to packageName
        ),
        delaySeconds,
        callback
    )
}