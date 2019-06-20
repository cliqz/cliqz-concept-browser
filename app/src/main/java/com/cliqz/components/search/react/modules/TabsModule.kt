package com.cliqz.components.search.react.modules

import android.content.Context
import android.os.Bundle
import com.facebook.react.bridge.*
import org.mozilla.reference.browser.ext.components

class TabsModule(reactContext: ReactApplicationContext, val context: Context) : ReactContextBaseJavaModule(reactContext) {
    override fun getName(): String {
        return "Tabs"
    }

    @ReactMethod
    fun list(promise: Promise) {
        val results = context.components.core.sessionManager.sessions.map {
            val map = Bundle()
            map.putString("id", it.id)
            map.putString("title", it.title)
            map.putString("url", it.url)
            map.putString("searchTerms", it.searchTerms)
            map
        }
        promise.resolve(Arguments.fromList(results))
    }

    @ReactMethod
    fun select(id: String) {
        val session = context.components.core.sessionManager.findSessionById(id)
        if (session != null) {
            context.components.core.sessionManager.select(session)
        }
    }
}