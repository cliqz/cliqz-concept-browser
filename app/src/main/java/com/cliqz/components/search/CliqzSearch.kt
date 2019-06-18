package com.cliqz.components.search

import android.app.Activity
import android.content.Context
import com.cliqz.components.search.react.ReactHost
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactRootView
import com.facebook.react.bridge.WritableMap
import com.facebook.react.bridge.WritableNativeMap
import mozilla.components.browser.toolbar.BrowserToolbar
import org.mozilla.reference.browser.R

class CliqzSearch(private val context: Context, private val cliqz: Cliqz, private val reactInstanceManager: ReactInstanceManager) {
    internal var onClickListener: (() -> Unit)? = null
    val view by lazy {
        val view = ReactRootView(context)
        view.startReactApplication(reactInstanceManager, "BrowserCoreApp", null)
        view
    }

    fun startSearch(query : String) {
        cliqz.callAction("search", "startSearch", query, WritableNativeMap(), getSearchSenderArgument())
    }

    private fun getSearchSenderArgument() : WritableMap {
        val sender = WritableNativeMap();
        sender.putString("contextId", "mobile-cards")
        return sender
    }

}