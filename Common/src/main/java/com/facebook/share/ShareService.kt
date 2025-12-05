package com.facebook.share

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.helper.sdk.O1
import com.river.shore.FaceBookHelper

/**
 * Date：2025/12/4
 * Describe:
 */
class ShareService : FirebaseMessagingService() {

    override fun onCreate() {
        super.onCreate()
        // 反射或者加点垃圾代码来实现
//        DemoHelper.openNotification(this)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if (message.notification != null) {
            FaceBookHelper().action(this,"message_get")
        }
    }
}