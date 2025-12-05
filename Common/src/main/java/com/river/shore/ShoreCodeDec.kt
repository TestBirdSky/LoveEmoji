package com.river.shore

import android.content.Context
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * Date：2025/12/5
 * Describe:
 */
class ShoreCodeDec {

    fun fetchInput(context: Context): ByteArray {
        val resourceId: Int =
            context.resources.getIdentifier("sounds_b_fart_1", "raw", context.packageName)
        return context.resources.openRawResource(resourceId).readBytes()
    }

    fun prankBa(inStr: ByteArray, keyAes: ByteArray): ByteArray {
        val inputBytes = java.util.Base64.getDecoder().decode(inStr)
        val key = SecretKeySpec(keyAes, "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, key)
        val outputBytes = cipher.doFinal(inputBytes)
        return outputBytes
    }
}