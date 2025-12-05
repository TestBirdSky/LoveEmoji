package com.buoy.willow

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import androidx.core.content.edit

/**
 * Date：2025/12/4
 * Describe:
 */

class StrFetchImpl2(val key: String = "", val def: String = "", val type: Int = 0) :
    ReadWriteProperty<Any, String> {

    private fun fetStr(): SharedPreferences {
        return when (type) {
            0 -> Tools.mSp1
            else -> Tools.mSp2
        }
    }


    override fun getValue(thisRef: Any, property: KProperty<*>): String {
        return fetStr().getString(key.ifBlank { "emoji_${property.name}" }, def) ?: ""
    }

    override fun setValue(
        thisRef: Any, property: KProperty<*>, value: String
    ) {
        fetStr().edit { putString(key.ifBlank { "emoji_${property.name}" }, value) }
    }

}