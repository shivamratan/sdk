package com.crackretail.sdk.nativeads;

import android.content.Context;
import android.util.Log;

import com.crackretail.sdk.customevents.CustomEventData;
import com.crackretail.sdk.customevents.CustomEventNative;
import com.crackretail.sdk.customevents.CustomEventNativeListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Shivam on 22-Jul-16.
 */
public class EventIterator
{
    private List<CustomEventData> data;
    private NativeAd nativeAd;
    private Context context;
    private Map<String, Object> params;


    public EventIterator(Context context, List<CustomEventData> data, NativeAd nativeAd, Map<String, Object> params)
    {
        this.context=context;
        this.data=data;
        this.nativeAd=nativeAd;
        this.params=params;
        ArrayList nonexistantclass=new ArrayList();
        Iterator iterator=data.iterator();


        while(iterator.hasNext()){
            CustomEventData customEventData=(CustomEventData)iterator.next();
            String classname="com.crackretail.sdk.customevents."+customEventData.className+"Native";

            try {
                Class.forName(classname);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                nonexistantclass.add(customEventData);
            }
        }

        this.data.removeAll(nonexistantclass);

    }

    public boolean hasNext()
    {
        return this.data.size()!=0||this.nativeAd!=null;
    }

    public void callNextEvent(CustomEventNativeListener customEventNativeListener)
    {
        if(this.data.size()>0){
            CustomEventData customEventData=(CustomEventData)this.data.get(0);
            this.data.remove(0);


            try{
                Class classname=Class.forName("com.crackretail.sdk.customevents."+customEventData.className+"Native");
                Constructor constructor=classname.getConstructor(new Class[0]);
                CustomEventNative customEventNative=(CustomEventNative)constructor.newInstance(new Object[0]);
                ArrayList extraTrackers=new ArrayList();
                extraTrackers.add(new Tracker("impression",customEventData.pixel));
                customEventNative.load(this.context,customEventNativeListener,customEventData.networkId,extraTrackers,this.params);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            finally{
                return;
            }

        }
        else{
            if(this.nativeAd!=null)
            {
                NativeEvent nativeEvent=new NativeEvent(this.nativeAd);
                nativeEvent.load(this.context,customEventNativeListener,(String)null,(List)null,(Map)null);
            }
        }



    }


    public static EventIterator parse(Context context,JSONObject response,Map<String,List<String>> headers,Map<String,Object> params)
    {
        NativeAd nativeAd=NativeAd.parse(response);
        ArrayList customEvents=new ArrayList();

        if(headers!=null)
        {
            Iterator iterator=headers.keySet().iterator();
            String header=(String)iterator.next();
            if(header!=null&&header.indexOf("CustomEvent")==0)
            {
                List values=(List)headers.get(header);
                if(values.size()>0)
                {
                    try {
                        CustomEventData customEventData=CustomEventData.parseJSON(new JSONObject((String)values.get(0)));
                    } catch (JSONException e) {
                     //  e.printStackTrace();
                        Log.e("CrackRetailNative","Failed to Parse CustomEvent",e);
                    }
                }
            }
        }


        return new EventIterator(context,customEvents,nativeAd,params);
    }





}
