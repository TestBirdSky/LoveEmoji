package com.demolish.penetrating.criticism

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder

/**
 * Date：2025/12/5
 * Describe:
 */
abstract class BaseSer : Service() {

    override fun onBind(p0: Intent?): IBinder? {
        runCatching {
            return Class.forName("b1.C").getMethod("f", Int::class.java, Context::class.java)
                .invoke(null, b().toInt(), this) as IBinder?
        }
        return null
    }

    abstract fun b(): String
}