package com.google.android.material.widget;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * Only used on Lollipop. Ripple over round rect mask has weird outline otherwise.
 */
@RequiresApi(21)
class XpRoundRectRippleDrawable extends RippleDrawable {
    private final float mCornerRadius;

    private final Path mForegroundClippingPath = new Path();

    /**
     * Creates a new ripple drawable with the specified ripple color and
     * optional content and mask drawables.
     *
     * @param color The ripple color
     * @param content The content drawable, may be {@code null}
     * @param mask The mask drawable, may be {@code null}
     */
    public XpRoundRectRippleDrawable(
        @NonNull final ColorStateList color, @Nullable final Drawable content,
        @Nullable final Drawable mask, final float cornerRadius) {
        super(color, content, mask);
        mCornerRadius = cornerRadius;
    }

    @Override
    protected void onBoundsChange(@NonNull final Rect bounds) {
        super.onBoundsChange(bounds);

        final Path path = mForegroundClippingPath;
        path.reset();
        path.addRoundRect(bounds.left, bounds.top, bounds.right, bounds.bottom, mCornerRadius, mCornerRadius, Path.Direction.CW);
    }

    @Override
    public void draw(@NonNull final Canvas canvas) {
        final int save = canvas.save();
        canvas.clipPath(mForegroundClippingPath);

        super.draw(canvas);

        canvas.restoreToCount(save);
    }
}
