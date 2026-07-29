package com.zebra.emdk_kotlin_wrapper.java_interface;

import androidx.annotation.Keep;

@Keep
public interface GeneralCompletion<T> {
    void onComplete(T result);
}
