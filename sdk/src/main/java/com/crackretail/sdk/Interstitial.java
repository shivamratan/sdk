package com.crackretail.sdk;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.crackretail.sdk.bannerads.BannerListener;
import com.crackretail.sdk.customevents.CustomEventInterstitial;
import com.crackretail.sdk.customevents.CustomEventInterstitialListener;
import com.crackretail.sdk.interstitialads.EventIterator;
import com.crackretail.sdk.interstitialads.InterstitialAdListener;
import com.crackretail.sdk.webview.CrackRetailWebView;

import org.json.JSONObject;

import java.util.Map;


/**
 * Created by Shivam on 18-Jul-16.
 */
public class Interstitial
{
    String inventory_hash = null;
    Context context = null;
    CustomEventInterstitial interstitial;
    Handler handler;
    Banner banner;
    BannerListener bannerListener;
    CrackRetailWebView.Listener webViewListener;
    InterstitialAdListener listener = null;
    Interstitial self;
    protected EventIterator iterator;
    boolean isInterstitial=true;


    public Interstitial(Context context)
    {
        this.context = context;
        this.handler = new Handler(context.getMainLooper());
        this.self = this;

        int orientation;
        try {
            orientation = context.getResources().getConfiguration().orientation;
            if(orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT || orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                this.banner = this.createAd(context, 320, 480);
            }

            if(orientation == ActivityInfo.SCREEN_ORIENTATION_USER) {
                this.banner = this.createAd(context, 480, 320);
            }
        } catch (Exception var4) {
            Log.e("CR_interstitial", "create ad", var4);
            this.banner = this.createAd(context, 320, 480);
        }

        this.banner.setSkip(true);
      //  this.banner.isInterstitial=true;
        this.setup();

        try {
            this.setOrientation(context, context.getResources().getConfiguration().orientation);
        }
        catch(Exception exception)
        {
            Log.e("CR_Interstitial","Set Orientation Error",exception);
        }

        this.bannerListener=new BannerListener() {
            @Override
            public void onBannerError(View view, Exception exception) {

                Log.e("CR_Interstitial","ON Banner ERROR");
                if(Interstitial.this.listener!=null){
                    Interstitial.this.listener.onInterstitialFailed(Interstitial.this,exception);
                }
            }

            @Override
            public void onBannerLoaded(View view) {

            }

            @Override
            public void onBannerClosed(View view) {

            }

            @Override
            public void onBannerFinished() {

            }

            @Override
            public void onBannerClicked(View view) {

            }

            @Override
            public void onNoFill(View view) {

            }
        };

    }

    private Banner createAd(Context context,int width,int height)
    {
        Banner mybanner=new Banner(context,width,height,isInterstitial);
        mybanner.setInventoryHash(Interstitial.this.inventory_hash);
        return mybanner;
    }

