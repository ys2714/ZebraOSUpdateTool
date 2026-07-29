package com.zebra.emdk_kotlin_wrapper.utils

import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.Keep
import org.json.JSONArray
import org.json.JSONObject

@Keep
object JsonUtils {

    @Keep
    fun bundleToJson(bundle: Bundle): String {
        return toJsonObject(bundle).toString(4)
    }

    private fun toJsonObject(bundle: Bundle): JSONObject {
        val json = JSONObject()
        for (key in bundle.keySet()) {
            when (val value = bundle.get(key)) {
                is Bundle -> json.put(key, toJsonObject(value))
                is List<*> -> {
                    val jsonArray = JSONArray()
                    for (item in value) {
                        if (item is Bundle) {
                            jsonArray.put(toJsonObject(item))
                        } else {
                            jsonArray.put(item)
                        }
                    }
                    json.put(key, jsonArray)
                }
                is Array<*> -> {
                    val jsonArray = JSONArray()
                    for (item in value) {
                        if (item is Bundle) {
                            jsonArray.put(toJsonObject(item))
                        } else {
                            jsonArray.put(item)
                        }
                    }
                    json.put(key, jsonArray)
                }
                else -> json.put(key, value)
            }
        }
        return json
    }

    @Keep
    fun jsonToBundle(jsonString: String): Bundle {
        val jsonObj = JSONObject(jsonString)
        val bundle = toBundle(jsonObj)
        return bundle
    }

    private fun toBundle(jsonObject: JSONObject): Bundle {
        val bundle = Bundle()
        for (key in jsonObject.keys()) {
            when (val value = jsonObject.get(key)) {
                is JSONObject -> bundle.putBundle(key, toBundle(value))
                is JSONArray -> {
                    val list = value
                    if (list.length() > 0) {
                        when (list.get(0)) {
                            is JSONObject -> {
                                val parcelables = arrayListOf<Parcelable>()
                                for (i in 0 until list.length()) {
                                    val item = list.get(i) as JSONObject
                                    val bundle = toBundle(item)
                                    parcelables.add(bundle)
                                }
                                bundle.putParcelableArrayList(key, parcelables)
                            }
                            is String -> {
                                val strings = arrayListOf<String>()
                                for (i in 0 until list.length()) {
                                    val item = list.get(i) as String
                                    strings.add(item)
                                }
                                bundle.putStringArrayList(key, strings)
                            }
                        }
                    }
                }
                is String -> bundle.putString(key, value)
                is Boolean -> bundle.putBoolean(key, value)
                is Int -> bundle.putInt(key, value)
                is Long -> bundle.putLong(key, value)
                is Double -> bundle.putDouble(key, value)
            }
        }
        return bundle
    }
}

internal fun String.trimNewLines(): String {
    return this.replace("\n", "").replace("\r", "")
}

internal fun String.trimSpace(): String {
    return this.replace(Regex("\\s+"), " ")
}

internal fun String.compressStringByTrimAll(): String {
    return this.trim().trimIndent().trimNewLines().trimSpace()
}

internal fun String.compressStringByTrim(): String {
    return this.trim().trimIndent().trimNewLines()
}
