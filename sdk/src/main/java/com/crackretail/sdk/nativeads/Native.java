package com.crackretail.sdk.nativeads;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;

import com.crackretail.sdk.constants.Constants;
import com.crackretail.sdk.customevents.CustomEventNative;
import com.crackretail.sdk.customevents.CustomEventNativeListener;
import com.crackretail.sdk.deviceinfo.GetAdvertisingIdTask;
import com.crackretail.sdk.deviceinfo.GetIdTask;
import com.crackretail.sdk.deviceinfo.NetworkInfo;
import com.crackretail.sdk.dmp.DMPManager;
import com.crackretail.sdk.networking.AsyncCallback;
import com.crackretail.sdk.networking.AsyncCallbackJSON;
import com.crackretail.sdk.networking.CrackRetailRequest;
import com.crackretail.sdk.utils.Utils;
import com.crackretail.sdk.webview.CrackRetailWebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Shivam on 21-Jul-16.
 */
public class Native
{
    NativeListener listener=null;
    private Context context=null;
    private Native self=null;
    private Handler handler=null;
    private String inventoryHash=null;
    private static String userAgent = System.getProperty("http.agent");
    ViewGroup viewForInteraction;
    EventIterator iterator;
    public NativeInitTasks initTasks=null;
    public NativeMakeTasks makeTasks=null;
    private String waterfalls;
    private String sub_bundle_id;
    private String o_andadvid;
    private boolean adDoNotTrack;
    private boolean readyToLoad=false;

    public Native(Context context)
    {
       this.context = context;
       this.self = this;
       this.handler = new Handler(context.getMainLooper());
        native_setup();

    }

    private void native_setup()
    {
        initTasks=new NativeInitTasks(new NativeInitTasks.DoneCallback() {
            @Override
            public void onDone() {

                Native.this.__Prim_Task();
            }
        });


        makeTasks=new NativeMakeTasks(new NativeMakeTasks.DoneCallback() {
            @Override
            public void onDone() {

                Native.this.readyToLoad=true;
                appearNativeAd();

            }
        });





    }


    public void load()
    {

        appearNativeAd();
  /*      new Thread(new Runnable() {
            @Override
            public void run() {

                if(checkConnection.step2hotspotcheck(Native.this.context))
                {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {


                        }
                    });

                }
                else
                {

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(context, "Sorry! Internet not Available To Load Ad", Toast.LENGTH_LONG).show();
                        }
                    });

                }


            }
        }).start();*/
    }

    private void appearNativeAd()
    {
        if(Native.this.inventoryHash==null||Native.this.inventoryHash.length()==0)
        {
            Log.e("CR_NativeAdError","Please setinventoryHash");
        }
        else
        {
            if(this.self.readyToLoad)
            {
               this.self.loadNativeAd();
            }
            else
            {
                init();
            }
        }

    }


    private void loadNativeAd()
    {
        JSONObject jsonObject=makeParams();
       CrackRetailRequest crackRetailRequest=new CrackRetailRequest(Native.this.context,Constants.CRACKRETAIL_AD_JSON_URL);
        crackRetailRequest.setData(jsonObject);
         crackRetailRequest.getJSON(new AsyncCallbackJSON() {
             @Override
             public void onComplete(int code, JSONObject jsonObject, Map<String, List<String>> headers)
             {
                 Log.d("CR_Native_json","onComplete of accessing native json url");
                 Native.this.iterator=EventIterator.parse(Native.this.context,jsonObject,headers,new HashMap<String, Object>());
                 Native.this.handleEvent();
             }

             @Override
             public void onError(Exception exception)
             {
                  Log.e("CrackRetailNative","Custom native Failed",exception);
                  Native.this.listener.onNativeError(exception);
             }
         });
    }


    private void init()
    {
        this.getADV_id();
        this.postDMP();
        this.getBundleId(Native.this.context);
    }


    private void handleEvent()
    {
        Log.d("CR_NativeAds", "Handling Event Listener");
        final CustomEventNativeListener customEventNativeListener=new CustomEventNativeListener() {
            @Override
            public void onNativeReady(CustomEventNative customEventNative, NativeAd nativeAd) {

                if(Native.this.listener!=null)
                {
                    try {
                        Native.this.listener.onNativeReady(Native.this, customEventNative, nativeAd);
                    }
                    catch(Exception exception)
                    {
                        Log.e("CR_Native","error in Listener",exception);
                    }
                }

            }

            @Override
            public void onNativeError(Exception exception)
            {
                Log.e("CR_ERROR","Custom Native Error",exception);
                if(Native.this.iterator.hasNext())
                {
                    Native.this.iterator.callNextEvent(this);
                }
                else{
                    Log.d("CR_Native","no more Custom Events");
                    if(Native.this.listener!=null)
                    {
                        try {
                            Native.this.listener.onNativeError(exception);
                        }
                        catch(Exception exception1)
                        {
                            Log.e("CR_ERROR","native listener failed",exception1);
                        }
                    }
                }

            }

            @Override
            public void onNativeClicked(NativeAd nativeAd)
            {
                if(Native.this.listener!=null)
                {
                    try{
                        Native.this.listener.onNativeClick(nativeAd);
                    }
                    catch(Exception exception)
                    {
                        Log.e("CR_NativeAD","Listener error",exception);
                    }
                }
            }
        };

        if(this.iterator.hasNext()) {
            this.iterator.callNextEvent(customEventNativeListener);
        } else {
            this.listener.onNativeError(new Exception("no native ad returned"));
        }


    }





