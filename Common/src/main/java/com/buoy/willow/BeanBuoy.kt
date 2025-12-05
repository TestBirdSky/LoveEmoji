package com.buoy.willow

import okhttp3.Request

/**
 * Date：2025/12/4
 * Describe:
 */
data class BeanBuoy(
    var request: Request, var num: Int, val timeTry: Long = 30000, val timeTry2: Long = 60000
)