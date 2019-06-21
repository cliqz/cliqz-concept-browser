package com.cliqz.components.search

import android.view.View
import mozilla.components.browser.session.Session
import mozilla.components.browser.session.SessionManager
import mozilla.components.browser.toolbar.BrowserToolbar
import mozilla.components.concept.awesomebar.AwesomeBar
import mozilla.components.support.base.feature.BackHandler
import mozilla.components.support.base.feature.LifecycleAwareFeature
import org.mozilla.reference.browser.ext.components

/**
 * @author Sam Macbeth
 */
class CliqzFeature(
        val cliqz: Cliqz,
        private val sessionManager: SessionManager,
        val toolbar: BrowserToolbar,
        val search: AwesomeBar,
        private val freshTab: FreshTab
) : LifecycleAwareFeature, BackHandler, SessionManager.Observer, Session.Observer {

    val newTabURL = "about:blank"

    var urlBarActive = toolbar.isFocused || toolbar.isInEditMode
    var currentURL : String? = null
        get() = sessionManager.selectedSession?.url
    private val sessionSearchTerms = mutableMapOf<String, String>()

    init {
        toolbar.setOnEditFocusChangeListener { it ->
            urlBarActive = it || toolbar.isInEditMode
            updateState()
        }
        sessionManager.register(this)
        sessionManager.sessions.forEach { addSession(it) }
        updateState()
    }

    override fun start() {
    }

    override fun onBackPressed(): Boolean {
        if (sessionManager.selectedSession == null ) {
            return true
        }
        val session = sessionManager.selectedSession!!
        val lastSearch = cliqz.search.lastSearch
        // check if the last search for this session matches the landing url from the search
        if (session.id in lastSearch && lastSearch[session.id]!!.url == session.url) {
            session.searchTerms = lastSearch[session.id]!!.query
            toolbar.editMode()
            cliqz.search.clearLastSearch(session)
            return true
        }
        // session is finished, remove it (which triggers freshtab)
        if (!session.canGoBack) {
            sessionManager.remove(session)
            return true
        }
        return false
    }

    override fun stop() {
    }

    private fun addSession(session: Session) {
        session.register(this)
    }

    private fun updateState() {
        if (urlBarActive) {
            updateVisibility(freshTab, false)
        } else {
            var showFreshtab = currentURL == newTabURL || currentURL == null
            if (showFreshtab) {
                cliqz.sendEvent("NEW_TAB:SHOW")
                toolbar.url = ""
            }
            updateVisibility(freshTab, showFreshtab)
        }
    }

    private fun updateVisibility(view: View, shouldShow: Boolean): Boolean {
        if (shouldShow && view.visibility != View.VISIBLE) {
            view.visibility = View.VISIBLE
            return true
        } else if (!shouldShow && view.visibility != View.GONE) {
            view.visibility = View.GONE
            return true
        }
        return false
    }

    /**
    //     * The selection has changed and the given session is now the selected session.
    //     */
    override fun onSessionSelected(session: Session) {
        updateState()
    }

    /**
     * The given session has been added.
     */
    override fun onSessionAdded(session: Session) {
        updateState()
        addSession(session)
    }

    /**
     * Sessions have been restored via a snapshot. This callback is invoked at the end of the
     * call to <code>read</code>, after every session in the snapshot was added, and
     * appropriate session was selected.
     */
    override fun onSessionsRestored() {
        updateState()
        sessionManager.sessions.forEach { addSession(it) }
    }

    /**
     * The given session has been removed.
     */
    override fun onSessionRemoved(session: Session) {
        updateState()
        session.unregister(this)
        sessionSearchTerms.remove(session.id)
    }

    override fun onUrlChanged(session: Session, url: String) {
        if (sessionManager.selectedSession?.url == newTabURL) {
            toolbar.post { toolbar.url = "" }
        }
        updateState()
    }

}