package com.zebra.osupdatetool

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.zebra.osupdatetool.ui.components.FilePickerDialog
import com.zebra.osupdatetool.ui.components.RoundButton
import com.zebra.osupdatetool.ui.components.StyledOutlinedTextField
import com.zebra.osupdatetool.ui.theme.ZebraOSUpdateToolTheme
import com.zebra.osupdatetool.util.StorageUtils
import java.io.File

/**
 * ZebraOSUpdateTool
 *
 * 1. choose a BSP zip file from the device storage
 * 2. upgrade or downgrade the OS with the MX Power Manager (through emdk_kotlin_wrapper)
 */
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    private val requestReadStorageLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) {
            viewModel.log("storage permission denied, the BSP package can not be read")
        }
        viewModel.refreshBSPFileInfo()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askStorageAccess()
        viewModel.prepareEMDK(this)
        setContent {
            ZebraOSUpdateToolTheme {
                RootView(this)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshBSPFileInfo()
        if (viewModel.emdkReady.value) {
            viewModel.startStatusPolling(this)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopStatusPolling()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            viewModel.teardownEMDK()
        }
    }

    /**
     * A BSP package sits in the shared storage, on Android 11 and above only
     * "All files access" gives the app a readable file path for it.
     */
    private fun askStorageAccess() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                openAllFilesAccessSettings()
            }
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestReadStorageLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun openAllFilesAccessSettings() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            return
        }
        runCatching {
            startActivity(
                Intent(
                    Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                    Uri.parse("package:$packageName")
                )
            )
        }.onFailure {
            startActivity(Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION))
        }
    }

    // ------------------------------------------------------------------ composables

    @Composable
    fun RootView(context: Context) {
        var showFilePicker by remember { mutableStateOf(false) }
        val openDocumentLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) { uri ->
            if (uri == null) {
                return@rememberLauncherForActivityResult
            }
            val path = StorageUtils.resolveDocumentUri(context, uri)
            if (path == null) {
                viewModel.log(
                    "can not map the picked document to a file path, " +
                        "please use \"Browse device storage\" instead"
                )
            } else {
                viewModel.selectBSPFile(path)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            HeaderSection()
            BSPFileSection(
                onBrowse = { showFilePicker = true },
                onSystemPicker = { openDocumentLauncher.launch(ZIP_MIME_TYPES) }
            )
            OptionSection()
            ActionSection(context)
            StatusSection(context)
            LogSection()
        }

        if (showFilePicker) {
            FilePickerDialog(
                startDirectory = File(viewModel.bspFilePath.value).parentFile,
                onDismiss = { showFilePicker = false },
                onFileSelected = { file ->
                    showFilePicker = false
                    viewModel.selectBSPFile(file.absolutePath)
                }
            )
        }

        viewModel.pendingConfirmMode.value?.let { mode ->
            ConfirmUpdateDialog(context, mode)
        }

        if (viewModel.shouldShowRebootDialog.value) {
            RebootDialog(context)
        }
    }

    @Composable
    private fun HeaderSection() {
        Text(
            "Zebra OS Update Tool",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Text(
            if (viewModel.emdkReady.value) {
                "EMDK ${viewModel.emdkVersion.value} / MX ${viewModel.mxVersion.value}"
            } else {
                "EMDK not connected"
            },
            fontSize = 12.sp,
            color = if (viewModel.emdkReady.value) Color(0xFF00801A) else Color(0xFFD10000)
        )
        HorizontalDivider(Modifier.padding(vertical = 8.dp))
    }

    @Composable
    private fun BSPFileSection(onBrowse: () -> Unit, onSystemPicker: () -> Unit) {
        Text("1. Choose the BSP zip file", fontWeight = FontWeight.Bold)
        StyledOutlinedTextField(
            placeholder = "/sdcard/Download/xx_FULL_UPDATE_xx.zip",
            currentValue = viewModel.bspFilePath.value,
            keyboardType = KeyboardType.Text,
            modifier = Modifier.fillMaxWidth()
        ) { newValue ->
            viewModel.bspFilePath.value = newValue
            viewModel.refreshBSPFileInfo()
        }
        if (viewModel.bspFileInfo.value.isNotEmpty()) {
            Text(viewModel.bspFileInfo.value, fontSize = 12.sp)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            RoundButton("Browse device storage", modifier = Modifier.weight(1f)) { onBrowse() }
            RoundButton("System file picker", modifier = Modifier.weight(1f)) { onSystemPicker() }
        }
        if (!StorageUtils.hasAllFilesAccess()) {
            Text(
                "all files access is not granted, the BSP package may not be readable",
                fontSize = 12.sp,
                color = Color(0xFFD10000)
            )
            RoundButton("Grant All Files Access", Color(0xFFD17F00)) {
                openAllFilesAccessSettings()
            }
        }
        HorizontalDivider(Modifier.padding(vertical = 8.dp))
    }

    @Composable
    private fun OptionSection() {
        Text("2. Options", fontWeight = FontWeight.Bold)
        SwitchRow(
            title = "verify the package before updating",
            checked = viewModel.verifyBeforeUpdate.value
        ) { viewModel.verifyBeforeUpdate.value = it }
        SwitchRow(
            title = "suppress reboot (upgrade only)",
            checked = viewModel.suppressReboot.value
        ) { viewModel.suppressReboot.value = it }
        Text(
            "with suppress reboot ON the device stays in WAITING_FOR_REBOOT " +
                "until you reboot it manually. MX ignores this option on downgrade.",
            fontSize = 11.sp
        )
        HorizontalDivider(Modifier.padding(vertical = 8.dp))
    }

    @Composable
    private fun SwitchRow(title: String, checked: Boolean, onChange: (Boolean) -> Unit) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Switch(checked = checked, onCheckedChange = onChange)
            Text(title, Modifier.padding(start = 8.dp), fontSize = 13.sp)
        }
    }

    @Composable
    private fun ActionSection(context: Context) {
        val enabled = viewModel.emdkReady.value && !viewModel.busy.value
        Text("3. Update the OS", fontWeight = FontWeight.Bold)
        RoundButton("Verify Package Only", Color(0xFF3F51B5)) {
            if (enabled) viewModel.verifyOnly(context)
        }
        RoundButton("Upgrade OS", Color(0xFF00801A)) {
            if (enabled) viewModel.requestUpdate(MainViewModel.UpdateMode.UPGRADE)
        }
        RoundButton("Downgrade OS", Color(0xFFD17F00)) {
            if (enabled) viewModel.requestUpdate(MainViewModel.UpdateMode.DOWNGRADE)
        }
        RoundButton("Cancel Ongoing Update", Color(0xFFD10000)) {
            if (viewModel.emdkReady.value) viewModel.cancelOngoingUpdate(context)
        }
        RoundButton("Reboot Device", Color(0xFF616161)) {
            if (viewModel.emdkReady.value) viewModel.rebootDevice(context)
        }
        HorizontalDivider(Modifier.padding(vertical = 8.dp))
    }

    @Composable
    private fun StatusSection(context: Context) {
        Text("OS update status", fontWeight = FontWeight.Bold)
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(8.dp)) {
                Text("status: ${viewModel.osUpdateStatus.value}", fontSize = 13.sp)
                Text("detail: ${viewModel.osUpdateDetail.value}", fontSize = 13.sp)
                Text("timestamp: ${viewModel.osUpdateTimestamp.value}", fontSize = 13.sp)
            }
        }
        RoundButton("Refresh Status") {
            viewModel.fetchStatusOnce(context)
        }
        HorizontalDivider(Modifier.padding(vertical = 8.dp))
    }

    @Composable
    private fun LogSection() {
        Text("Log", fontWeight = FontWeight.Bold)
        viewModel.logs.forEach { line ->
            Text(
                line,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }

    @Composable
    private fun ConfirmUpdateDialog(context: Context, mode: MainViewModel.UpdateMode) {
        AlertDialog(
            onDismissRequest = { viewModel.cancelConfirm() },
            confirmButton = {
                TextButton(onClick = { viewModel.startUpdate(context, mode) }) {
                    Text(mode.title.uppercase())
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.cancelConfirm() }) {
                    Text("CANCEL")
                }
            },
            title = { Text("${mode.title} the OS ?") },
            text = {
                Column {
                    Text("package:")
                    Text(
                        viewModel.bspFilePath.value,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        "\nThe device will install this package and reboot. " +
                            "Do not remove the package or power off the device during the update.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        )
    }

    @Composable
    private fun RebootDialog(context: Context) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissRebootDialog() },
            confirmButton = {
                TextButton(onClick = { viewModel.rebootDevice(context) }) {
                    Text("REBOOT NOW")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissRebootDialog() }) {
                    Text("LATER")
                }
            },
            title = { Text("Package installed") },
            text = { Text("The OS update is waiting for a reboot to complete.") }
        )
    }

    companion object {
        private val ZIP_MIME_TYPES = arrayOf(
            "application/zip",
            "application/x-zip-compressed",
            "application/octet-stream"
        )
    }
}
