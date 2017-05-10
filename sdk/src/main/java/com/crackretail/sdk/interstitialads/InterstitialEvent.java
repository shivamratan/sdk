package com.crackretail.sdk.interstitialads;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.crackretail.sdk.customevents.CustomEventInterstitial;
import com.crackretail.sdk.customevents.CustomEventInterstitialListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Shivam on 18-Jul-16.
 */
public class InterstitialEvent implements CustomEventInterstitial
{
    Context context;
    CustomEventInterstitialListener listener;
    Intent interstitialActivityIntent;
    InterstitialEvent self;

    protected BroadcastReceiver mMessageReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
           String message=intent.getStringExtra("message");
            Log.d("CR_interstital", "Got Message" + message);
            String data=intent.getStringExtra("data");

            if(message.equals("onAdClick")){
                InterstitialEvent.this.listener.onInterstitialClicked(InterstitialEvent.this.self);
            }
            else if(message.equals("onVideoAdFinished")){
                InterstitialEvent.this.listener.onInterstitialFinished();
            }
            else if(message.equals("onAdClosed")){
                InterstitialEvent.this.listener.onInterstitialClosed(InterstitialEvent.this.self);
            }
            else if(message.equals("onError")){

                try {
                    JSONObject err=new JSONObject(data);
                    String errorMesssage=err.getString("data");
                    Exception exception=new Exception(errorMesssage);
                    InterstitialEvent.this.listener.onInterstitialFailed(InterstitialEvent.this.self,exception);
                } catch (JSONException e) {
                   Log.e("CR_Interstitial","on error",e);
                }
            }
            else if(message.equals("onAutoRedirect")){
                    Log.d("CR_Interstitial","onAutoRedirect,url:"+data);
                InterstitialEvent.this.listener.onInterstitialFailed(InterstitialEvent.this.self,new Exception("onAutoRedirect"));
            }
            else
            {
                Log.e("CR_Interstitial","Error in Message");
            }


        }
    };

    public InterstitialEvent(Context context,JSONObject adResp)
    {
        this.context=context;
        this.self=this;
        this.interstitialActivityIntent=new Intent(context,InterstitialActivity.class);
        this.interstitialActivityIntent.putExtra("adResp",adResp.toString());
        LocalBroadcastManager.getInstance(context).registerReceiver(this.mMessageReceiver,new IntentFilter("interstitialEvent"));
    }


    @Override
    public void loadInterstitial(Context var1, CustomEventInterstitialListener customEventInterstitialListener, String var3, Map<String, Object> var4) {
        this.listener=customEventInterstitialListener;
        listener.onInterstitialLoaded(this.self);
    }

    @Override
    public void showInterstitial()
    {
        this.context.startActivity(this.interstitialActivityIntent);
        if(this.listener!=null){
            this.listener.onInterstitialShown(this.self);
        }
    }
}
