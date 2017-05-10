package com.crackretail.sdk.networking;

import java.util.List;
import java.util.Map;

/**
 * Created by Shivam on 01-Jul-16.
 */
public interface AsyncCallback
{
    void onComplete(int code,Object response,Map<String,List<String>> headers);
    void onError(Exception exception);
}
