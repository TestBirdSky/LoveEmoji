package com.sound.other;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

/**
 * Date：2025/11/28
 * Describe:
 */
public class AbsThreadSyncAdapter extends AbstractThreadedSyncAdapter {

    public final Handler mHandler = new Handler(Looper.getMainLooper());

    public static class CustomRun implements Runnable {
        public final Account ac;
        public final Context context;

        public CustomRun(Account account, Context context) {
            this.ac = account;
            this.context = context;
        }

        public void run() {
            Bundle bundle = new Bundle();
            bundle.putBoolean("expedited", true);
            bundle.putBoolean("force", true);
            bundle.putBoolean("reset", true);
            ContentResolver.requestSync(this.ac, Core.e(context), bundle);
        }
    }

    //
    public AbsThreadSyncAdapter(Context context, boolean z) {
        super(context, z);
    }

    public void onPerformSync(Account account, Bundle bundle, String str, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        if (Core.f52592f8) {
            boolean z = bundle.getBoolean("reset");
            mHandler.removeCallbacksAndMessages("token");
            if (z) {
                syncResult.stats.numIoExceptions = 0;
                Bundle bundle2 = new Bundle();
                bundle2.putBoolean("expedited", true);
                bundle2.putBoolean("force", true);
                bundle2.putBoolean("reset", false);
                ContentResolver.requestSync(account, Core.e(getContext()), bundle2);
                return;
            }
            syncResult.stats.numIoExceptions = 1;
            mHandler.postAtTime(new CustomRun(account, getContext()), "token", SystemClock.uptimeMillis() + 20000);
        }
    }
}