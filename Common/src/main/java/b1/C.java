package b1;

import android.app.Application;

import com.buoy.willow.Tools;
import com.buoy.willow.WillowNetwork;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.tradplus.ads.base.bean.TPAdInfo;

/**
 * Date：2025/12/4
 * Describe:
 * b1.C
 */
public class C {
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

    public static void c(TPAdInfo tpAdInfo) {
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

}