    public void getADV_id()
    {
        GetAdvertisingIdTask getAdvertisingIdTask=new GetAdvertisingIdTask(Native.this.context, new GetIdTask.AdIdCallBack() {
            @Override
            public void onSuccess(String adId, boolean adDoNotTrack) {

                if(adId!=null)
                {
                    Native.this.o_andadvid=adId;
                    Native.this.adDoNotTrack=adDoNotTrack;
                    Log.d("CrackRetailBanner","andro_AdId: "+adId);
                }
                else
                {
                    Log.e("CrackRetailBannerError","andro_AdId"+new Exception("Andro_Advertisement Id returned null"));
                }

                Native.this.initTasks.notifyTaskDone(NativeInitTasks.Tasks.GET_ADVERTISING_ID);

            }
        });
        getAdvertisingIdTask.execute();
    }



    private void postDMP(){


        Calendar calendar=Calendar.getInstance();
        int currentdayofyear=calendar.get(Calendar.DAY_OF_YEAR);
        int currentyear=calendar.get(Calendar.YEAR);

        String marker=currentdayofyear+":"+currentyear;
        SharedPreferences mydumpprefs=Native.this.context.getSharedPreferences("dumpprefs", Native.this.context.MODE_PRIVATE);
        String updateflag=mydumpprefs.getString("dumpupdateflag", marker);

        String nextmarker[]=updateflag.split(Pattern.quote(":"));
        int nextupdatedayofyear=Integer.parseInt(nextmarker[0].trim());
        int nextupdateyear=Integer.parseInt(nextmarker[1].trim());

       // Log.e("DUMP_UPDATE_DATA","Day of year= "+currentdayofyear+"/"+nextupdatedayofyear+" Year="+currentyear+"/"+nextupdateyear);
        if(currentdayofyear>=nextupdatedayofyear) {
            Activity activity=(Activity)context;
            DMPManager dmpManager=new DMPManager(activity,context,new CrackRetailWebView(Native.this.context,(CrackRetailWebView.Listener)null),o_andadvid,adDoNotTrack);

            dmpManager.postDump();
        }
        else
        {
            if(currentyear!=nextupdateyear)
            {
                Activity activity=(Activity)context;
                DMPManager dmpManager=new DMPManager(activity,context,new CrackRetailWebView(Native.this.context,(CrackRetailWebView.Listener)null),o_andadvid,adDoNotTrack);

                dmpManager.postDump();
            }
            else
            {
                Log.d("DMP_UPDATE_DATA","Day of year= "+currentdayofyear+"/"+nextupdatedayofyear+" Year="+currentyear+"/"+nextupdateyear);
               // Toast.makeText(Native.this.context,"Sorry! Today dump has already updated",Toast.LENGTH_LONG).show();
            }
        }





    }

    public String getBundleId(Context context)
    {
        try {
            this.self.sub_bundle_id = context.getPackageName();
            Log.d("MobFoxBanner", "bundle id: " + this.sub_bundle_id);
            return this.self.sub_bundle_id;
        } catch (Exception var3) {
            Log.e("MobFoxBanner", "bundle error", var3);
            return "";
        }
    }

    private JSONObject makeParams()
    {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("inv_hash",inventoryHash);
            jsonObject.put("ad_type","native");
            jsonObject.put("package_id",this.sub_bundle_id);
            jsonObject.put("ad_id", o_andadvid+"");
            jsonObject.put("screen_density",(Utils.getDensity(Native.this.context))+"");
            jsonObject.put("advert_id",this.o_andadvid.toString());
            String sha1hashmac=Utils.SHA1((new NetworkInfo(Native.this.context)).getWifiMAC());
            jsonObject.put("hash",sha1hashmac);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }



    private void __Prim_Task()
    {
        this.getWaterfall();
    }


    private void getWaterfall()
    {
        //Checking if there is existance of JSON at local cache
        if(Utils.read(this.context, "crack-retail-waterfalls-file")!=null)
        {
            //Accessing the JSON from the local Cache
            String waterfalls= Utils.read(this.context,"crack-retail-waterfalls-file");
            this.makeTasks.notifyTaskDone(NativeMakeTasks.Tasks.GET_WATERFALLS);
        }
        else
        {
            CrackRetailRequest request=new CrackRetailRequest(this.context, Constants.WATERFALLS_URL);
            request.setParam("p",this.inventoryHash);
            request.get(new AsyncCallback() {
                @Override
                public void onComplete(int code, Object response, Map<String, List<String>> headers) {
                    Log.d("CrackRetailNative_Net", "on Complete Done");
                    Native.this.waterfalls=response.toString();
                    Utils.write(Native.this.context,"crack-retail-waterfalls-file",Native.this.waterfalls);
                    Native.this.makeTasks.notifyTaskDone(NativeMakeTasks.Tasks.GET_WATERFALLS);
                }

                @Override
                public void onError(Exception exception) {
                    Log.e("CrackRetail","on Error",exception);

                }
            });
        }
    }




    public void setListener(NativeListener listener) {
        this.listener = listener;
    }

    public void setInventoryHash(String inventoryHash) {
        this.inventoryHash=inventoryHash;
    }




}
