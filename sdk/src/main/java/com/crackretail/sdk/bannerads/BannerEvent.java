package com.crackretail.sdk.bannerads;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.crackretail.sdk.customevents.CustomEventBanner;
import com.crackretail.sdk.customevents.CustomEventBannerListener;
import com.crackretail.sdk.webview.CrackRetailWebView;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Shivam on 02-Jul-16.
 */
public class BannerEvent implements CustomEventBanner
{
   CrackRetailWebView webView;
   JSONObject respObj;

    public BannerEvent(CrackRetailWebView webView,JSONObject respObj)
    {
        this.webView=webView;
        this.respObj=respObj;
    }

    @Override
    public void loadAd(final Context context, final CustomEventBannerListener listener, String str, Map<String, Object> mymap) {

        BannerEvent.this.webView.setListener(new CrackRetailWebView.Listener() {
            @Override
            public void onReady(CrackRetailWebView webView) {

            }

            @Override
            public void onError(CrackRetailWebView webView, Exception exception) {
                listener.onBannerError(webView, exception);
            }

            @Override
            public void onAdClick(CrackRetailWebView webView, String str)
            {

                try {
                //  String clickURL=respObj.getJSONObject("ad").getJSONObject("request").getJSONObject("clickurl").getString("__cdata");

                    Uri uri = Uri.parse(str);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(uri);
                    context.startActivity(intent);
                } catch (Exception e) {
                    Log.e("CrackRetailBanner", "Launch Browser",e);
                    listener.onBannerError(webView, e);
                    return;
                }

                listener.onBannerClicked(webView);

            }

            @Override
            public void onVideoAdFinished(CrackRetailWebView webView) {

                listener.onBannerFinished();
            }

            @Override
            public void onAdClosed(CrackRetailWebView webView) {

                listener.onBannerClosed(webView);
            }

            @Override
            public void onAdResponse(CrackRetailWebView webView, JSONObject jsonObj) {

            }

            @Override
            public void onAdLoaded(CrackRetailWebView webView) {


            }

            @Override
            public void onNoAd(CrackRetailWebView webView) {

                listener.onBannerError(webView, new Exception("On No Ad"));
            }

            @Override
            public void onAutoDirect(CrackRetailWebView webView, String str) {
                listener.onBannerError(webView, new Exception("onAutoDirect"));
            }
        });


        if (this.render(this.respObj))
        {
            listener.onBannerLoaded(this.webView);
        }
        else
        {
            listener.onBannerError((View)null,new Exception("Error while Rendering"));
        }

    }

    boolean render(JSONObject respObj)
    {
        try
        {
            this.webView.renderAd(respObj);
            return true;
        }
        catch(Exception exception)
        {
            Log.e("CrackRetailBanner","Banner Event Render",exception);
            return false;
        }
    }

}
