package net.xpece.android.content

import android.content.Context
import android.content.res.TypedArray
import androidx.annotation.AnyRes
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes

object BaseResources {

    private val TEMP_ARRAY = object : ThreadLocal<IntArray>() {
        override fun initialValue(): IntArray = intArrayOf(0)
    }

    @Suppress("NOTHING_TO_INLINE")
    @AnyRes
    inline fun Context.resolveResourceId(@AttrRes attr: Int, @AnyRes fallback: Int): Int =
            resolveResourceId(0, attr, fallback)

    @AnyRes
    fun Context.resolveResourceId(@StyleRes style: Int, @AttrRes attr: Int, @AnyRes fallback: Int): Int {
        val ta = obtainStyledAttributes(style, attr)
        try {
            return ta.getResourceId(0, fallback)
        } finally {
            ta.recycle()
        }
    }

    fun Context.obtainStyledAttributes(@StyleRes style: Int, @AttrRes attr: Int): TypedArray {
        val tempArray = TEMP_ARRAY.get()!!
        tempArray[0] = attr
        return obtainStyledAttributes(style, tempArray)
    }
}
