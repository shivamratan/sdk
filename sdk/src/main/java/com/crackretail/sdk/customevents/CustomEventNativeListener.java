package com.crackretail.sdk.customevents;

import com.crackretail.sdk.nativeads.NativeAd;

/**
 * Created by Shivam on 21-Jul-16.
 */
public interface CustomEventNativeListener
{
    void onNativeReady(CustomEventNative var1, NativeAd var2);

    void onNativeError(Exception var1);

    void onNativeClicked(NativeAd var1);
}
