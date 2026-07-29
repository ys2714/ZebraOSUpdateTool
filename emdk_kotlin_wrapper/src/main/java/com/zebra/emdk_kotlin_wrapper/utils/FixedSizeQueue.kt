package com.zebra.emdk_kotlin_wrapper.utils

import android.os.Build
import androidx.annotation.Keep

@Keep
interface FixedSizeQueueItem {
    fun getID(): String
    fun onDisposal()
}

internal class FixedSizeQueue<T: FixedSizeQueueItem>(val size: Int) {

    private val _items: ArrayList<T> = arrayListOf<T>()

    val items: List<T>
        get() {
            return _items
        }

    val count: Int
        get() {
            return items.size
        }

    init {
        if (size <= 0) {
            throw IllegalArgumentException("size must be greater than 0")
        }
    }

    fun enqueue(item: T) {
        val first = _items.find { it.getID() == item.getID() }
        if (first != null) {
            _items.remove(first)
            _items.add(item)
            first.onDisposal()
        } else {
            if (_items.size < size) {
                _items.add(item)
            } else {
                val first = _items.removeAt(0)
                first.onDisposal()
                _items.add(item)
            }
        }
    }

    fun dequeue(): T {
        if (_items.isEmpty()) {
            throw RuntimeException("queue is empty")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            return _items.removeFirst()
        } else {
            val first = _items.first()
            _items.remove(first)
            return first
        }
    }

    fun remove(item: T) {
        if (_items.contains(item)) {
            item.onDisposal()
            _items.remove(item)
        }
    }
}