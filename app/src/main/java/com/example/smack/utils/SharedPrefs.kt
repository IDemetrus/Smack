package com.example.smack.utils

import android.content.Context
import android.content.SharedPreferences
import com.android.volley.toolbox.Volley

class SharedPrefs(context: Context) {
    private val prefFILENAME = "prefs"
    private val prefs: SharedPreferences = context.getSharedPreferences(prefFILENAME, 0)
    private val isLOGGED = "isLogIn"
    private val authTOKEN = "authToken"
    private val userEMAIL = "userEmail"

    var isLogIn: Boolean
        get() = prefs.getBoolean(isLOGGED, false)
        set(value) = prefs.edit().putBoolean(isLOGGED, value).apply()

    var authToken: String?
        get() = prefs.getString(authTOKEN, "")
        set(value) = prefs.edit().putString(authTOKEN, value).apply()

    var userEmail: String?
        get() = prefs.getString(userEMAIL, "")
        set(value) = prefs.edit().putString(userEMAIL, value).apply()

    val requestQueue = Volley.newRequestQueue(context)

}