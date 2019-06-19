package com.cliqz.components.search

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import org.mozilla.reference.browser.ext.components

/**
 * @author Sam Macbeth
 */
class FreshTab @JvmOverloads constructor (
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LazyReactView(context, attrs, defStyleAttr) {
    override val reactView by lazy { context.components.cliqz.freshTab }
}