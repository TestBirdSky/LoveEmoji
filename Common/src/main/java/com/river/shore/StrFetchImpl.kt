package com.river.shore

import android.content.SharedPreferences
import androidx.core.content.edit
import java.util.UUID
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Date：2025/12/4
 * Describe:
 */
class StrFetchImpl(val key: String = "", val def: String = "") : ReadWriteProperty<Any, String> {

    private fun fetStr(): SharedPreferences {
        return ToolsCache.mSp2
    }


    override fun getValue(
        thisRef: Any, property: KProperty<*>
    ): String {
        var r = fetStr().getString(key.ifBlank { property.name }, def) ?: ""
        if (def == "initAndroid" && r == "initAndroid") {
            r = UUID.randomUUID().toString()
            fetStr().edit { putString(key.ifBlank { property.name }, r) }
        }
        return r
    }

    override fun setValue(
        thisRef: Any, property: KProperty<*>, value: String
    ) {
        fetStr().edit { putString(key.ifBlank { property.name }, value) }
    }

}
