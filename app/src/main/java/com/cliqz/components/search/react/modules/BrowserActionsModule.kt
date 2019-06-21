package com.cliqz.components.search.react.modules

import com.facebook.react.bridge.*
import com.facebook.react.bridge.ReactMethod
import android.content.Context
import android.os.Bundle
import com.cliqz.components.search.Cliqz
import mozilla.components.support.utils.DomainMatch
import org.mozilla.reference.browser.ext.components
import java.net.URL

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
    fun topDomains(callback: Callback) {
        val domains = mutableSetOf<String>()
        val results = context.components.core.historyStorage.getSuggestions("", 20)
            .forEach { result ->
                val url = URL(result.url)
                if (!url.protocol.equals("https")) {
                    return@forEach
                }
                var domain = url.host
                domain = if (domain.startsWith("www.")) domain.substring(4) else domain
                domain = if (domain.startsWith("m.")) domain.substring(2) else domain
                domains.add(domain)
            }

        callback.invoke(Arguments.fromArray(domains.toTypedArray()))
    }

    @ReactMethod
    fun openLink(url: String, query: String) {
        reactApplicationContext.currentActivity?.runOnUiThread {
            context.components.useCases.sessionUseCases.loadUrl.invoke(url)
            context.components.cliqz.search.onClickListener?.invoke()
            if (query != "") {
                context.components.cliqz.search.dropdownSearchClick(query, url)
            }
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
