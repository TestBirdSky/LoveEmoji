package walks;

import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import walks.lb.ha;


/**
 * Date：2025/7/28
 * Describe:
 */
// todo 重命名改成和so中的一样
public class WhiteChrome extends WebChromeClient {
    @Override
    public void onProgressChanged(WebView webView, int i10) {
        super.onProgressChanged(webView, i10);
        // todo del
        Log.e("LOG-->", "onProgressChanged: " + i10);
        if (i10 == 100) {
            ha.d(i10);
        }
    }
}
