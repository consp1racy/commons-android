@file:JvmName("XpResources")
@file:Suppress("NOTHING_TO_INLINE")

package net.xpece.android.content

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import androidx.annotation.*
import androidx.appcompat.content.res.AppCompatResources
import net.xpece.android.content.BaseResources.obtainStyledAttributes
import net.xpece.android.content.BaseResources.resolveResourceId as resolveResourceIdImpl

/**
 * Retrieve the resource identifier from a styled attribute in this Context's theme.
 *
 * Example:
 *
 *     val resId = context.resolveResourceId(
 *         android.R.attr.textColorPrimary,
 *         R.color.defaultColor
 *     )
 *     val color = context.getColorCompat(resId)
 *
 * @param attr The desired attribute in the style.
 * @param fallback Identifier to return if the attribute is not defined or not a resource.
 *
 * @see android.content.Context.obtainStyledAttributes
 * @see android.content.res.TypedArray.getResourceId
 */
@AnyRes
inline fun Context.resolveResourceId(@AttrRes attr: Int, @AnyRes fallback: Int): Int =
    resolveResourceIdImpl(0, attr, fallback)

/**
 * Retrieve the resource identifier from a styled attribute in this Context's theme.
 *
 * Example:
 *
 *     val resId = context.resolveResourceId(
 *         R.style.SomeTextAppearance,
 *         android.R.attr.textColorPrimary,
 *         R.color.defaultColor
 *     )
 *     val color = context.getColorCompat(resId)
 *
 * @param style The desired style resource.
 * @param attr The desired attribute in the style.
 * @param fallback Identifier to return if the attribute is not defined or not a resource.
 *
 * @see android.content.Context.obtainStyledAttributes
 * @see android.content.res.TypedArray.getResourceId
 */
@AnyRes
inline fun Context.resolveResourceId(
    @StyleRes style: Int,
    @AttrRes attr: Int,
    @AnyRes fallback: Int
): Int = resolveResourceIdImpl(style, attr, fallback)

/**
 * Returns the color integer from the given resource.
 * The resource can include themeable attributes, regardless of API level.
 *
 * @receiver context to inflate against
 * @param resId the resource identifier of the color to retrieve
 *
 * @throws Resources.NotFoundException
 * @see AppCompatResources.getColorStateList
 * @see ColorStateList.getDefaultColor
 */
@ColorInt
inline fun Context.getColorCompat(@ColorRes resId: Int): Int =
    getColorStateListCompat(resId).defaultColor

/**
 * Returns the [ColorStateList] from the given resource.
 * The resource can include themeable attributes, regardless of API level.
 *
 * @receiver context to inflate against
 * @param resId the resource identifier of the [ColorStateList] to retrieve
 *
 * @throws Resources.NotFoundException
 * @see AppCompatResources.getColorStateList
 */
fun Context.getColorStateListCompat(@ColorRes resId: Int): ColorStateList =
    AppCompatResources.getColorStateList(this, resId)
        ?: throw Resources.NotFoundException("0x" + String.format("%08X", resId))

/**
 * Return a drawable object associated with a particular resource ID.
 *
 * This method supports inflation of `<vector>`, `<animated-vector>` and
 * `<animated-selector>` resources on devices where platform support is not available.
 * The resource can include themeable attributes, regardless of API level.
 *
 * @receiver context to inflate against
 * @param resId   The desired resource identifier, as generated by the aapt
 *                tool. This integer encodes the package, type, and resource
 *                entry. The value 0 is an invalid identifier.
 * @return An object that can be used to draw this resource.
 * @see AppCompatResources.getDrawable
 */
fun Context.getDrawableCompat(@DrawableRes resId: Int): Drawable? {
    @Suppress("DEPRECATION")
    if (DrawableResolver.isDrawableResolversEnabled) {
        DrawableResolver.drawableResolvers.forEach {
            val d = it.getDrawable(this, resId)
            if (d != null) return d
        }
    }
    return AppCompatResources.getDrawable(this, resId)
}

