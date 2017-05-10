package com.crackretail.sdk.nativeads;

import com.crackretail.sdk.customevents.CustomEventNative;

/**
 * Created by Shivam on 21-Jul-16.
 */
public interface NativeListener
{

        void onNativeReady(Native nativevar, CustomEventNative customEventNative, NativeAd nativeAd);

        void onNativeError(Exception var1);

        void onNativeClick(NativeAd var1);


}
