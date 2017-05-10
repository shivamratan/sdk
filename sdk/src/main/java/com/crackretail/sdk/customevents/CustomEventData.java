package com.crackretail.sdk.customevents;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shivam on 02-Jul-16.
 */
public class CustomEventData
{
    public String pixel;
    public String className;
    public String networkId;

    public CustomEventData()
    {
    }

    //parse JSON objectto respective string of pixel,class & networkID
    public static CustomEventData parseJSON(JSONObject jsonObject)
    {
        CustomEventData customEventData=new CustomEventData();
        try
        {
            customEventData.pixel=jsonObject.getString("pixel");
            customEventData.className=jsonObject.getString("class");
            customEventData.networkId=jsonObject.getString("parameter");

        }
        catch (Exception exception)
        {
            customEventData=null;
            Log.e("CrackRetailError","Error occur while parsing Adresp JSON",exception);
        }


        return customEventData;
    }

    
    /* parse JSONArray to the List of CustomEventData objec containing
    *  the pixel, class & networkid using the parseJSON() method described above
     */
    public static List<CustomEventData> parseJSONArray(JSONArray jsonArray)
    {
        ArrayList<CustomEventData> eventDataArrayList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++)
        {
            CustomEventData temp=null;
            try {
                temp=parseJSON(jsonArray.getJSONObject(i));
            } catch (JSONException e) {
                continue;
            }

            if(temp!=null)
            {
                eventDataArrayList.add(temp);
            }
        }

        return eventDataArrayList;
    }



}