/**
 * Retrieve the floating-point value from a styled attribute in this Context's theme.
 *
 * @param attr The desired attribute in the style.
 * @param fallback Value to return if the attribute is not defined or not a floating-point value.
 *
 * @see android.content.Context.obtainStyledAttributes
 * @see android.content.res.TypedArray.getFloat
 */
inline fun Context.resolveFloat(@AttrRes attr: Int, fallback: Float = 0F): Float =
    resolveFloat(0, attr, fallback)

/**
 * Retrieve the boolean value from a styled attribute in this Context's theme.
 *
 * @param attr The desired attribute in the style.
 * @param fallback Value to return if the attribute is not defined or not a boolean value.
 *
 * @see android.content.Context.obtainStyledAttributes
 * @see android.content.res.TypedArray.getBoolean
 */
inline fun Context.resolveBoolean(@AttrRes attr: Int, fallback: Boolean = false): Boolean =
    resolveBoolean(0, attr, fallback)

/**
 * Retrieve the color integer from a styled attribute in this Context's theme.
 *
 * The resource can include themeable attributes, regardless of API level.
 *
 * @param attr The desired attribute in the style.
 * @param fallback Value to return if the attribute is not defined or not a color.
 *
 * @see android.content.Context.obtainStyledAttributes
 * @see android.content.res.TypedArray.getColor
 */
@ColorInt
inline fun Context.resolveColor(@AttrRes attr: Int, @ColorInt fallback: Int = 0): Int =
    resolveColor(0, attr, fallback)

/**
 * Retrieve the [ColorStateList] from a styled attribute in this Context's theme.
 *
 * The resource can include themeable attributes, regardless of API level.
 *
 * @param attr The desired attribute in the style.
 * @return `null` if the attribute is not defined or not a color.
 *
 * @see android.content.Context.obtainStyledAttributes
 * @see android.content.res.TypedArray.getColorStateList
 */
inline fun Context.resolveColorStateList(@AttrRes attr: Int): ColorStateList? =
    resolveColorStateList(0, attr)

/**
 * Retrieve the dimensional from a styled attribute in this Context's theme.
 *
 * @param attr The desired attribute in the style.
 * @param fallback Value to return if the attribute is not defined or not a dimensional.
 * @return Resource dimension value multiplied by the appropriate metric to convert to pixels.
 *
 * @see android.content.Context.obtainStyledAttributes
 * @see android.content.res.TypedArray.getDimension
 */
inline fun Context.resolveDimension(@AttrRes attr: Int, fallback: Float = 0F): Float =
    resolveDimension(0, attr, fallback)

/**
 * Retrieve the dimensional from a styled attribute in this Context's theme
 * for use as an offset in raw pixels. This is the same as [resolveDimension],
 * except the returned value is converted to integer pixels for you.
 * An offset conversion involves simply truncating the base value to an integer.
 *
 * @param attr The desired attribute in the style.
 * @param fallback Value to return if the attribute is not defined or not a dimensional.
 * @return Resource dimension value multiplied by the appropriate metric
 *         and truncated to integer pixels.
 *
 * @see android.content.Context.obtainStyledAttributes
 * @see android.content.res.TypedArray.getDimensionPixelOffset
 */
inline fun Context.resolveDimensionPixelOffset(@AttrRes attr: Int, fallback: Int = 0): Int =
    resolveDimensionPixelOffset(0, attr, fallback)

/**
 * Retrieve the dimensional from a styled attribute in this Context's theme
 * for use as a size in raw pixels. This is the same as [resolveDimension],
 * except the returned value is converted to integer pixels for use as a size.
 * A size conversion involves rounding the base value,
 * and ensuring that a non-zero base value is at least one pixel in size.
 *
 * @param attr The desired attribute in the style.
 * @param fallback Value to return if the attribute is not defined or not a dimensional.
 * @return Resource dimension value multiplied by the appropriate metric
 *         and rounded up to integer pixels.
 *
 * @see android.content.Context.obtainStyledAttributes
 * @see android.content.res.TypedArray.getDimensionPixelSize
 */
inline fun Context.resolveDimensionPixelSize(@AttrRes attr: Int, fallback: Int = 0): Int =
    resolveDimensionPixelSize(0, attr, fallback)

