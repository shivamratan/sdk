package com.crackretail.sdk.webview;

import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by Shivam on 29-Jun-16.
 */
public class CrackRetailWebChromeClient extends WebChromeClient
{
    CrackRetailWebChromeClient.ReadyCallback callback;
    public CrackRetailWebChromeClient(CrackRetailWebChromeClient.ReadyCallback callback)
    {
        this.callback=callback;
    }

    public boolean onConsoleMessage(ConsoleMessage consoleMessage)
    {
        String message=consoleMessage.message();
        Log.d("CrackRetail_WebConsole","CONSOLE_MESSAGE>>>>>>> "+message);
        return super.onConsoleMessage(consoleMessage);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress)
    {
        if(newProgress==100)
        {
            Log.d("CrackRetailWebView","Loading done at 100");
         //   this.callback.onReady();
        }
        super.onProgressChanged(view, newProgress);
    }

    public interface ReadyCallback
    {
        void onReady();
    }
}
