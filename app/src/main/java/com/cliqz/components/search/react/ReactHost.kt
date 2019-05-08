package com.cliqz.components.search.react

import android.content.Context
import com.facebook.react.common.LifecycleState
import com.facebook.react.shell.MainReactPackage
import com.facebook.react.ReactInstanceManager
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import org.mozilla.reference.browser.BuildConfig
import org.mozilla.reference.browser.ext.application
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.facebook.react.bridge.WritableArray


internal class ReactHost(private val context: Context) : ReactInstanceManager.ReactInstanceEventListener {
    var reactContext : ReactContext? = null

    val reactInstanceManager by lazy {
        ReactInstanceManager.builder()
                .setApplication(context.application)
                .setBundleAssetName("cliqzSearch/index.android.bundle")
                .setJSMainModulePath("index")
                .addPackage(BridgePackage(context))
                .addPackage(MainReactPackage())
                .setUseDeveloperSupport(BuildConfig.DEBUG)
                .setInitialLifecycleState(LifecycleState.BEFORE_RESUME)
                .build()
    }

    init {
        reactInstanceManager.addReactInstanceEventListener(this)
        reactInstanceManager.createReactContextInBackground()
    }

    override fun onReactContextInitialized(context: ReactContext?) {
        reactContext = context;
    }

    fun callAction(module: String, action: String, vararg args: Any) {
        val eventBody = Arguments.createMap()
        eventBody.putString("module", module)
        eventBody.putString("action", action)
        eventBody.putArray("args", Arguments.fromJavaArgs(args))
        emit("action", eventBody)
    }

    private fun emit(eventName: String, arg: WritableMap) {
        if (isReady()) {
            reactContext?.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)?.emit(eventName, arg)
        }
    }

    private fun emit(eventName: String, arg: WritableArray) {
        if (isReady()) {
            reactContext?.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)?.emit(eventName, arg)
        }
    }

    private fun isReady(): Boolean {
        return reactContext != null && reactContext?.hasActiveCatalystInstance()!!
    }
}