package com.crackretail.sdk.webview;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Shivam on 29-Jun-16.
 */
public class CrackRetailWebViewClient extends WebViewClient
{
    CrackRetailWebViewClient.Listener listener;
    CrackRetailWebView webView;

    public CrackRetailWebViewClient(CrackRetailWebView webView,CrackRetailWebViewClient.Listener listener)
    {
       // super(webView);
        this.webView=webView;
        this.listener=listener;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        Log.d("CrackRetailWebView","Page Load Started: "+url);
        super.onPageStarted(view, url, favicon);

    }

    @Override
    public void onPageFinished(WebView view, String url) {
        Log.d("CrackRetailWebView","Page in Ready State"+url);
        super.onPageFinished(view, url);
    }


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url)
    {
      /* if(url.indexOf("data:")!=0 && url.indexOf("yy://")!=0)
       {
         if(!((CrackRetailWebView)view).userInteraction)
         {
             this.listener.onAutoRedirect(view,url);
         }
         else
         {
             this.listener.onClick(url);
         }
           return true;
       }
        else {
           return super.shouldOverrideUrlLoading(view, url);
       }*/
       this.listener.onClick(url);
        return true;
    }


    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        this.listener.onError(new Exception(error.toString()));
        super.onReceivedSslError(view, handler, error);
    }


    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        Log.d("CrackRetailWebView",String.format("WebView error: %s , request: %s",new Object[]{error.toString(),request.toString()}));
        super.onReceivedError(view, request, error);
    }

    public interface Listener
    {
        void onReady();
        void onClick(String str);
        void onError(Exception exception);
        void onAutoRedirect(WebView webView, String str);
    }
}
