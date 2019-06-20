package com.cliqz.components.search

import android.app.Activity
import android.content.Context
import android.view.View
import com.cliqz.components.search.react.ReactHost
import com.facebook.react.ReactRootView
import mozilla.components.browser.session.Session
import mozilla.components.browser.session.SessionManager
import mozilla.components.browser.toolbar.BrowserToolbar
import org.mozilla.reference.browser.R
import org.mozilla.reference.browser.ext.components

/**
 * @author Sam Macbeth
 */
class Cliqz(private val context: Context) {

    private val reactHost by lazy { ReactHost(context) }
    private val reactInstanceManager by lazy { reactHost.reactInstanceManager }

    val search by lazy { CliqzSearch(context, this, reactInstanceManager) }
    val freshTab by lazy {
        val view = ReactRootView(context)
        view.startReactApplication(reactInstanceManager, "FreshTabApp", null)
        view
    }

    enum class State { FRESHTAB, SEARCH, BROWSING }

    var toolbar: BrowserToolbar? = null

    fun callAction(module: String, action: String, vararg args: Any) {
        return reactHost.callAction(module, action, *args)
    }

    fun onResume(activity: Activity) {
        reactInstanceManager.onHostResume(activity);
        val reactContext = reactInstanceManager.getCurrentReactContext()
        reactContext?.onHostResume(activity)

        toolbar = activity.findViewById(R.id.toolbar)
    }

    fun showDevOptionsDialog() {
        reactInstanceManager.showDevOptionsDialog()
    }

}