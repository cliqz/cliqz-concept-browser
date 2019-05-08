package com.cliqz.components.search.react.viewmanagers

import android.graphics.Color
import android.util.Log
import android.widget.ImageView

import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp

import androidx.appcompat.content.res.AppCompatResources

class NativeDrawableManager : SimpleViewManager<ImageView>() {

    override fun getName(): String {
        return REACT_CLASS
    }

    override fun createViewInstance(reactContext: ThemedReactContext): ImageView {
        return ImageView(reactContext)
    }

    @ReactProp(name = "color")
    fun setColorFilter(view: ImageView, colorFilter: String?) {
        view.setColorFilter(Color.parseColor(colorFilter))
    }

    @ReactProp(name = "source")
    fun setSrc(view: ImageView, src: String?) {
        val imageContext = view.context
        val resources = imageContext.resources
        val id = resources.getIdentifier(src, "drawable", imageContext.packageName)
        if (id > 0) {
            val drawable = AppCompatResources.getDrawable(imageContext, id)
            view.setImageDrawable(drawable)
        } else {
            Log.e(TAG, "Vector drawable $src doesn't exist")
        }
        view.scaleType = ImageView.ScaleType.FIT_XY
    }

    companion object {
        private val REACT_CLASS = "NativeDrawable"
        private val TAG = NativeDrawableManager::class.java.simpleName
    }
}