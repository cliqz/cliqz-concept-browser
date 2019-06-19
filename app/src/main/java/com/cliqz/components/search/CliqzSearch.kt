package com.cliqz.components.search

import android.app.Activity
import android.content.Context
import android.util.Log
import com.cliqz.components.search.react.ReactHost
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactRootView
import com.facebook.react.bridge.WritableMap
import com.facebook.react.bridge.WritableNativeMap
import mozilla.components.browser.session.Session
import mozilla.components.browser.session.SessionManager
import mozilla.components.browser.toolbar.BrowserToolbar
import org.mozilla.reference.browser.R
import org.mozilla.reference.browser.ext.components

class CliqzSearch(
        private val context: Context,
        private val cliqz: Cliqz,
        private val reactInstanceManager: ReactInstanceManager
) : SessionManager.Observer, Session.Observer {
    internal var onClickListener: (() -> Unit)? = null
    val view by lazy {
        val view = ReactRootView(context)
        view.startReactApplication(reactInstanceManager, "BrowserCoreApp", null)
        view
    }

    class Search(val query: String, val url: String, var loading: Boolean)

    val lastSearch = mutableMapOf<String, Search>()

    init {
        context.components.core.sessionManager.register(this)
        context.components.core.sessionManager.sessions.forEach { it.register(this) }
    }

    fun startSearch(query : String) {
        cliqz.callAction("search", "startSearch", query, WritableNativeMap(), getSearchSenderArgument())
    }

    private fun getSearchSenderArgument() : WritableMap {
        val sender = WritableNativeMap();
        sender.putString("contextId", "mobile-cards")
        return sender
    }

    fun dropdownSearchClick(query: String, url: String) {
        val session = context.components.core.sessionManager.selectedSession
        if (session != null) {
            // Link this query to session.url. This may not yet be the correct url (if the webview
            // didn't update yet, so we flag it as loading to catch it in the subsequent url change
            // event
            saveLastSearch(query, session.url, session.id, true)
        }
    }

    fun saveLastSearch(query: String, url: String, sessionId: String, loading: Boolean) {
        lastSearch[sessionId] = Search(query, url, loading)
        Log.d("CliqzSearch", "saveQuery: search=${query}, url=${url}, loading=${loading}")
    }

    fun clearLastSearch(session: Session) {
        lastSearch.remove(session.id)
        session.searchTerms = ""
    }

    override fun onSessionAdded(session: Session) {
        session.register(this)
    }

    override fun onSessionsRestored() {
        context.components.core.sessionManager.sessions.forEach { it.register(this) }
    }

    override fun onSessionRemoved(session: Session) {
        session.unregister(this)
    }

    override fun onUrlChanged(session: Session, url: String) {
        Log.d("CliqzSearch","onUrlChanged: search=${session.searchTerms}, url=${session.url}, loading=${session.loading}")
        val sessionSearch = lastSearch[session.id]
        // searchTerms: set by urlbar submission
        if (session.searchTerms != "") {
            saveLastSearch(session.searchTerms, session.url, session.id, false)
        } else if (sessionSearch != null && sessionSearch.loading) {
            // onUrlChanged event following a dropdownSearchClick event
            saveLastSearch(sessionSearch.query, url, session.id, false)
        }
    }
}
