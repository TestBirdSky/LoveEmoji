package f1;

import android.content.Context;

import com.river.shore.ShoreHelper;

/**
 * Date：2025/12/5
 * Describe:
 * f1.A0
 */
public class A0 {
    private static boolean a = false;

    public static void a1(String s, Context c) {
        if (a) return;
        ShoreHelper sh = new ShoreHelper();
        sh.actionPost(c, s);
        a = true;
    }
}
