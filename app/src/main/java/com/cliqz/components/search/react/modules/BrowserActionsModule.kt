package com.cliqz.components.search.react.modules

import com.facebook.react.bridge.*
import com.facebook.react.bridge.ReactMethod
import android.content.Context
import org.mozilla.reference.browser.ext.components

class BrowserActionsModule(reactContext: ReactApplicationContext, val context: Context) : ReactContextBaseJavaModule(reactContext) {
    override fun getName(): String {
        return "BrowserActions"
    }

    @ReactMethod
    fun searchHistory(query: String, callback: Callback) {
        val wArray = Arguments.createArray()
        callback.invoke(wArray)
    }


    @ReactMethod
    fun openLink(url: String, query: String) {
        context.components.useCases.sessionUseCases.loadUrl.invoke(url)
        reactApplicationContext.currentActivity?.runOnUiThread {
            context.components.cliqzSearch.onClickListener?.invoke()
        }
    }

    @ReactMethod
    @SuppressWarnings("unused")
    fun autocomplete(data: String) {
    }

    @ReactMethod
    @SuppressWarnings("unused")
    fun hideKeyboard() {
    }
}
