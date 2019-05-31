package com.cliqz.components.search.react

import com.cliqz.components.search.react.modules.BridgeModule
import com.cliqz.components.search.react.modules.BrowserActionsModule
import com.cliqz.components.search.react.viewmanagers.NativeDrawableManager
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.NativeModule
import com.facebook.react.ReactPackage
import android.content.Context
import com.cliqz.components.search.react.modules.TabsModule
import com.cliqz.components.search.react.modules.URLBarModule


class BridgePackage(val context: Context) : ReactPackage {
    override fun createViewManagers(reactContext: ReactApplicationContext): MutableList<NativeDrawableManager> /* MutableList<com.facebook.react.uimanager.ViewManager<View, ReactShadowNode<*>>> */ {
        return mutableListOf(
                NativeDrawableManager()
        )
    }

    override fun createNativeModules(reactContext: ReactApplicationContext): MutableList<NativeModule> {
        return mutableListOf(
                BridgeModule(reactContext),
                BrowserActionsModule(reactContext, context),
                URLBarModule(reactContext, context),
                TabsModule(reactContext, context)
        )
    }
}