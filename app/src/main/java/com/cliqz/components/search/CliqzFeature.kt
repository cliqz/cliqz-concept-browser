package com.cliqz.components.search

import mozilla.components.browser.session.SessionManager
import mozilla.components.support.base.feature.BackHandler
import mozilla.components.support.base.feature.LifecycleAwareFeature

/**
 * @author Sam Macbeth
 */
class CliqzFeature(
        val cliqz: Cliqz,
        private val sessionManager: SessionManager
) : LifecycleAwareFeature, BackHandler {
    override fun start() {
    }

    override fun onBackPressed(): Boolean {
        if (sessionManager.selectedSession == null || sessionManager.selectedSession?.canGoBack == false) {
            cliqz.setComponentState(Cliqz.State.FRESHTAB)
            return true
        }
        return false
    }

    override fun stop() {
    }

}