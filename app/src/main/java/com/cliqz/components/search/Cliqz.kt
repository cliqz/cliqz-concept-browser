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
class Cliqz(private val context: Context) : SessionManager.Observer, Session.Observer {

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

    init {
        context.components.core.sessionManager.register(this)
        context.components.core.sessionManager.sessions.forEach { it.register(this) }
        updateViewState()
    }

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

    fun setComponentState(newState: State) {
        when (newState) {
            State.FRESHTAB -> freshTab.visibility = View.VISIBLE
            State.SEARCH -> {
                freshTab.visibility = View.GONE
                search.view.visibility = View.VISIBLE
            }
            State.BROWSING -> {
                freshTab.visibility = View.GONE
                search.onClickListener?.invoke()
                search.active = false
            }
        }
    }

    fun updateViewState() {
        if (context.components.core.sessionManager.selectedSession == null || context.components.core.sessionManager.selectedSession?.url == "about:blank") {
            if (search.active) {
                setComponentState(State.SEARCH)
            } else {
                setComponentState(State.FRESHTAB)
            }
        } else {
            setComponentState(State.BROWSING)
        }
    }

    /**
     * The selection has changed and the given session is now the selected session.
     */
    override fun onSessionSelected(session: Session) {
        updateViewState()
    }

    /**
     * The given session has been added.
     */
    override fun onSessionAdded(session: Session) {
        updateViewState()
        session.register(this)
    }

    /**
     * Sessions have been restored via a snapshot. This callback is invoked at the end of the
     * call to <code>read</code>, after every session in the snapshot was added, and
     * appropriate session was selected.
     */
    override fun onSessionsRestored() {
        updateViewState()
        context.components.core.sessionManager.sessions.forEach { it.register(this) }
    }

    /**
     * The given session has been removed.
     */
    override fun onSessionRemoved(session: Session) {
        updateViewState()
        session.unregister(this)
    }

    override fun onUrlChanged(session: Session, url: String) {
        updateViewState()
    }
}