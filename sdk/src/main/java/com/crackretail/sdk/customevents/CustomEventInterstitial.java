package com.crackretail.sdk.customevents;

import android.content.Context;

import java.util.Map;

/**
 * Created by Shivam on 18-Jul-16.
 */
public interface CustomEventInterstitial
{
    void loadInterstitial(Context var1, CustomEventInterstitialListener customEventInterstitialListener, String var3, Map<String, Object> var4);

    void showInterstitial();
}
