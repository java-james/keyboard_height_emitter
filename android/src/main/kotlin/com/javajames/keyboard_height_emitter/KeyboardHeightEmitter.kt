package com.javajames.keyboard_height_emitter

import android.graphics.Rect
import android.os.Build
import android.view.ViewTreeObserver
import android.view.WindowInsets
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.EventChannel

class KeyboardHeightEmitter : FlutterPlugin, EventChannel.StreamHandler, ActivityAware {
    private val keyboardHeightEventChannelName = "keyboardHeightEventChannel"
    private var eventSink: EventChannel.EventSink? = null
    private var eventChannel: EventChannel? = null
    private var activityPluginBinding: ActivityPluginBinding? = null

    override fun onAttachedToEngine(
            @NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding
    ) {
        eventChannel =
                EventChannel(flutterPluginBinding.binaryMessenger, keyboardHeightEventChannelName)
        eventChannel?.setStreamHandler(this)
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        eventChannel?.setStreamHandler(null)
    }

    override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
        eventSink = events
        val rootView = activityPluginBinding?.activity?.window?.decorView?.rootView
        rootView?.viewTreeObserver?.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        val windowHeight = rootView.height
                        val r = Rect()

                        rootView.getWindowVisibleDisplayFrame(r)

                        val fallbackKeyboardHeight = windowHeight - r.bottom

                        val imeHeight =
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                                        rootView.rootWindowInsets?.getInsets(
                                                        WindowInsets.Type.ime()
                                                )
                                                ?.bottom
                                                ?: 0
                                else fallbackKeyboardHeight

                        val metrics = activityPluginBinding?.activity?.resources?.displayMetrics
                        val logicalKeypadHeight = imeHeight / (metrics?.density ?: 1f)

                        events?.success(
                                if (imeHeight > windowHeight * 0.15) logicalKeypadHeight.toDouble()
                                else 0.0
                        )
                    }
                }
        )
    }

    override fun onCancel(arguments: Any?) {
        eventSink = null
    }

    private fun getNavigationBarHeight(): Int {
        val resourceId =
                activityPluginBinding?.activity?.resources?.getIdentifier(
                        "navigation_bar_height",
                        "dimen",
                        "android"
                )
        return if (resourceId != null && resourceId > 0) {
            activityPluginBinding?.activity?.resources?.getDimensionPixelSize(resourceId) ?: 0
        } else {
            0
        }
    }

    private fun isNavigationBarVisible(): Boolean {
        val decorView = activityPluginBinding?.activity?.window?.decorView
        val rootWindowInsets = decorView?.rootWindowInsets ?: return false
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            rootWindowInsets.isVisible(android.view.WindowInsets.Type.navigationBars())
        } else {
            val systemWindowInsetBottom = rootWindowInsets.systemWindowInsetBottom
            systemWindowInsetBottom > 0
        }
    }

    // Implement ActivityAware methods
    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activityPluginBinding = binding
    }

    override fun onDetachedFromActivityForConfigChanges() {
        activityPluginBinding = null
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        activityPluginBinding = binding
    }

    override fun onDetachedFromActivity() {
        activityPluginBinding = null
    }
}