/**
 * Retrieve the [Drawable] from a styled attribute in this Context's theme.
 *
 * The resource can include themeable attributes, regardless of API level.
 *
 * @param attr The desired attribute in the style.
 * @return `null` if the attribute is not defined or not a drawable.
 *
 * @see android.content.Context.obtainStyledAttributes
 * @see android.content.res.TypedArray.getDrawable
 */
inline fun Context.resolveDrawable(@AttrRes attr: Int): Drawable? =
    resolveDrawable(0, attr)

/**
 * Retrieve the [String] from a styled attribute in this Context's theme.
 *
 * @param attr The desired attribute in the style.
 * @return `null` if the attribute is not defined or not a string.
 *
 * @see android.content.Context.obtainStyledAttributes
 * @see android.content.res.TypedArray.getString
 */
inline fun Context.resolveString(@AttrRes attr: Int): String? =
    resolveString(0, attr)

/**
 * Retrieve the [CharSequence] from a styled attribute in this Context's theme.
 *
 * @param attr The desired attribute in the style.
 * @return `null` if the attribute is not defined or not a string.
 *
 * @see android.content.Context.obtainStyledAttributes
 * @see android.content.res.TypedArray.getString
 */
inline fun Context.resolveText(@AttrRes attr: Int): CharSequence? =
    resolveText(0, attr)

/**
 * Retrieve the floating-point value from a styled attribute in this Context's theme.
 *
 * @param style The desired style resource.
 * @param attr The desired attribute in the style.
 * @param fallback Value to return if the attribute is not defined or not a floating-point value.
 *
 * @see android.content.Context.obtainStyledAttributes
 * @see android.content.res.TypedArray.getFloat
 */
fun Context.resolveFloat(@StyleRes style: Int, @AttrRes attr: Int, fallback: Float): Float {
    val ta = obtainStyledAttributes(style, attr)
    try {
        return ta.getFloat(0, fallback)
    } finally {
        ta.recycle()
    }
}

/**
 * Retrieve the boolean value from a styled attribute in this Context's theme.
 *
 * @param style The desired style resource.
 * @param attr The desired attribute in the style.
 * @param fallback Value to return if the attribute is not defined or not a boolean value.
 *
 * @see android.content.Context.obtainStyledAttributes
 * @see android.content.res.TypedArray.getBoolean
 */
fun Context.resolveBoolean(@StyleRes style: Int, @AttrRes attr: Int, fallback: Boolean): Boolean {
    val ta = obtainStyledAttributes(style, attr)
    try {
        return ta.getBoolean(0, fallback)
    } finally {
        ta.recycle()
    }
}

/**
 * Retrieve the color integer from a styled attribute in this Context's theme.
 *
 * The resource can include themeable attributes, regardless of API level.
 *
 * @param style The desired style resource.
 * @param attr The desired attribute in the style.
 * @param fallback Value to return if the attribute is not defined or not a color.
 *
 * @see android.content.Context.obtainStyledAttributes
 * @see android.content.res.TypedArray.getColor
 */
@ColorInt
fun Context.resolveColor(
    @StyleRes style: Int, @AttrRes attr: Int, @ColorInt fallback: Int = 0
): Int {
    val ta = obtainStyledAttributes(style, attr)
    try {
        return ta.getColorCompat(this, 0, fallback)
    } finally {
        ta.recycle()
    }
}

/**
 * Retrieve the [ColorStateList] from a styled attribute in this Context's theme.
 *
 * The resource can include themeable attributes, regardless of API level.
 *
 * @param style The desired style resource.
 * @param attr The desired attribute in the style.
 * @return `null` if the attribute is not defined or not a color.
 *
 * @see android.content.Context.obtainStyledAttributes
 * @see android.content.res.TypedArray.getColorStateList
 */
fun Context.resolveColorStateList(@StyleRes style: Int, @AttrRes attr: Int): ColorStateList? {
    val ta = obtainStyledAttributes(style, attr)
    try {
        return ta.getColorStateListCompat(this, 0)
    } finally {
        ta.recycle()
    }
}

