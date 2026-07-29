package com.zebra.emdk_kotlin_wrapper.mx

import android.content.Context

/**
 * https://techdocs.zebra.com/mx/datawedgemgr/
 *
 * XML:
 * profile_datawedge_manager_import_profile.xml
 *
 * Path:
 * /data/tmp/public/dwprofile_ocr_workflow.db
 * */
internal fun MXProfileProcessor.importProfile(
    context: Context,
    profileFileName: String,
    delaySeconds: Long,
    callback: (MXBase.ErrorInfo?) -> Unit) {
    processProfileWithCallback(
        context,
        MXBase.ProfileXML.DataWedgeManagerImportProfile,
        MXBase.ProfileName.DataWedgeManagerImportProfile,
        mapOf(
            MXConst.ConfigurationFile to profileFileName
        ),
        delaySeconds,
        callback
    )
}