package com.zebra.emdk_kotlin_wrapper.java_interface;

import android.content.Context;

import androidx.annotation.Keep;

import com.zebra.emdk_kotlin_wrapper.emdk.EMDKHelper;
import kotlin.Unit;

@Keep
public class EMDKHelperJava {

    @Keep
    public static void prepare(Context context, BooleanCompletion completion) {
        EMDKHelper.Static.getShared().prepare(context, (success) -> {
            completion.onComplete(success);
            return Unit.INSTANCE;
        });
    }

    @Keep
    public static void teardown() {
        EMDKHelper.Static.getShared().teardown();
    }

    @Keep
    public static String getEMDKVersion() {
        return EMDKHelper.Static.getShared().getEMDKVersion();
    }

    @Keep
    public static String getMXVersion() {
        return EMDKHelper.Static.getShared().getMXVersion();
    }

    @Keep
    public static String getDWVersion() {
        return EMDKHelper.Static.getShared().getDWVersion();
    }
}