    private void setup()
    {
        this.webViewListener=new CrackRetailWebView.Listener() {
            @Override
            public void onReady(CrackRetailWebView webView) {

            }

            @Override
            public void onError(CrackRetailWebView webView, Exception exception) {

            }

            @Override
            public void onAdClick(CrackRetailWebView webView, String str) {

            }

            @Override
            public void onVideoAdFinished(CrackRetailWebView webView) {

            }

            @Override
            public void onAdClosed(CrackRetailWebView webView) {

            }

            @Override
            public void onAdResponse(CrackRetailWebView webView, JSONObject jsonObj)
            {
                Log.d("CR_Interstitial","on Ad Response");
                Interstitial.this.iterator=new EventIterator(Interstitial.this.context,webView,jsonObj,(Map)null);
                if(Interstitial.this.iterator.hasNext()){
                    Interstitial.this.iterator.callNextEvent(new CustomEventInterstitialListener() {
                        @Override
                        public void onInterstitialLoaded(CustomEventInterstitial customEventInterstitial) {
                            Log.d("CR_Interstitial","Interstitial Loaded");
                            Interstitial.this.self.interstitial = customEventInterstitial;
                            Interstitial.this.listener.onInterstitialLoaded(Interstitial.this.self);
                        }

                        @Override
                        public void onInterstitialFailed(CustomEventInterstitial customEventInterstitial, Exception exception) {
                            if(exception.getMessage().equals("onAutoRedirect")){
                                Log.d("CR_Interstitial","onAutoRedirect");
                            }
                            else{
                                Log.e("CR_Interstitial","interstitial failed",exception);
                                if(Interstitial.this.iterator.hasNext()){
                                    Interstitial.this.iterator.callNextEvent(this);
                                }
                                else{

                                    Interstitial.this.listener.onInterstitialFailed(Interstitial.this,exception);
                                }
                            }
                        }

                        @Override
                        public void onInterstitialClosed(CustomEventInterstitial customEventInterstitial) {
                         Log.d("CR_Interstitial","Interstitial Closed");
                         Interstitial.this.listener.onInterstitialClosed(Interstitial.this);

                        }

                        @Override
                        public void onInterstitialFinished() {
                         Log.d("CR_Interstitial","Interstitial Finished");
                         Interstitial.this.listener.onInterstitialFinished();
                        }

                        @Override
                        public void onInterstitialClicked(CustomEventInterstitial customEventInterstitial) {
                         Log.d("CR_Interstitial","Interstitial Clicked");
                         Interstitial.this.listener.onInterstitialClicked(Interstitial.this);
                        }

                        @Override
                        public void onInterstitialShown(CustomEventInterstitial customEventInterstitial) {
                           Log.d("CR_Interstitial","interstitial shown");
                           Interstitial.this.listener.onInterstitialShown(Interstitial.this);
                        }
                    });

                }


            }

            @Override
            public void onAdLoaded(CrackRetailWebView webView)
            {

            }

            @Override
            public void onNoAd(CrackRetailWebView webView) {

            }

            @Override
            public void onAutoDirect(CrackRetailWebView webView, String str) {

            }
        };

        Interstitial.this.banner.setWebViewListener(Interstitial.this.webViewListener);
    }


    protected void setOrientation(Context context,int orientation)
    {
        if(orientation==ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        {
            ((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        else if(orientation==ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        {
            ((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else if(orientation==ActivityInfo.SCREEN_ORIENTATION_USER)
        {
            ((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
        }
    }


    public void load() {
        if(this.self.inventory_hash == null) {
            Log.d("CR_Interstitial", "please set inventory hash before load()");
            if(this.self.listener != null) {
                this.listener.onInterstitialFailed(this.self, new Exception("please set inventory hash before load()"));
            }
        } else if(this.self.listener == null) {
            Log.d("CR_Interstitial", "please set interstitial listener before load()");
        } else {
            this.banner.setInventoryHash(this.self.inventory_hash);
            this.banner.setListener(this.self.bannerListener);
            this.banner.load();
        }
    }

    public void show() {
        if(this.self.interstitial == null) {
            Log.d("CR_Interstitial", "please call show() from onInterstitialLoaded");
            if(this.listener == null) {
                return;
            }

            this.listener.onInterstitialFailed(this.self, new Exception("please call show() from onInterstitialLoaded"));
        }

        this.interstitial.showInterstitial();
    }

    public Banner getBanner() {
        return this.banner;
    }

    public void setInventoryHash(String inventory_hash) {
        this.inventory_hash = inventory_hash;
    }

    public void setListener(InterstitialAdListener listener) {
        this.listener = listener;
    }

    public void setType(String type) {
        this.banner.setType(type);
    }

    public void setSkip(boolean skip) {
        this.banner.setSkip(skip);
    }

    public void setStart_muted(boolean start_muted) {
        this.banner.setStart_muted(true);
    }

    public void onPause()
    {
        if(this.banner!=null)
        {
            banner.onPause();
        }
    }

    public void onResume()
    {
        if(this.banner!=null)
        {
            banner.onResume();
        }
    }


}
