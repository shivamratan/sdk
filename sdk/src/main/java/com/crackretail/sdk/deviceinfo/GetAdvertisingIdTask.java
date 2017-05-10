package com.crackretail.sdk.deviceinfo;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;


/**
 * Created by Shivam on 30-Jun-16.
 */
public class GetAdvertisingIdTask extends AsyncTask<Void,Void,String>
{
   Context context;
   GetIdTask.AdIdCallBack callBack=null;
    String ADV_id="";
    boolean adDoNotTrack;

   public GetAdvertisingIdTask(Context context,GetIdTask.AdIdCallBack callBack)
   {
       this.context=context;
       this.callBack=callBack;
   }

    @Override
    protected String doInBackground(Void... params)
    {
        try
        {
           AdvertisingIdClient.Info info=AdvertisingIdClient.getAdvertisingIdInfo(context);
           ADV_id=info.getId();
           adDoNotTrack=info.isLimitAdTrackingEnabled();

            return ADV_id;
        }
        catch(Exception e)
        {
            Log.e("CrackRetailBanner","Google Play Error",e);
            return "";
        }


    }

    @Override
    protected void onPostExecute(String s) {

        callBack.onSuccess(ADV_id,adDoNotTrack);
    }
}
