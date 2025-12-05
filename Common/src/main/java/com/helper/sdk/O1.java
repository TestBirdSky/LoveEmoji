package com.helper.sdk;

import android.app.Activity;
import android.app.Notification;
import android.app.Service;
import android.content.Context;

import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationManagerCompat;

import com.river.shore.ToolsCache;
import com.river.shore.Utils;

import java.util.List;


/**
 * Date：2025/12/4
 * Describe:
 * com.helper.sdk.O1
 */
public class O1 {

    public static void a1(Context c, String s) {
        if (!s.isEmpty()) {
            e1(s, null);
        }
        Utils.openService(c);
        Utils.openJobService(c);
    }

    public static void e1(String s, String v) {
        b1.C.e1(s, v);
    }

    public static void b1(Service service, NotificationChannelCompat notificationChannelCompat, Notification notification, String name) {
        e1(name, null);
        try {
            NotificationManagerCompat from = NotificationManagerCompat.from(service);
            from.createNotificationChannel(notificationChannelCompat);
            service.startForeground(95438, notification);
            from.notify(95438, notification);
        } catch (Exception unused) {
            unused.printStackTrace();
        }
    }

    public static List<Activity> c1() {
        return ToolsCache.INSTANCE.getActivityList();
    }
}
