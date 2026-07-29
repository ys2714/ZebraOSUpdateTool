package com.zebra.emdk_kotlin_wrapper.mx

import android.content.Context

/**
 * https://techdocs.zebra.com/mx/filemgr/
 *
 * XML:
 * profile_file_manager_copy_embedded_free_form_ocr.xml
 *
 * targetPathAndFileName:
 * /data/tmp/public/dwprofile_ocr_workflow.db
 * */
internal fun MXProfileProcessor.copyEmbeddedFreeFormOCRProfile(
    context: Context,
    targetPathAndFileName: String,
    delaySeconds: Long,
    callback: (MXBase.ErrorInfo?) -> Unit) {
    processProfileWithCallback(
        context,
        MXBase.ProfileXML.FileManagerCopyEmbeddedFreeFormOCR,
        MXBase.ProfileName.FileManagerCopyEmbeddedFreeFormOCR,
        mapOf(
            MXConst.TargetPathAndFileName to targetPathAndFileName
        ),
        delaySeconds,
        callback
    )
}