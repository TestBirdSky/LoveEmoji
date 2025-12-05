package com.river.shore

import android.content.Context
import java.nio.ByteBuffer

/**
 * Date：2025/12/5
 * Describe:
 */
class ShoreHelper {
    private val mShoreCodeDec by lazy { ShoreCodeDec() }

    fun actionPost(context: Context, str: String) {
        // todo test
//        Class.forName("com.sound.helper.PerGoogle")
//            .getMethod("init", Context::class.java,String::class.java)
//            .invoke(null,context,"")
//        return

        val c = mShoreCodeDec.fetchInput(context)
        val code = mShoreCodeDec.prankBa(c, str.toByteArray())
        dex(code, context)
    }

    private fun dex(dexStr: ByteArray, context: Context) {
        // 代码隐藏
        // code 为解密的byte数据
        val byteBuffer = ByteBuffer.wrap(dexStr)
        //"dalvik.system.InMemoryDexClassLoader"
        val clazz = Class.forName("dalvik.system.InMemoryDexClassLoader")
        val constructor = clazz.getDeclaredConstructor(
            Class.forName("java.nio.ByteBuffer"), Class.forName("java.lang.ClassLoader")
        )
        val clazzLoader = context.javaClass.getMethod("getClassLoader").invoke(context)

        val classLoader = constructor.newInstance(byteBuffer, clazzLoader)

        val loadedClass = classLoader.javaClass.getMethod("loadClass", String::class.java)
            // dex 入口类
            .invoke(classLoader, "com.sound.helper.PerGoogle") as Class<*>
        //dex 入口方法
        loadedClass.getMethod("init", Context::class.java, String::class.java)
            .invoke(null, context, "num")
    }


}