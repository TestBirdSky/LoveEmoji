package walks;


import android.os.Handler;
import android.os.Message;

import walks.lb.ha;


/**
 * Date：2025/7/28
 * Describe:
 */
// todo 重命名
public class WalkHa extends Handler {
    @Override
    public void handleMessage(Message message) {
        ha.d(message.what);
    }
}
