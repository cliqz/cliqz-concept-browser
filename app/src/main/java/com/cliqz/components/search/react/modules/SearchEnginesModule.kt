package com.cliqz.components.search.react.modules

import android.content.Context
import android.os.Bundle
import com.facebook.react.bridge.*
import org.mozilla.reference.browser.ext.components
import com.facebook.react.bridge.WritableArray



/**
 * @author Sam Macbeth
 */
class SearchEnginesModule(reactContext: ReactApplicationContext, val context: Context) : ReactContextBaseJavaModule(reactContext) {
    override fun getName(): String {
        return "SearchEnginesModule"
    }

    @ReactMethod
    fun getSearchEngines(promise: Promise) {
        val searchEngineManager = context.components.search.searchEngineManager
        val engines = searchEngineManager.getSearchEngines(context)
        val defaultEngine = searchEngineManager.getDefaultSearchEngine(context)
        val type = "text/html"
        val searchTerm = "{SearchTerm}"
        val localeTerm = "{LocaleTerm}"
        val result = Arguments.createArray()
        engines.forEach {
            val map = Arguments.createMap()
            val urls = Arguments.createMap()
            val searchUrl = it.buildSearchUrl("XXX").replace("XXX", searchTerm)
            urls.putString(type, searchUrl)
            map.putString("name", it.name)
            map.putBoolean("default", it.name == defaultEngine.name)
            map.putString("base_url", searchUrl.replace(searchTerm, ""))
            map.putMap("urls", urls)
            map.putString("SearchTermComponent", searchTerm)
            map.putString("LocaleTermComponent", localeTerm)
            result.pushMap(map)
        }
        promise.resolve(result)
    }
}