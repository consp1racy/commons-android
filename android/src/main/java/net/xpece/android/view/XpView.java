package net.xpece.android.view;

import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.EdgeEffect;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import net.xpece.android.AndroidUtils;
import net.xpece.commons.android.BuildConfig;
import net.xpece.commons.android.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;

/**
 * Created by pechanecjr on 4. 1. 2015.
 */
@TargetApi(21)
public class XpView {
    protected XpView() {}

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressWarnings("deprecation")
    public static void setBackground(View v, Drawable d) {
        if (Build.VERSION.SDK_INT < 16) {
            v.setBackgroundDrawable(d);
        } else {
            v.setBackground(d);
        }
    }

    /**
     * @return Returns true this ScrollView can be scrolled
     */
    public static boolean canScroll(ScrollView scroll) {
        View child = scroll.getChildAt(0);
        if (child != null) {
            int childHeight = child.getHeight();
            return scroll.getHeight() < childHeight + scroll.getPaddingTop() + scroll.getPaddingBottom();
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressWarnings("deprecation")
    public static void removeOnGlobalLayoutListener(View v, ViewTreeObserver.OnGlobalLayoutListener l) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            v.getViewTreeObserver().removeGlobalOnLayoutListener(l);
        } else {
            v.getViewTreeObserver().removeOnGlobalLayoutListener(l);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void setSearchViewLayoutTransition(android.widget.SearchView view) {
        if (!AndroidUtils.API_11) return;
        int searchBarId = view.getContext().getResources().getIdentifier("android:id/search_bar", null, null);
        LinearLayout searchBar = (LinearLayout) view.findViewById(searchBarId);
        searchBar.setLayoutTransition(new LayoutTransition());
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void setSearchViewLayoutTransition(android.support.v7.widget.SearchView view) {
        if (!AndroidUtils.API_11) return;
        LinearLayout searchBar = (LinearLayout) view.findViewById(R.id.search_bar);
        searchBar.setLayoutTransition(new LayoutTransition());
    }

    private static final Class<ScrollView> CLASS_SCROLL_VIEW = ScrollView.class;
    private static final Field SCROLL_VIEW_FIELD_EDGE_GLOW_TOP;
    private static final Field SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM;

    private static final Class<AbsListView> CLASS_LIST_VIEW = AbsListView.class;
    private static final Field LIST_VIEW_FIELD_EDGE_GLOW_TOP;
    private static final Field LIST_VIEW_FIELD_EDGE_GLOW_BOTTOM;

    private static final Field EDGE_EFFECT_FIELD_EDGE;
    private static final Field EDGE_EFFECT_FIELD_GLOW;

    static {
        Field edgeGlowTop = null, edgeGlowBottom = null;

        for (Field f : CLASS_SCROLL_VIEW.getDeclaredFields()) {
            switch (f.getName()) {
                case "mEdgeGlowTop":
                    f.setAccessible(true);
                    edgeGlowTop = f;
                    break;
                case "mEdgeGlowBottom":
                    f.setAccessible(true);
                    edgeGlowBottom = f;
                    break;
            }
        }

        SCROLL_VIEW_FIELD_EDGE_GLOW_TOP = edgeGlowTop;
        SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM = edgeGlowBottom;

        for (Field f : CLASS_LIST_VIEW.getDeclaredFields()) {
            switch (f.getName()) {
                case "mEdgeGlowTop":
                    f.setAccessible(true);
                    edgeGlowTop = f;
                    break;
                case "mEdgeGlowBottom":
                    f.setAccessible(true);
                    edgeGlowBottom = f;
                    break;
            }
        }

        LIST_VIEW_FIELD_EDGE_GLOW_TOP = edgeGlowTop;
        LIST_VIEW_FIELD_EDGE_GLOW_BOTTOM = edgeGlowBottom;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Field edge = null, glow = null;

            Class cls = null;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                try {
                    cls = Class.forName("android.widget.EdgeGlow");
                } catch (ClassNotFoundException e) {
                    if (BuildConfig.DEBUG) e.printStackTrace();
                }
            } else {
                cls = EdgeEffect.class;
            }

            if (cls != null) {
                for (Field f : cls.getDeclaredFields()) {
                    switch (f.getName()) {
                        case "mEdge":
                            f.setAccessible(true);
                            edge = f;
                            break;
                        case "mGlow":
                            f.setAccessible(true);
                            glow = f;
                            break;
                    }
                }
            }

            EDGE_EFFECT_FIELD_EDGE = edge;
            EDGE_EFFECT_FIELD_GLOW = glow;
        } else {
            EDGE_EFFECT_FIELD_EDGE = null;
            EDGE_EFFECT_FIELD_GLOW = null;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface EdgeGlowColorApi {}

    @XpView.EdgeGlowColorApi
    public static final int ALWAYS = 0;
    @XpView.EdgeGlowColorApi
    public static final int PRE_HONEYCOMB = Build.VERSION_CODES.HONEYCOMB;
    @XpView.EdgeGlowColorApi
    public static final int PRE_KITKAT = Build.VERSION_CODES.KITKAT;
    @XpView.EdgeGlowColorApi
    public static final int PRE_LOLLIPOP = Build.VERSION_CODES.LOLLIPOP;

    public static void setEdgeGlowColor(AbsListView listView, int color, @EdgeGlowColorApi int when) {
        if (Build.VERSION.SDK_INT < when || when == 0) {
            setEdgeGlowColor(listView, color);
        }
    }

    public static void setEdgeGlowColor(AbsListView listView, int color) {
        try {
            Object ee;
            ee = LIST_VIEW_FIELD_EDGE_GLOW_TOP.get(listView);
            setEdgeGlowColor(ee, color);
            ee = LIST_VIEW_FIELD_EDGE_GLOW_BOTTOM.get(listView);
            setEdgeGlowColor(ee, color);
        } catch (Exception ex) {
            if (BuildConfig.DEBUG) ex.printStackTrace();
        }
    }

    public static void setEdgeGlowColor(ScrollView scrollView, int color, @EdgeGlowColorApi int when) {
        if (Build.VERSION.SDK_INT < when || when == 0) {
            setEdgeGlowColor(scrollView, color);
        }
    }

    public static void setEdgeGlowColor(ScrollView scrollView, int color) {
        try {
            Object ee;
            ee = SCROLL_VIEW_FIELD_EDGE_GLOW_TOP.get(scrollView);
            setEdgeGlowColor(ee, color);
            ee = SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM.get(scrollView);
            setEdgeGlowColor(ee, color);
        } catch (Exception ex) {
            if (BuildConfig.DEBUG) ex.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void setEdgeGlowColor(Object edgeEffect, int color) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            try {
                final Drawable mEdge = (Drawable) EDGE_EFFECT_FIELD_EDGE.get(edgeEffect);
                final Drawable mGlow = (Drawable) EDGE_EFFECT_FIELD_GLOW.get(edgeEffect);
                mEdge.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                mGlow.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                mEdge.setCallback(null); // free up any references
                mGlow.setCallback(null); // free up any references
            } catch (Exception ex) {
                if (BuildConfig.DEBUG) ex.printStackTrace();
            }
        } else {
            ((EdgeEffect) edgeEffect).setColor(color);
        }
    }
}
