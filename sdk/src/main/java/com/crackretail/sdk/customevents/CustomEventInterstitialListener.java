package com.crackretail.sdk.customevents;

/**
 * Created by Shivam on 18-Jul-16.
 */
public interface CustomEventInterstitialListener
{
    void onInterstitialLoaded(CustomEventInterstitial customEventInterstitial);

    void onInterstitialFailed(CustomEventInterstitial customEventInterstitial, Exception exception);

    void onInterstitialClosed(CustomEventInterstitial customEventInterstitial);

    void onInterstitialFinished();

    void onInterstitialClicked(CustomEventInterstitial customEventInterstitial);

    void onInterstitialShown(CustomEventInterstitial customEventInterstitial);
}
