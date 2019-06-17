package com.cliqz.components.search

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import org.mozilla.reference.browser.ext.components

/**
 * @author Sam Macbeth
 */
class FreshTab @JvmOverloads constructor (
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    val freshTab by lazy { context.components.cliqz.freshTab }

    init {
        if (freshTab.parent != null) {
            val parentView = freshTab.parent as FrameLayout
            parentView.removeView(freshTab)
        }

        addView(freshTab)
    }
}