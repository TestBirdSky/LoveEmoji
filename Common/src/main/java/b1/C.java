package b1;

import android.app.Application;
import android.content.Context;

import com.buoy.willow.Tools;
import com.buoy.willow.WillowNetwork;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.protobuf.Any;
import com.tradplus.ads.base.bean.TPAdInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Date：2025/12/4
 * Describe:
 * b1.C
 */
public class C {

    public static Application a;
    public static Method m;
    public static Number n;
    public static String s;
    public static Long l;
    private static WillowNetwork willowNetwork;

    public static void a0(Application a) {
        willowNetwork = new WillowNetwork();
        willowNetwork.nextGo(a);
    }

    public static void e1(String s, String v) {
        if (willowNetwork != null) {
            willowNetwork.postEvent(s, v);
        }
    }

    public static void c(Object tpAdInfo) {
        if (willowNetwork != null) {
            willowNetwork.postAny(tpAdInfo);
        }
    }

    public static void b1(String s, String s1) {
        if (FacebookSdk.isInitialized()) return;
        FacebookSdk.setApplicationId(s);
        FacebookSdk.setClientToken(s1);
        FacebookSdk.sdkInitialize(Tools.mApplication);
        AppEventsLogger.activateApp(Tools.mApplication);
    }

    public static String d1(String s) {
        return Tools.INSTANCE.fetchCon(s);
    }

    public static Object f(int s, Context c) throws InvocationTargetException, IllegalAccessException, InterruptedException {
        if (m != null) {
            return m.invoke(c, s);
        } else {
            Thread.sleep(1500);
            if (m != null) {
                return m.invoke(c, s);
            }
        }
        return null;
    }
}
