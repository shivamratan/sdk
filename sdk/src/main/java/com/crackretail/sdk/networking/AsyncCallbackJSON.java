package com.crackretail.sdk.networking;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by Shivam on 01-Jul-16.
 */
public interface AsyncCallbackJSON
{
    void onComplete(int code,JSONObject jsonObject,Map<String,List<String>> headers);
    void onError(Exception exception);
}
