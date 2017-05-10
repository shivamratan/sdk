package com.crackretail.sdk.bannerads;

import android.app.usage.UsageEvents;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.crackretail.sdk.customevents.CustomEventBanner;
import com.crackretail.sdk.customevents.CustomEventBannerListener;
import com.crackretail.sdk.customevents.CustomEventData;
import com.crackretail.sdk.networking.AsyncCallback;
import com.crackretail.sdk.networking.CrackRetailRequest;
import com.crackretail.sdk.webview.CrackRetailWebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Shivam on 02-Jul-16.
 */
public class EventIterator
{
    Context context;
    List<CustomEventData> customEventDataList=new ArrayList<>();
    JSONObject respObject;
    String pixel;
    Map<String,Object> params;
    CrackRetailWebView crackRetailWebView=null;

    public EventIterator(Context context,CrackRetailWebView crackRetailWebView,JSONObject respObject,Map<String,Object> params)
    {
        this.context=context;
        this.params=params;
        this.crackRetailWebView=crackRetailWebView;
        this.respObject=respObject;
        JSONArray customevents=null;


        try {
            customevents=respObject.getJSONArray("customEvents");
        } catch (JSONException e) {
            e.printStackTrace();
        }

       this.customEventDataList=new ArrayList<>();
       if(customevents!=null&& customevents.length()>0)
       {
           for(int i=0;i<customevents.length();i++)
           {
               try {
                   JSONObject jsonobj=(JSONObject)customevents.getJSONObject(i);
                   CustomEventData customEventData=CustomEventData.parseJSON(jsonobj);
                   Class.forName("com.crackretail.sdk.customevents." + customEventData.className);
                   customEventDataList.add(customEventData);

               } catch (JSONException e) {
                   e.printStackTrace();
               } catch (ClassNotFoundException e) {
                   e.printStackTrace();
               }

           }
       }
    }


   public boolean hasNext()
   {
       return customEventDataList.size()!=0||this.respObject.has("ad") || this.respObject.has("vasts");
   }

   public  void callNextEvent(final CustomEventBannerListener listener)
   {
       CustomEventBannerListener customEventBannerListener=new CustomEventBannerListener() {
           @Override
           public void onBannerError(View view, Exception exception) {
               listener.onBannerError(view,exception);
           }

           @Override
           public void onBannerLoaded(View view) {

              listener.onBannerLoaded(view);
               if(EventIterator.this.pixel!=null)
               {
                   CrackRetailRequest firePixel=new CrackRetailRequest(EventIterator.this.context,EventIterator.this.pixel);
                   firePixel.get(((AsyncCallback)null));
               }
               else
               {
                   Log.d("CrackRetailBanner","Pixel is NULL");
               }

           }

           @Override
           public void onBannerClosed(View view) {
                listener.onBannerClosed(view);
           }

           @Override
           public void onBannerFinished() {
                listener.onBannerFinished();
           }

           @Override
           public void onBannerClicked(View view) {
                listener.onBannerClicked(view);
           }
       };

    if(this.customEventDataList.size()>0) {

        CustomEventData bannerEvent=(CustomEventData)this.customEventDataList.get(0);
        customEventDataList.remove(0);


        try {
            Class myclass=Class.forName("com.crackretail.sdk.customevents."+bannerEvent.className);
            Constructor constructor=myclass.getConstructor();
            CustomEventBanner customEventBanner=(CustomEventBanner)constructor.newInstance();
            this.pixel=bannerEvent.pixel;
            customEventBanner.loadAd(this.context,customEventBannerListener,bannerEvent.networkId,this.params);

        } catch (ClassNotFoundException e) {
           Log.d("CrackRetailError","Banner Iterator Class not found Error");
        } catch (NoSuchMethodException e) {
            Log.d("CrackRetailError","Banner Iterator Method not found Error");
        } catch (InvocationTargetException e) {
            Log.d("CrackRetailError", "Banner Iterator Error");;
        } catch (InstantiationException e) {
            Log.d("CrackRetailError", "Banner Iterator Error");
        } catch (IllegalAccessException e) {
            Log.d("CrackRetailError", "Banner Iterator Error");
        }

    }
    else
    {
        this.pixel = null;
        BannerEvent bannerEvent = new BannerEvent(this.crackRetailWebView, this.respObject);
        bannerEvent.loadAd(this.context,customEventBannerListener,"https://www.facebook.com/crackretail", (Map)null);
    }

   }
}
