package com.demolish.penetrating.criticism

import android.content.Context
import android.content.SharedPreferences

object SettingsManager {
    var isSkip = false
    private const val PREFS_NAME = "app_settings"
    private const val KEY_VIBRATION = "vibration_enabled"
    private const val KEY_FLASHLIGHT = "flashlight_enabled"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun isVibrationEnabled(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_VIBRATION, false)
    }

    fun isFlashlightEnabled(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_FLASHLIGHT, false)
    }
}
