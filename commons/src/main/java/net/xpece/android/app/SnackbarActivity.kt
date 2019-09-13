package net.xpece.android.app

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.BaseTransientBottomBar
import androidx.core.graphics.drawable.DrawableCompat
import androidx.appcompat.content.res.AppCompatResources
import android.view.View

/**
 * Created by Eugen on 17.10.2016.
 */
interface SnackbarActivity {
    val snackbarParent : View

    var snackbar : Snackbar?

    fun showSnackbar(@StringRes textId: Int, @BaseTransientBottomBar.Duration duration: Int, @ColorRes backgroundId: Int? = null)  {
        val context = snackbarParent.context
        showSnackbar(context.getString(textId), duration, backgroundId)
    }

    fun showSnackbar(text: CharSequence, @BaseTransientBottomBar.Duration duration: Int, @ColorRes backgroundId: Int? = null) {
        val snackbar = Snackbar.make(snackbarParent, text, duration)
        if (backgroundId != null) {
            val view = snackbar.view
            val background = AppCompatResources.getColorStateList(view.context, backgroundId).defaultColor
            val d = DrawableCompat.wrap(view.background)
            DrawableCompat.setTint(d, background)
            @Suppress("DEPRECATION")
            view.setBackgroundDrawable(d)
        }
        this.snackbar = snackbar
        snackbar.show()
    }

    private fun hideSnackbar()  {
        snackbar?.dismiss()
    }

}
