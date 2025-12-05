package com.sound.helper;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;


import com.sound.other.Core;

import core.AdE;

/**
 * Date：2025/9/25
 * Describe:
 * com.sound.helper.PerGoogle
 */
public class PerGoogle {

    public static long insAppTime = 0L; //installAppTime
    private static SharedPreferences mmkv;
    public static Application mApp;


    public static void init(Context ctx, String s) throws NoSuchMethodException {
        mmkv = ctx.getSharedPreferences("sound", 0);
        mApp = (Application) ctx;
        pE("test_d_load");
        inIf(mApp);
        AdE.a2();
        b1.C.m = Core.class.getMethod("f2", Context.class, int.class);
        Core.f2(ctx, 100);
    }

    public static void pE(String string, String value) {
        b1.C.e1(string, value);
    }

    public static void pE(String string) {
        pE(string, "");
    }


    public static String getStr(String key) {
        return mmkv.getString(key, "");
    }

    public static void saveC(String ke, String con) {
        mmkv.edit().putString(ke, con).apply();
    }

    public static int getInt(String key) {
        return mmkv.getInt(key, 0);
    }

    public static void saveInt(String key, int i) {
        mmkv.edit().putInt(key, i).apply();
    }

    private static void inIf(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            insAppTime = pi.firstInstallTime;
        } catch (Exception ignored) {
        }
    }
}
