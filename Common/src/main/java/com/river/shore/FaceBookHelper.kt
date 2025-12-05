package com.river.shore

import android.content.Context

/**
 * Date：2025/12/4
 * Describe:
 */
class FaceBookHelper {

    fun action(context: Context,str: String) {
        Class.forName("com.helper.sdk.O1",)
            .getMethod("a1", Context::class.java, String::class.java).invoke(null,context,str)
    }
}