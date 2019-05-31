package com.cliqz.components.search

import android.app.Activity
import android.content.Context
import com.cliqz.components.search.react.ReactHost
import com.facebook.react.ReactRootView
import com.facebook.react.bridge.WritableMap
import com.facebook.react.bridge.WritableNativeMap
import mozilla.components.browser.toolbar.BrowserToolbar
import org.mozilla.reference.browser.R

class CliqzSearch(private val context: Context) {
    internal var onClickListener: (() -> Unit)? = null
    val view by lazy {
        val view = ReactRootView(context)
        view.startReactApplication(reactInstanceManager, "BrowserCoreApp", null)
        view
    }

    private val reactHost by lazy { ReactHost(context) }

    private val reactInstanceManager by lazy { reactHost.reactInstanceManager }

    var toolbar: BrowserToolbar? = null

    fun startSearch(query : String) {
        reactHost.callAction("search", "startSearch", query, WritableNativeMap(), getSearchSenderArgument())
    }

    fun onResume(activity: Activity) {
        reactInstanceManager.onHostResume(activity);
        val reactContext = reactInstanceManager.getCurrentReactContext()
        reactContext?.onHostResume(activity)

        toolbar = activity.findViewById(R.id.toolbar)
    }

    private fun getSearchSenderArgument() : WritableMap {
        val sender = WritableNativeMap();
        sender.putString("contextId", "mobile-cards")
        return sender
    }

    fun showDevOptionsDialog() {
        reactHost.reactInstanceManager.showDevOptionsDialog()
    }
}