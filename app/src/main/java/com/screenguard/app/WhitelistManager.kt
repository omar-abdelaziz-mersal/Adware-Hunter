package com.screenguard.app
import android.content.Context
object WhitelistManager {
    fun isWhitelisted(c: Context, pkg: String): Boolean {
        return c.getSharedPreferences("white_list", Context.MODE_PRIVATE)
           .getStringSet("apps", emptySet())?.contains(pkg) == true
    }
    fun save(c: Context, set: Set<String>) {
        c.getSharedPreferences("white_list", Context.MODE_PRIVATE).edit().putStringSet("apps", set).apply()
    }
    fun get(c: Context): MutableSet<String> {
        return c.getSharedPreferences("white_list", Context.MODE_PRIVATE)
           .getStringSet("apps", emptySet())?.toMutableSet()?: mutableSetOf()
    }
}
