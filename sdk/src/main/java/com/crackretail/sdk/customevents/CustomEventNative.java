package com.crackretail.sdk.customevents;

import android.content.Context;
import android.view.View;

import com.crackretail.sdk.nativeads.Tracker;

import java.util.List;
import java.util.Map;

/**
 * Created by Shivam on 21-Jul-16.
 */
public interface CustomEventNative
{
    void load(Context var1, CustomEventNativeListener var2, String var3, List<Tracker> var4, Map<String, Object> var5);

    void registerViewForInteraction(View var1);
}
