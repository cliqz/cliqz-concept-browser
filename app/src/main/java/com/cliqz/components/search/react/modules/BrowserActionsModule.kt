package com.cliqz.components.search.react.modules

import com.facebook.react.bridge.*
import com.facebook.react.bridge.ReactMethod
import android.content.Context
import android.os.Bundle
import org.mozilla.reference.browser.ext.components

class BrowserActionsModule(reactContext: ReactApplicationContext, val context: Context) : ReactContextBaseJavaModule(reactContext) {
    override fun getName(): String {
        return "BrowserActions"
    }

    @ReactMethod
    fun searchHistory(query: String, callback: Callback) {
        if (query != null && query.length > 1) {
            val results = context.components.core.historyStorage.getSuggestions(query, 5)
                    .map { searchResult ->
                val map = Bundle()
                map.putString("id", searchResult.id)
                map.putString("url", searchResult.url)
                map.putString("title", searchResult.title)
                map.putInt("score", searchResult.score)
                map
            }
            callback.invoke(Arguments.fromList(results))
        } else {
            callback.invoke(Arguments.createArray())
        }
    }


    @ReactMethod
    fun openLink(url: String, query: String) {
        reactApplicationContext.currentActivity?.runOnUiThread {
            context.components.useCases.sessionUseCases.loadUrl.invoke(url)
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