/**
 * Retrieve the dimensional from a styled attribute in this Context's theme.
 *
 * @param style The desired style resource.
 * @param attr The desired attribute in the style.
 * @param fallback Value to return if the attribute is not defined or not a dimensional.
 * @return Resource dimension value multiplied by the appropriate metric to convert to pixels.
 *
 * @see android.content.Context.obtainStyledAttributes
 * @see android.content.res.TypedArray.getDimension
 */
fun Context.resolveDimension(
    @StyleRes style: Int, @AttrRes attr: Int, fallback: Float = 0F
): Float {
    val ta = obtainStyledAttributes(style, attr)
    try {
        return ta.getDimension(0, fallback)
    } finally {
        ta.recycle()
    }
}

/**
 * Retrieve the dimensional from a styled attribute in this Context's theme
 * for use as an offset in raw pixels. This is the same as [resolveDimension],
 * except the returned value is converted to integer pixels for you.
 * An offset conversion involves simply truncating the base value to an integer.
 *
 * @param style The desired style resource.
 * @param attr The desired attribute in the style.
 * @param fallback Value to return if the attribute is not defined or not a dimensional.
 * @return Resource dimension value multiplied by the appropriate metric
 *         and truncated to integer pixels.
 *
 * @see android.content.Context.obtainStyledAttributes
 * @see android.content.res.TypedArray.getDimensionPixelOffset
 */
fun Context.resolveDimensionPixelOffset(
    @StyleRes style: Int, @AttrRes attr: Int, fallback: Int = 0
): Int {
    val ta = obtainStyledAttributes(style, attr)
    try {
        return ta.getDimensionPixelOffset(0, fallback)
    } finally {
        ta.recycle()
    }
}

/**
 * Retrieve the dimensional from a styled attribute in this Context's theme
 * for use as a size in raw pixels. This is the same as [resolveDimension],
 * except the returned value is converted to integer pixels for use as a size.
 * A size conversion involves rounding the base value,
 * and ensuring that a non-zero base value is at least one pixel in size.
 *
 * @param style The desired style resource.
 * @param attr The desired attribute in the style.
 * @param fallback Value to return if the attribute is not defined or not a dimensional.
 * @return Resource dimension value multiplied by the appropriate metric
 *         and rounded up to integer pixels.
 *
 * @see android.content.Context.obtainStyledAttributes
 * @see android.content.res.TypedArray.getDimensionPixelSize
 */
fun Context.resolveDimensionPixelSize(
    @StyleRes style: Int, @AttrRes attr: Int, fallback: Int = 0
): Int {
    val ta = obtainStyledAttributes(style, attr)
    try {
        return ta.getDimensionPixelSize(0, fallback)
    } finally {
        ta.recycle()
    }
}

/**
 * Retrieve the [Drawable] from a styled attribute in this Context's theme.
 *
 * The resource can include themeable attributes, regardless of API level.
 *
 * @param style The desired style resource.
 * @param attr The desired attribute in the style.
 * @return `null` if the attribute is not defined or not a drawable.
 *
 * @see android.content.Context.obtainStyledAttributes
 * @see android.content.res.TypedArray.getDrawable
 */
fun Context.resolveDrawable(@StyleRes style: Int, @AttrRes attr: Int): Drawable? {
    val ta = obtainStyledAttributes(style, attr)
    try {
        return ta.getDrawableCompat(this, 0)
    } finally {
        ta.recycle()
    }
}

/**
 * Retrieve the [String] from a styled attribute in this Context's theme.
 *
 * @param style The desired style resource.
 * @param attr The desired attribute in the style.
 * @return `null` if the attribute is not defined or not a string.
 *
 * @see android.content.Context.obtainStyledAttributes
 * @see android.content.res.TypedArray.getString
 */
fun Context.resolveString(@StyleRes style: Int, @AttrRes attr: Int): String? {
    val ta = obtainStyledAttributes(style, attr)
    try {
        val resId = ta.getResourceId(0, 0)
        if (resId != 0) {
            // It's a reference, let Context handle it.
            return getString(resId)
        } else {
            // It's in-place, obtain it form TypedArray.
            return ta.getString(0)
        }
    } finally {
        ta.recycle()
    }
}

