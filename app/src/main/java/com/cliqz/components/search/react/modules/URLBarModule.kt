package com.cliqz.components.search.react.modules

import android.content.Context
import android.view.View
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import org.mozilla.reference.browser.ext.components

class URLBarModule(reactContext: ReactApplicationContext, val context: Context) : ReactContextBaseJavaModule(reactContext) {
    override fun getName(): String {
        return "URLBar"
    }

    @ReactMethod
    fun isVisible(promise: Promise) {
        val toolbar = context.components.cliqzSearch.toolbar
        if (toolbar != null) {
            promise.resolve(toolbar.visibility === View.VISIBLE)
        } else {
            promise.reject("LAYOUT_ERROR", "toolbar view not found")
        }
    }

    @ReactMethod
    fun focus(promise: Promise) {
        val toolbar = context.components.cliqzSearch.toolbar
        if (toolbar != null) {
            reactApplicationContext.currentActivity?.runOnUiThread {
                toolbar.visibility = View.VISIBLE
                toolbar.editMode()
            }
            promise.resolve(true)
        } else {
            promise.reject("LAYOUT_ERROR", "toolbar view not found")
        }
    }

    @ReactMethod
    fun show(promise: Promise) {
        val toolbar = context.components.cliqzSearch.toolbar
        if (toolbar != null) {
            reactApplicationContext.currentActivity?.runOnUiThread {
                toolbar.visibility = View.VISIBLE
            }
            promise.resolve(true)
        } else {
            promise.reject("LAYOUT_ERROR", "toolbar view not found")
        }
    }

    @ReactMethod
    fun hide(promise: Promise) {
        val toolbar = context.components.cliqzSearch.toolbar
        if (toolbar != null) {
            reactApplicationContext.currentActivity?.runOnUiThread {
                toolbar.visibility = View.GONE
            }
            promise.resolve(true)
        } else {
            promise.reject("LAYOUT_ERROR", "toolbar view not found")
        }
    }

    @ReactMethod
    fun fillIn(text: String, promise: Promise) {
        val toolbar = context.components.cliqzSearch.toolbar
        if (toolbar != null) {
            reactApplicationContext.currentActivity?.runOnUiThread {
                toolbar.visibility = View.VISIBLE
                toolbar.editMode()
                toolbar.setSearchTerms(text)
            }
            promise.resolve(true)
        } else {
            promise.reject("LAYOUT_ERROR", "toolbar view not found")
        }
    }
}