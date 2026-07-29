# ZebraOSUpdateTool

Small Android app to upgrade / downgrade the OS of a Zebra device from a local BSP zip file,
built on top of the `emdk_kotlin_wrapper` module taken from
[zebra-sdk-kotlin-wrapper](../zebra-sdk-kotlin-wrapper).

Please be aware that this application is provided as a community project without any guarantee of support.

## Features

1. **Choose the BSP zip file**
   - built-in storage browser (folders + `.zip` files, shows file size)
   - system document picker, the returned `content://` URI is converted back to a real file path
   - the path can also be typed / pasted manually
2. **Upgrade or downgrade the OS with the MX API**
   - optional package verification before the update
   - `SuppressReboot` option for upgrade
   - live OS update status, cancel an ongoing update, manual reboot

## How it works

Everything goes through MX Power Manager (`PowerMgr`) profiles inside the wrapper module:

| App action              | MXHelper call                    | MX `ResetAction` |
|-------------------------|----------------------------------|------------------|
| Verify Package Only     | `MXHelper.checkOSZipFile()`      | 9                |
| Upgrade OS              | `MXHelper.upgradeOS()`           | 10               |
| Downgrade OS            | `MXHelper.downgradeOS()`         | 11               |
| Cancel Ongoing Update   | `MXHelper.cancelOngoingUpdate()` | 14               |
| Reboot Device           | `MXHelper.setDeviceToReboot()`   | 4                |

Status is read from the `oem_info` content provider through `MXHelper.fetchOSUpdateStatus()`
(`UNKNOWN`, `IN_PROGRESS`, `WAITING_FOR_REBOOT`, `PASSED`, `FAILED`, ...) and is polled every 5
seconds while the app is in the foreground.

`EMDKHelper.shared.prepare()` is called in `MainActivity.onCreate()`; all MX buttons stay inert
until the EMDK session is open.

## Project layout

```
app/                                  the tool itself
  src/main/java/com/zebra/osupdatetool/
    MainActivity.kt                   Compose UI (file selection, options, actions, status, log)
    MainViewModel.kt                  MX calls + status polling + logging
    util/StorageUtils.kt              storage roots, zip listing, content:// -> file path
    ui/components/FilePickerDialog.kt built-in file browser
    ui/components/ComposableUI.kt     RoundButton / StyledOutlinedTextField (from the wrapper demo)
emdk_kotlin_wrapper/                  unchanged copy of the wrapper library module
```

## Requirements / notes

- Zebra device with EMDK service; MX 10.0 or later for the OS update actions used here.
- The BSP package must be reachable by a **real file path** (for example
  `/sdcard/Download/AT_FULL_UPDATE_xx.zip`). MX cannot install from a `content://` URI, that is why
  the app resolves the picked document to a path and refuses to continue if it cannot.
- Android 11+: grant **All files access** (the app opens the settings page on first start and shows
  a "Grant All Files Access" button while the permission is missing).
- `SuppressReboot` is only honored by MX on **upgrade**. With it enabled the device reports
  `WAITING_FOR_REBOOT` after the package is installed and the app offers to reboot.
- Downgrade requires the target BSP to allow it (anti-rollback / Verified Boot restrictions still
  apply, verification failure is reported in the log).
- As in the wrapper demo project: if `ProfileManager` comes back null, open
  Android Studio menu **EMDK > Profile Manager** once and create an empty profile.

## Build

```
gradlew :app:assembleDebug
gradlew :app:assembleRelease
```

Outputs: `app/build/outputs/apk/debug/app-debug.apk`, `app/build/outputs/apk/release/app-release-unsigned.apk`.

Requires JDK 17+ (for example the Android Studio JBR: `set JAVA_HOME=C:\Program Files\Android\Android Studio\jbr`).
