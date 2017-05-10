package com.crackretail.sdk.customevents;

import android.content.Context;
import android.view.View;

import java.util.Map;

/**
 * Created by Shivam on 02-Jul-16.
 */
public interface CustomEventBannerListener
{
   void onBannerError(View view,Exception exception);
   void onBannerLoaded(View view);
   void onBannerClosed(View view);
   void onBannerFinished();
   void onBannerClicked(View view);
}
