package com.zebra.emdk_kotlin_wrapper.java_interface;

import android.content.Context;

import androidx.annotation.Keep;
import com.zebra.emdk_kotlin_wrapper.dw.DWAPI;
import com.zebra.emdk_kotlin_wrapper.dw.DWScannerMap;
import com.zebra.emdk_kotlin_wrapper.dw.DataWedgeHelper;

import java.util.List;
import kotlin.Unit;

@Keep
public class DWHelperJava {

    @Keep
    public static void addScanDataListener(DataWedgeHelper.ScanDataListener listener) {
        DataWedgeHelper.INSTANCE.addScanDataListener(listener);
    }

    @Keep
    public static void removeScanDataListener(DataWedgeHelper.ScanDataListener listener) {
        DataWedgeHelper.INSTANCE.removeScanDataListener(listener);
    }

    @Keep
    public static void prepare(Context context, BooleanCompletion callback) {
        DataWedgeHelper.INSTANCE.prepare(context, (success) -> {
            callback.onComplete(success);
            return Unit.INSTANCE;
        });
    }

    @Keep
    public static void checkDWStatus(Context context, BooleanCompletion callback) {
        DataWedgeHelper.INSTANCE.checkDWStatus(context, (enabled) -> {
            callback.onComplete(enabled);
            return Unit.INSTANCE;
        });
    }

    @Keep
    public static void enableDW(Context context, BooleanCompletion callback) {
        DataWedgeHelper.INSTANCE.enableDW(context, (success) -> {
            callback.onComplete(success);
            return Unit.INSTANCE;
        });
    }

    @Keep
    public static void disableDW(Context context, BooleanCompletion callback) {
        DataWedgeHelper.INSTANCE.disableDW(context, (success) -> {
            callback.onComplete(success);
            return Unit.INSTANCE;
        });
    }

    @Keep
    public static void createProfile(Context context, String name, BooleanCompletion callback) {
        DataWedgeHelper.INSTANCE.createProfile(context, name, (success) -> {
            callback.onComplete(success);
            return Unit.INSTANCE;
        });
    }

    @Keep
    public static void deleteProfile(Context context, String name, BooleanCompletion callback) {
        DataWedgeHelper.INSTANCE.deleteProfile(context, name, (success) -> {
            callback.onComplete(success);
            return Unit.INSTANCE;
        });
    }

    @Keep
    public static void bindProfileToApp(Context context, String name, String packageName, BooleanCompletion callback) {
        DataWedgeHelper.INSTANCE.bindProfileToApp(context, name, packageName, (success) -> {
            callback.onComplete(success);
            return Unit.INSTANCE;
        });
    }

    @Keep
    public static void configBarcodePlugin(Context context, String name, boolean enable, boolean hardTrigger, BooleanCompletion callback) {
        DataWedgeHelper.INSTANCE.configBarcodePlugin(context, name, enable, hardTrigger, (success) -> {
            callback.onComplete(success);
            return Unit.INSTANCE;
        });
    }

    @Keep
    public static void configKeystrokePlugin(Context context, String name, boolean enable, BooleanCompletion callback) {
        DataWedgeHelper.INSTANCE.configKeystrokePlugin(context, name, enable, (success) -> {
            callback.onComplete(success);
            return Unit.INSTANCE;
        });
    }

    @Keep
    public static void configIntentPlugin(Context context, String name, BooleanCompletion callback) {
        DataWedgeHelper.INSTANCE.configIntentPlugin(context, name, (success) -> {
            callback.onComplete(success);
            return Unit.INSTANCE;
        });
    }

    @Keep
    public static void softScanTrigger(Context context, DWAPI.SoftScanTriggerOptions option) {
        DataWedgeHelper.INSTANCE.softScanTrigger(context, option);
    }

    @Keep
    public static DWScannerMap.DWScannerInfo getScannerInfo(Context context, String id) {
        return DataWedgeHelper.INSTANCE.getScannerInfo(context, id);
    }

    @Keep
    public static void getScannerStatus(Context context, int delaySeconds, GeneralCompletion<DWAPI.ScannerStatus> callback) {
        DataWedgeHelper.INSTANCE.getScannerStatus(context, delaySeconds, (status) -> {
            callback.onComplete(status);
            return Unit.INSTANCE;
        });
    }

    @Keep
    public static void getScannerList(Context context, GeneralCompletion<List<DWScannerMap.DWScannerInfo>> callback) {
        DataWedgeHelper.INSTANCE.getScannerList(context, (list) -> {
            callback.onComplete(list);
            return Unit.INSTANCE;
        });
    }
}
