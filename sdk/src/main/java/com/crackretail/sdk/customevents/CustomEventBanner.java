package com.crackretail.sdk.customevents;

import android.content.Context;

import java.util.Map;

/**
 * Created by Shivam on 02-Jul-16.
 */
public interface CustomEventBanner
{
        void loadAd(Context context,CustomEventBannerListener listener,String str,Map<String,Object> mymap);
}
