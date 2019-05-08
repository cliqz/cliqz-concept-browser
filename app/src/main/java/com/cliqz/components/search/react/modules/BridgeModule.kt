package com.cliqz.components.search.react.modules

import com.facebook.react.bridge.*

class BridgeModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    override fun getName(): String {
        return "Bridge";
    }

    @ReactMethod
    @SuppressWarnings("unused")
    fun getConfig(promise: Promise) {
        val config = Arguments.createMap()
        config.putString("appearance", "dark")
        config.putString("layout", "vertical")
        promise.resolve(config)
    }
}