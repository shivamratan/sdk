package com.crackretail.sdk.nativeads;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.crackretail.sdk.customevents.CustomEventNative;
import com.crackretail.sdk.customevents.CustomEventNativeListener;

import java.util.List;
import java.util.Map;

/**
 * Created by Shivam on 21-Jul-16.
 */
public class NativeEvent implements CustomEventNative
{
    private NativeAd nativeAd=null;
    private Context context=null;
    private CustomEventNativeListener customEventNativeListener=null;

    public NativeEvent(NativeAd nativeAd)
    {
        this.nativeAd=nativeAd;
    }

    @Override
    public void load(Context context, CustomEventNativeListener customEventNativeListener, String var3, List<Tracker> var4, Map<String, Object> var5)
    {
        this.context=context;
        this.customEventNativeListener=customEventNativeListener;
       if(this.nativeAd!=null)
        {
            nativeAd.loadImages(context,customEventNativeListener,this);
        }
        else{
           customEventNativeListener.onNativeError(new Exception("No Ad Found"));
         //   customEventNativeListener.onNativeReady(this,this.nativeAd);
        }



    }

    @Override
    public void registerViewForInteraction(View layout)
    {
        if(layout!=null)
        {
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                  try {
                      String clickURL = NativeEvent.this.nativeAd.getClickUrl();
                      Uri uri = Uri.parse(clickURL);
                      Intent intent = new Intent(Intent.ACTION_VIEW);
                      intent.setData(uri);
                      NativeEvent.this.context.startActivity(intent);
                  }
                  catch (Exception exception)
                  {
                      Log.e("CR_Native_error","Browser Activity Failed");
                  }

                    NativeEvent.this.customEventNativeListener.onNativeClicked(NativeEvent.this.nativeAd);


                }
            });
        }
        else{
            Log.e("CrackRetailNative","Layout is NULL");
            NativeEvent.this.customEventNativeListener.onNativeError(new Exception("Native AD layout is NULL"));
        }

    }
}
