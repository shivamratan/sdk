package com.crackretail.sdk.interstitialads;

import android.content.Context;
import android.util.Log;

import com.crackretail.sdk.customevents.CustomEventData;
import com.crackretail.sdk.customevents.CustomEventInterstitial;
import com.crackretail.sdk.customevents.CustomEventInterstitialListener;
import com.crackretail.sdk.networking.AsyncCallback;
import com.crackretail.sdk.networking.CrackRetailRequest;
import com.crackretail.sdk.webview.CrackRetailWebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Shivam on 18-Jul-16.
 */
public class EventIterator
{
    List<CustomEventData> customDataList = new ArrayList();
    Context context;
    JSONObject adResp;
    JSONObject ad;
    Map<String, Object> params;
    String pixel;
    CrackRetailWebView crackRetailWebView;
    protected CustomEventInterstitialListener internalListener;

    public EventIterator(Context context,CrackRetailWebView crackRetailWebView,JSONObject adResp,Map<String,Object> params)
    {
        this.context=context;
        this.params=params;
        this.crackRetailWebView=crackRetailWebView;
        this.adResp=adResp;
        JSONArray customEvents=null;

        try {
            customEvents=adResp.getJSONArray("customEvents");
        } catch (JSONException e) {
            //e.printStackTrace();
            Log.e("CR_Interstitial","Iterator Parse Error");
        }

        if(customEvents!=null&&customEvents.length()>0)
        {
            for(int i=0;i<customEvents.length();++i)
            {
                String clazz = "";

                try {
                    JSONObject e = (JSONObject)customEvents.get(i);
                    CustomEventData customData = CustomEventData.parseJSON(e);
                    clazz = "com.crackretail.sdk.customevents." + customData.className + "Interstitial";
                    Class.forName(clazz);
                    this.customDataList.add(customData);
                } catch (JSONException exception) {
                    Log.e("CR_Interstitial", "iterator parse error", exception);
                } catch (ClassNotFoundException exception) {
                    Log.d("CR_Interstitial", "Custom Event class does not exist: " + clazz);
                }
            }
        }

    }


    public boolean hasNext(){
        return (this.customDataList.size()!=0||this.adResp.has("ad"));
    }

    public void callNextEvent(final CustomEventInterstitialListener listener){

        this.internalListener=new CustomEventInterstitialListener() {
            @Override
            public void onInterstitialLoaded(CustomEventInterstitial customEventInterstitial) {
                listener.onInterstitialLoaded(customEventInterstitial);
            }

            @Override
            public void onInterstitialFailed(CustomEventInterstitial customEventInterstitial, Exception exception) {

                listener.onInterstitialFailed(customEventInterstitial,exception);
            }

            @Override
            public void onInterstitialClosed(CustomEventInterstitial customEventInterstitial) {
                listener.onInterstitialClosed(customEventInterstitial);
            }

            @Override
            public void onInterstitialFinished() {
                listener.onInterstitialFinished();
            }

            @Override
            public void onInterstitialClicked(CustomEventInterstitial customEventInterstitial) {
                listener.onInterstitialClicked(customEventInterstitial);
            }

            @Override
            public void onInterstitialShown(CustomEventInterstitial customEventInterstitial) {

                if(EventIterator.this.pixel!=null){
                    CrackRetailRequest fireRequest=new CrackRetailRequest(EventIterator.this.context,EventIterator.this.pixel);
                    fireRequest.get((AsyncCallback)null);
                }
                else{
                    Log.d("CR_Interstitial","pixel is null");
                }

                listener.onInterstitialShown(customEventInterstitial);

            }
        };

        if(this.customDataList.size()>0){

            CustomEventData interstitialEvent=(CustomEventData)this.customDataList.get(0);
            this.customDataList.remove(0);

            try{
                Class myclass =Class.forName("com.crackretail.sdk.customevents."+interstitialEvent.className+"Interstitial");
                Constructor constructor=myclass.getConstructor(new Class[0]);
                CustomEventInterstitial customEventInterstitial=(CustomEventInterstitial)constructor.newInstance(new Object[0]);
                this.pixel=interstitialEvent.pixel;
                customEventInterstitial.loadInterstitial(this.context,this.internalListener,interstitialEvent.networkId,this.params);
            }
            catch (Exception exception)
            {
                Log.e("CR_Interstitial","Error creating customEvent",exception);
            }
        }
        else if(!this.adResp.has("ad"))
        {
            Log.d("CR_Interstitial","no ads to show");
        }
        else{

            InterstitialEvent interstitialEvent = new InterstitialEvent(this.context,this.adResp);
            interstitialEvent.loadInterstitial(this.context,this.internalListener,(String)null,(Map)null);

        }
    }

    public List<CustomEventData> getCustomDataList() {
        return this.customDataList;
    }

    public JSONObject getAd() {
        return this.ad;
    }


}
