package com.crackretail.sdk.networking;

import android.graphics.Bitmap;

import java.util.List;
import java.util.Map;

/**
 * Created by Shivam on 01-Jul-16.
 */
public interface AsyncCallbackBitmap
{
    void onComplete(int code,Bitmap bitmap,Map<String,List<String>> headers);
    void onError(Exception exception);
}
