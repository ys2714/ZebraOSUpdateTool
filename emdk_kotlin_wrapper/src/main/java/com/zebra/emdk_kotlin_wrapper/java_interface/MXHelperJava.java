package com.zebra.emdk_kotlin_wrapper.java_interface;

import android.content.Context;
import android.os.Build;

import androidx.annotation.Keep;
import androidx.annotation.RequiresApi;
import com.zebra.emdk_kotlin_wrapper.mx.MXBase;
import com.zebra.emdk_kotlin_wrapper.mx.MXHelper;
import kotlin.Unit;

@Keep
public class MXHelperJava {

    @Keep
    public static void whiteListApproveApp(Context context, Long delaySeconds, BooleanCompletion completion) {
        MXHelper.INSTANCE.whiteListApproveApp(context, delaySeconds, (success) -> {
            completion.onComplete(success);
            return Unit.INSTANCE;
        });
    }

    @Keep
    public static void setDeviceToSleep(Context context) {
        MXHelper.INSTANCE.setDeviceToSleep(context, 0);
    }

    @Keep
    public static void setDeviceToReboot(Context context) {
        MXHelper.INSTANCE.setDeviceToReboot(context, 0);
    }

    @Keep
    public static void setSystemClock(Context context, String timeZone, String date, String time, Long delaySeconds, BooleanCompletion completion) {
        MXHelper.INSTANCE.setSystemClock(context, timeZone, date, time, delaySeconds, (success) -> {
            completion.onComplete(success);
            return Unit.INSTANCE;
        });
    }

    @Keep
    public static void resetSystemClockToNTP(Context context, String ntpServer, String syncInterval, Long delaySeconds, BooleanCompletion completion) {
        MXHelper.INSTANCE.resetSystemClockToNTP(context, ntpServer, syncInterval, delaySeconds, (success) -> {
            completion.onComplete(success);
            return Unit.INSTANCE;
        });
    }

    @Keep
    public static void setScreenLockType(Context context, MXBase.ScreenLockType lockType, Long delaySeconds, BooleanCompletion completion) {
        MXHelper.INSTANCE.setScreenLockType(context, lockType, delaySeconds, (success) -> {
            completion.onComplete(success);
            return Unit.INSTANCE;
        });
    }

    @Keep
    public static void setScreenShotUsage(Context context, MXBase.ScreenShotUsage usage, Long delaySeconds, BooleanCompletion completion) {
        MXHelper.INSTANCE.setScreenShotUsage(context, usage, delaySeconds, (success) -> {
            completion.onComplete(success);
            return Unit.INSTANCE;
        });
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @Keep
    public static void setPowerKeyMenuEnablePowerOffButton(Context context, Boolean enable, Long delaySeconds, BooleanCompletion completion) {
        MXHelper.INSTANCE.setPowerKeyMenuEnablePowerOffButton(context, enable, delaySeconds, (success) -> {
            completion.onComplete(success);
            return Unit.INSTANCE;
        });
    }

    @Keep
    public static void powerKeyTriggerAutoScreenLock(Context context, Boolean enable, Long delaySeconds, BooleanCompletion completion) {
        MXHelper.INSTANCE.powerKeyTriggerAutoScreenLock(context, enable, delaySeconds, (success) -> {
            completion.onComplete(success);
            return Unit.INSTANCE;
        });
    }

    @Keep
    public static void powerKeyAutoScreenLockSettingsOptionEnable(Context context, Boolean enable, Long delaySeconds, BooleanCompletion completion) {
        MXHelper.INSTANCE.powerKeyAutoScreenLockSettingsOptionEnable(context, enable, delaySeconds, (success) -> {
            completion.onComplete(success);
            return Unit.INSTANCE;
        });
    }

    @Keep
    public static void fetchSerialNumber(Context context, StringCompletion completion) {
        MXHelper.INSTANCE.fetchSerialNumber(context, 0, (serialNumber) -> {
            completion.onComplete(serialNumber);
            return Unit.INSTANCE;
        });
    }

    @Keep
    public static void fetchPPID(Context context, Boolean isDevDevice, StringCompletion completion) {
        MXHelper.INSTANCE.fetchPPID(context, isDevDevice, 0, (ppid) -> {
            completion.onComplete(ppid);
            return Unit.INSTANCE;
        });
    }

    @Keep
    public static void fetchIMEI(Context context, Long delaySeconds, StringCompletion completion) {
        MXHelper.INSTANCE.fetchIMEI(context, delaySeconds, (imei) -> {
            completion.onComplete(imei);
            return Unit.INSTANCE;
        });
    }

    @Keep
    public static void setKeyMappingToSendIntent(Context context, MXBase.KeyIdentifiers keyIdentifier, String intentAction, String intentCategory, Long delaySeconds, BooleanCompletion completion) {
        MXHelper.INSTANCE.setKeyMappingToSendIntent(context, keyIdentifier, intentAction, intentCategory, delaySeconds, (success) -> {
            completion.onComplete(success);
            return Unit.INSTANCE;
        });
    }

    @Keep
    public static void setKeyMappingToDefault(Context context, Long delaySeconds, BooleanCompletion completion) {
        MXHelper.INSTANCE.setKeyMappingToDefault(context, delaySeconds, (success) -> {
            completion.onComplete(success);
            return Unit.INSTANCE;
        });
    }
}
