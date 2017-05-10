package com.crackretail.sdk.bannerads;

import android.view.View;

/**
 * Created by Shivam on 29-Jun-16.
 */
public interface BannerListener
{
    void onBannerError(View view, Exception exception);

    void onBannerLoaded(View view);

    void onBannerClosed(View view);

    void onBannerFinished();

    void onBannerClicked(View view);

    void onNoFill(View view);
}
