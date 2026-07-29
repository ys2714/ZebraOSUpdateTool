package com.zebra.emdk_kotlin_wrapper.utils

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

internal object AssetsReader {

    private const val TAG = "AssetsReader"

    @Throws(RuntimeException::class)
    fun readFileToStringWithParams(context: Context,
                                   fileName: String,
                                   params: Map<String, String>?): String {
        val pmap = params ?: emptyMap()
        val command = readFileToString(
            context,
            fileName,
            ) { line ->
            var result: String? = null

            val prefix = "=\\[" // Escaped '['
            val suffix = "\\]"  // Escaped ']'
            val pattern = "$prefix(.*?)$suffix"

            val placeholderRegex = Regex(pattern)
            val placeholderResult = placeholderRegex.find(line)
            if (placeholderResult == null) {
                //normal static line
                result = line
            } else {
                //line with parametersDeviceAdminReceiver
                val placeholder = placeholderResult.value
                for (entity in pmap.entries) {
                    val key = entity.key
                    val value = entity.value
                    if (line.contains('"' + key + '"')) {
                        // find the param
                        if (line.contains(placeholder)) {
                            // has variable value
                            result = line.replace(placeholder, value)
                            Log.d(TAG, "replace $placeholder with $value in $fileName")
                        } else {
                            // has fixed default value
                            result = line
                        }
                        // already find the param, do not check other parameters
                        break
                    } else {
                        continue
                    }
                }
                //no input for the param, skip the line if it was not mandatory
                if (result == null) {
                    if (placeholder.uppercase() == placeholder) {
                        throw RuntimeException("mandatory parameter not filled: $placeholder")
                    }
                    if (Regex("(\\{|\\[)+\\s+\"").containsMatchIn(line)) {
                        throw RuntimeException("can not skip content before first delimiter: $line")
                    }
                    if (Regex("\"\\s+(\\}|\\])+").containsMatchIn(line)) {
                        throw RuntimeException("can not skip content after last delimiter: $line")
                    }
                    result = null
                }
            }
            // return the result line
            result
        }
        // check the parameters
        params?.forEach { (key, value) ->
            val placeholder = "=[$key]"
            if (command.contains(placeholder)) {
                throw RuntimeException("parameter: $placeholder in XML not filled correctly")
            }
        }
        return command
    }

    fun readFileToString(context: Context,
                         fileName: String,
                         lineProcessor: (String) -> String?
                         ): String {
        val stringBuilder = StringBuilder()
        val assetManager = context.assets
        val delimiter: Char
        if (fileName.contains(".xml")) {
            delimiter = '>'
        } else if (fileName.contains(".json")) {
            delimiter = ','
        } else {
            delimiter = '\n'
        }
        try {
            assetManager.open(fileName).use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { bufferedReader ->
                    val reader = bufferedReader.buffered()
                    var nextChar: Int = Int.MAX_VALUE
                    while (reader.read().also { nextChar = it } != -1) {
                        val lineBuilder = StringBuilder()
                            .append(nextChar.toChar())
                        while (reader.read().also { nextChar = it } != -1 && nextChar.toChar() != delimiter) {
                            lineBuilder.append(nextChar.toChar())
                        }
                        val cleanLine = lineBuilder.toString().compressStringByTrim()
                        lineProcessor(cleanLine)?.let {
                            if (!it.isEmpty()) {
                                stringBuilder
                                    .append(it)
                                    .append(delimiter)
                            }
                        }
                    }
                    Log.d(TAG, "LAST CHAR: $nextChar")
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error reading XML file from assets: $fileName", e)
        }
        if (delimiter == ',') {
            var result = stringBuilder.removeSuffix(delimiter.toString()).toString()
            result = result.replace(",}", "}")
            result = result.replace(",]", "]")
            result = result.replace(", }", "}")
            result = result.replace(", ]", "]")
            return result
        } else {
            var result = stringBuilder.toString()
            return result
        }
    }
}