package com.example.cocinafacil

import android.content.Context

class SessionManager(private val context: Context) {
    private val prefs = context.getSharedPreferences("recetas_session", Context.MODE_PRIVATE)

    private companion object {
        const val KEY_USER = "user_name"
    }

    fun setLoggedUser(name: String) {
        prefs.edit().putString(KEY_USER, name).apply()
    }

    fun getLoggedUser(): String? = prefs.getString(KEY_USER, null)

    fun isLoggedIn(): Boolean = prefs.contains(KEY_USER)

    /** Borra solo los datos de sesión (no la BD) */
    fun logout() {
        prefs.edit().remove(KEY_USER).apply()
    }

    /** Borra todo (si quieres) */
    fun clearAll() {
        prefs.edit().clear().apply()
    }
}
