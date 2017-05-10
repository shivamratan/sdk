package com.crackretail.sdk.dmp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.crackretail.sdk.constants.Constants;
import com.crackretail.sdk.networking.AsyncCallback;
import com.crackretail.sdk.networking.CrackRetailRequest;
import com.crackretail.sdk.webview.CrackRetailWebView;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by Shivam on 16-Jul-16.
 */
public class DMPManager
{
    Activity activity=null;
    Context context=null;
    CrackRetailWebView crackRetailWebView=null;
    String AdId=null;
    boolean adDoNotTrack;

    public DMPManager(Activity activity,Context context,CrackRetailWebView crackRetailWebView,String AdId,boolean adDoNotTrack)
    {
        this.activity=activity;
        this.context=context;
        this.crackRetailWebView=crackRetailWebView;
        this.AdId=AdId;
        this.adDoNotTrack=adDoNotTrack;
    }

   public void postDump()
    {
        dump mydump=dump.getInstance(activity,context,crackRetailWebView,AdId,adDoNotTrack);
        JSONObject jsonObject=mydump.prepareDump();

        CrackRetailRequest crackRetailRequest=new CrackRetailRequest(context, Constants.CRACKRETAIL_DUMP_URL);
        crackRetailRequest.setData(jsonObject);
        crackRetailRequest.setHeader("Content-type","application/json;charset=UTF-8");
        Log.d("DMP_DATA", jsonObject.toString());

        crackRetailRequest.post(new AsyncCallback() {
            @Override
            public void onComplete(int code, Object response, Map<String, List<String>> headers)
            {
                Calendar calendar=Calendar.getInstance();
                int nextupdatedayofyear=calendar.get(Calendar.DAY_OF_YEAR)+Constants.CRACKRETAIL_DUMP_FREQUENCY;
                int currentyear=calendar.get(Calendar.YEAR);


                String marker=nextupdatedayofyear+":"+currentyear;
                SharedPreferences mydumpprefs=DMPManager.this.context.getSharedPreferences("dumpprefs", DMPManager.this.context.MODE_PRIVATE);
                SharedPreferences.Editor editor=mydumpprefs.edit();
                editor.putString("dumpupdateflag",marker);
                editor.commit();


                Log.d("DMP_INFO","DUMP POSTED");
              //  Toast.makeText(context,"Data Dumped successfully!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Exception exception) {
                Log.e("DMP_ERROR",exception.toString());
            }
        });

    }


}
