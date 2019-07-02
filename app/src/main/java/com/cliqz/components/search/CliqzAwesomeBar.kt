package com.cliqz.components.search

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import mozilla.components.concept.awesomebar.AwesomeBar
import org.mozilla.reference.browser.ext.components

class CliqzAwesomeBar @JvmOverloads constructor (
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LazyReactView(context, attrs, defStyleAttr), AwesomeBar  {
    val search by lazy { context.components.cliqz.search }
    override val reactView by lazy { search.view }

    override fun addProviders(vararg providers: AwesomeBar.SuggestionProvider) {
    }

    override fun onInputChanged(text: String) {
        search.startSearch(text)
    }

    override fun removeAllProviders() {
    }

    override fun removeProviders(vararg providers: AwesomeBar.SuggestionProvider) {
    }

    override fun setOnStopListener(listener: () -> Unit) {
        context.components.cliqz.search.onClickListener = listener
    }
}