/**
 * Retrieve the [CharSequence] from a styled attribute in this Context's theme.
 *
 * @param style The desired style resource.
 * @param attr The desired attribute in the style.
 * @return `null` if the attribute is not defined or not a string.
 *
 * @see android.content.Context.obtainStyledAttributes
 * @see android.content.res.TypedArray.getText
 */
fun Context.resolveText(@StyleRes style: Int, @AttrRes attr: Int): CharSequence? {
    val ta = obtainStyledAttributes(style, attr)
    try {
        val resId = ta.getResourceId(0, 0)
        if (resId != 0) {
            // It's a reference, let Context handle it.
            return getText(resId)
        } else {
            // It's in-place, obtain it form TypedArray.
            return ta.getText(0)
        }
    } finally {
        ta.recycle()
    }
}

/**
 * Retrieve the Drawable for the attribute at [index].
 *
 * This method will throw an exception if the attribute is defined but is
 * not a color or drawable resource.
 *
 * @param index Index of attribute to retrieve.
 *
 * @return Drawable for the attribute, or `null` if not defined.
 * @throws RuntimeException if the TypedArray has already been recycled.
 * @throws UnsupportedOperationException if the attribute is defined but is
 *         not a color or drawable resource.
 * @see TypedArray.getDrawable
 * @see AppCompatResources.getDrawable
 */
fun TypedArray.getDrawableCompat(context: Context, @StyleableRes index: Int): Drawable? {
    val resId = getResourceId(index, 0)
    return if (resId != 0) {
        // It's a reference, let Context handle it.
        context.getDrawableCompat(resId)
    } else {
        // It's in-place, obtain it form TypedArray. (Maybe a color int?)
        getDrawable(index)
    }
}

/**
 * Retrieve the ColorStateList for the attribute at [index].
 * The value may be either a single solid color or a reference to
 * a color or complex [ColorStateList] description.
 *
 * The resource can include themeable attributes, regardless of API level.
 *
 * This method will return `null` if the attribute is not defined or
 * is not an integer color or color state list.
 *
 * @param index Index of attribute to retrieve.
 *
 * @return ColorStateList for the attribute, or `null` if not defined.
 * @throws RuntimeException if the TypedArray has already been recycled.
 * @throws UnsupportedOperationException if the attribute is defined but is
 *         not an integer color or color state list.
 * @see TypedArray.getColor
 * @see AppCompatResources.getColorStateList
 */
fun TypedArray.getColorStateListCompat(
    context: Context,
    @StyleableRes index: Int
): ColorStateList? {
    val resId = getResourceId(index, 0)
    return if (resId != 0) {
        // It's a reference, let Context handle it.
        context.getColorStateListCompat(resId)
    } else {
        // It's in-place, obtain it form TypedArray. (Maybe a color int?)
        getColorStateList(index)
    }
}

/**
 * Retrieve the color value for the attribute at [index].
 * If the attribute references a color resource holding a complex [ColorStateList],
 * then the default color from the set is returned.
 *
 * The resource can include themeable attributes, regardless of API level.
 *
 * This method will throw an exception if the attribute is defined but is
 * not an integer color or color state list.
 *
 * @param index Index of attribute to retrieve.
 * @param defValue Value to return if the attribute is not defined or
 *                 not a resource.
 *
 * @return Attribute color value, or defValue if not defined.
 * @throws RuntimeException if the TypedArray has already been recycled.
 * @throws UnsupportedOperationException if the attribute is defined but is
 *         not an integer color or color state list.
 * @see TypedArray.getColor
 * @see AppCompatResources.getColorStateList
 */
@ColorInt
fun TypedArray.getColorCompat(
    context: Context,
    @StyleableRes index: Int,
    @ColorInt defValue: Int
): Int {
    val resId = getResourceId(index, 0)
    return if (resId != 0) {
        // It's a reference, let Context handle it.
        context.getColorCompat(resId)
    } else {
        // It's in-place, obtain it form TypedArray. (Maybe a color int?)
        getColor(index, defValue)
    }
}
