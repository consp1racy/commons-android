@file:JvmName("XpConfiguration")

package net.xpece.android.content.res

import android.content.res.Configuration
import android.os.Build
import androidx.core.text.TextUtilsCompat

@Suppress("DEPRECATION")
val Configuration.layoutDirectionCompat: Int
    get() = if (Build.VERSION.SDK_INT >= 17) {
        layoutDirection
    } else {
        TextUtilsCompat.getLayoutDirectionFromLocale(locale)
    }
