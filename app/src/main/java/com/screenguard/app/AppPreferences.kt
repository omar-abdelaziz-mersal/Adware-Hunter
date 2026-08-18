package com.screenguard.app
import android.content.Context
object AppPreferences {
    private const val PREF_NAME = "guard_prefs"
    private const val KEY_PROTECTION = "is_protection_on"
    private const val KEY_LOGS = "blocked_logs"
    fun isProtectionOn(context: Context): Boolean {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getBoolean(KEY_PROTECTION, false)
    }
    fun setProtectionOn(context: Context, on: Boolean) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().putBoolean(KEY_PROTECTION, on).apply()
    }
    fun addLog(context: Context, packageName: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val old = prefs.getString(KEY_LOGS, "") ?: ""
        val time = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())
        val newEntry = "$time - تم حظر $packageName\n"
        prefs.edit().putString(KEY_LOGS, newEntry + old).apply()
    }
    fun getLogs(context: Context): String {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(KEY_LOGS, "") ?: ""
    }
    fun clearLogs(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().putString(KEY_LOGS, "").apply()
    }
}
