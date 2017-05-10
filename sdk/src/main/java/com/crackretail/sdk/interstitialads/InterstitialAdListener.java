package com.crackretail.sdk.interstitialads;

import com.crackretail.sdk.Interstitial;

/**
 * Created by Shivam on 18-Jul-16.
 */
public interface InterstitialAdListener
{
    void onInterstitialLoaded(Interstitial interstitial);

    void onInterstitialFailed(Interstitial interstitial, Exception exception);

    void onInterstitialClosed(Interstitial interstitial);

    void onInterstitialFinished();

    void onInterstitialClicked(Interstitial interstitial);

    void onInterstitialShown(Interstitial interstitial);
}
