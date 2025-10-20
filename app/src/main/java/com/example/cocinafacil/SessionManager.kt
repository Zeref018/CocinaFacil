package com.example.cocinafacil

import android.content.Context


class SessionManager(private val context: Context) {
    private val prefs = context.getSharedPreferences("recetas_session", Context.MODE_PRIVATE)


    fun setLoggedUser(name: String) {
        prefs.edit().putString("user_name", name).apply()
    }


    fun getLoggedUser(): String? = prefs.getString("user_name", null)


    fun clear() { prefs.edit().clear().apply() }
}