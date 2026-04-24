package com.animex.app.utils

import android.content.Context
import com.animex.app.data.model.WatchHistory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object HistoryManager {
    private const val PREF = "animex_history"
    private const val KEY  = "history_list"
    private const val MAX  = 30

    private val gson = Gson()

    fun getAll(ctx: Context): List<WatchHistory> {
        val prefs = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val json  = prefs.getString(KEY, null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<WatchHistory>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (_: Exception) { emptyList() }
    }

    fun save(ctx: Context, history: WatchHistory) {
        val list = getAll(ctx).toMutableList()
        val idx  = list.indexOfFirst { it.slug == history.slug }
        if (idx >= 0) list.removeAt(idx)
        list.add(0, history)
        val trimmed = list.take(MAX)
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit().putString(KEY, gson.toJson(trimmed)).apply()
    }

    fun clear(ctx: Context) {
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE).edit().remove(KEY).apply()
    }
}
