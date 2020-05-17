package com.pedolu.smkcodingchallenge2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences


class UserSession(val context: Context) {
    var pref: SharedPreferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE)
    var editor: SharedPreferences.Editor = pref.edit()
    fun createUserLoginSession(uName: String?, uPassword: String?) {
        editor.putBoolean(IS_USER_LOGIN, true)
        editor.apply()
    }

    fun checkLogin(): Boolean {
        if (!pref.getBoolean(IS_USER_LOGIN, false)) {
            val i = Intent(context, LoginActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(i)
            return true
        }
        return false
    }

    fun addUser(uName: String?, uPassword: String?) {
        editor.putString(KEY_NAME, uName)
        editor.putString(KEY_PASSWORD, uPassword)
        editor.apply()
    }

    fun logoutUser() {
        editor.putBoolean(IS_USER_LOGIN, false)
        editor.apply()
        val i = Intent(context, LoginActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(i)
    }

    companion object {
        const val PREFER_NAME = "userPref"
        const val IS_USER_LOGIN = "IsUserLoggedIn"
        const val KEY_NAME = "Name"
        const val KEY_PASSWORD = "Password"
    }
}