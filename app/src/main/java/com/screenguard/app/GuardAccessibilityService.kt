package com.screenguard.app
import android.accessibilityservice.AccessibilityService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.view.accessibility.AccessibilityEvent
import androidx.core.app.NotificationCompat
class GuardAccessibilityService : AccessibilityService() {
    private val whiteList = setOf(
        "com.android.systemui",
        "com.android.phone",
        "com.android.dialer",
        "com.google.android.inputmethod.latin",
        "com.google.android.gms",
        "com.android.launcher",
        "com.google.android.apps.nexuslauncher",
        "com.screenguard.app",
        "android"
    )
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        if (!AppPreferences.isProtectionOn(this)) return
        if (event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return
        val packageName = event.packageName?.toString() ?: return
        if (packageName in whiteList) return
        if (packageName.startsWith("com.android")) return
        if (packageName.startsWith("com.google.android")) return
        // اعتبره هجوم اعلاني خبيث
        performGlobalAction(GLOBAL_ACTION_HOME)
        AppPreferences.addLog(this, packageName)
        showNotification(packageName)
    }
    override fun onInterrupt() {}
    private fun showNotification(packageName: String) {
        val channelId = "guard_channel"
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(channelId, "حماية الشاشة", NotificationManager.IMPORTANCE_HIGH)
        manager.createNotificationChannel(channel)
        val text = getString(R.string.attack_notification_text, packageName)
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(getString(R.string.attack_notification_title))
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setAutoCancel(true)
            .build()
        manager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
