package ru.ndevelop.reuser.repositories

import android.content.SharedPreferences
import android.preference.PreferenceManager
import ru.ndevelop.reuser.App


object PreferencesRepository {

    private val prefs: SharedPreferences by lazy {
        val ctx = App.applicationContext()
        PreferenceManager.getDefaultSharedPreferences(ctx)
    }
    fun setIsNotFirstLaunch(){
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putBoolean("FIRST_LAUNCH",false )
        editor.apply()
    }
    fun isFirstLaunch() = prefs.getBoolean("FIRST_LAUNCH", true)



}
