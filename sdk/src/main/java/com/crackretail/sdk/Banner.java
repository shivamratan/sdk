package com.crackretail.sdk;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.crackretail.sdk.bannerads.BannerInitTasks;
import com.crackretail.sdk.bannerads.BannerListener;
import com.crackretail.sdk.bannerads.BannerMakeTasks;
import com.crackretail.sdk.bannerads.EventIterator;
import com.crackretail.sdk.constants.Constants;
import com.crackretail.sdk.customevents.CustomEventBannerListener;
import com.crackretail.sdk.deviceinfo.GetAdvertisingIdTask;
import com.crackretail.sdk.deviceinfo.GetIdTask;
import com.crackretail.sdk.deviceinfo.NetworkInfo;
import com.crackretail.sdk.dmp.DMPManager;
import com.crackretail.sdk.networking.AsyncCallback;
import com.crackretail.sdk.networking.CrackRetailRequest;
import com.crackretail.sdk.utils.Utils;
import com.crackretail.sdk.webview.CrackRetailWebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Banner extends RelativeLayout
{
    private final Context context;
    private int adspace_width=0;
    private int adspace_height=0;
    private Banner self=null;
    private Handler handler=null;
    private String inv_hash="";
    private BannerListener listener=null;
    private String type="waterfall";
    private String adFormat="banner";
    private boolean autoplay=true;
    private String demo_gender="";
    private String demo_keywords="";
    private String no_markup="";
    private boolean auto_pilot=true;
    private int refresh=0;
    private int adspace_strict=0;
    private int v_dur_min=0;
    private int v_dur_max=0;
    private int r_floor=0;
    private String rt="android_app";
    private boolean skip=false;
    private boolean debug=false;
    private boolean secure=false;
    private boolean start_muted=false;
    private int demo_age=0;
    private Location location=null;
    private boolean readyToLoad=false;
    private BannerMakeTasks makeTasks=null;
    private BannerInitTasks initTasks=null;
    public boolean isInterstitial=false;
    private String sub_bundle_id="";
    private String o_andadvid="";
    private boolean adDoNotTrack;
    private CrackRetailWebView.Listener webViewListener=null;
    private CrackRetailWebView crackRetailWebView=null;
    private String waterfalls="";
    private EventIterator iterator;
    private int current_frequency=0;
    private int maximum_frequency=0;
    private boolean isActivityVisible=true;
  //  private Handler handler=new Handler();


    // first constructor of relative layout
    public Banner(Context context) {
        super(context);
        this.context = context;
        this.self = this;
        this.handler = new Handler(context.getMainLooper());
        this.crackretail_setup();

    }


// second constructor of relative layout
    public Banner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.self = this;
        this.handler = new Handler(context.getMainLooper());
        adspace_width=attrs.getAttributeIntValue(android.R.attr.layout_width,0);
        adspace_width=attrs.getAttributeIntValue(android.R.attr.layout_height,0);
        this.crackretail_setup();
    }


 // third constructor of relative layout
    public Banner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.self = this;
        this.handler = new Handler(context.getMainLooper());
        this.crackretail_setup();
    }

 // fourth constructor of relative layout
    public Banner(Context context, int width, int height) {
        super(context);
        this.context = context;
        this.adspace_width = width;
        this.adspace_height = height;
        this.self = this;
        this.handler = new Handler(context.getMainLooper());
        this.crackretail_setup();
    }


    public Banner(Context context, int width, int height,boolean isInterstitial) {
        super(context);
        this.isInterstitial=isInterstitial;
        this.context = context;
        this.adspace_width = width;
        this.adspace_height = height;
        this.self = this;
        this.handler = new Handler(context.getMainLooper());
        this.crackretail_setup();
    }



    public void crackretail_setup()
   {
       //defining onDone() method for bannerinit()
       initTasks=new BannerInitTasks(new BannerInitTasks.DoneCallback() {
           @Override
           public void onDone()
           {
            Log.d("CrackRetailBanner","Banner Init Done");
            Banner.this.make();

           }
       });

       //defining onDone() method of bannermaketasks()
       makeTasks=new BannerMakeTasks(new BannerMakeTasks.DoneCallback() {
           @Override
           public void onDone()
           {
             Banner.this.readyToLoad=true;
              Banner.this.load(Banner.this.inv_hash);
           }
       });


       if(!Banner.this.isInterstitial) {
           this.webViewListener = new CrackRetailWebView.Listener() {
               @Override
               public void onReady(CrackRetailWebView webView) {
                   Banner.this.initTasks.notifyTaskDone(BannerInitTasks.Tasks.LOAD_JS);

               }

               @Override
               public void onError(CrackRetailWebView webView, Exception exception) {

               }

               @Override
               public void onAdClick(CrackRetailWebView webView, String str) {

               }

               @Override
               public void onVideoAdFinished(CrackRetailWebView webView) {

               }

               @Override
               public void onAdClosed(CrackRetailWebView webView) {

               }

               @Override
               public void onAdResponse(CrackRetailWebView webView, final JSONObject adResp) {
             /*   if (adResp.has("vasts")) {
                    String err;
                    String audioName;
                    try {

                        String params = Utils.getVideoUrl(adResp);
                        err = params.substring(params.lastIndexOf("/") + 1);
                        audioName = Banner.this.context.getCacheDir() + "/" + err;

                        if (Utils.videoExists(audioName)) {
                            adResp = Utils.replaceAudioCached(audioName, adResp);
                        } else {
                            DownloadTask audioCache = new DownloadTask(Banner.this.context) {
                                @Override
                                protected void onPostExecute(String s) {
                                    Log.d("CrackRetailBanner", "Downloaded To cache");
                                }
                            };

                            audioCache.execute((params));
                        }

                    } catch (Exception exception) {
                        Log.e("CrackRetailError", "Error occurred on loading Video", exception);
                    }
                }*/
                   //CrackRetail Banner width,height HashMap wrapping
                   HashMap hashParam = new HashMap();
                   hashParam.put("width", Integer.valueOf(Banner.this.adspace_width));
                   hashParam.put("height", Integer.valueOf(Banner.this.adspace_height));
                   Banner.this.iterator = new EventIterator(Banner.this.context, crackRetailWebView, adResp, hashParam);
                   if (Banner.this.iterator.hasNext()) {
                       Banner.this.iterator.callNextEvent(new CustomEventBannerListener() {
                           @Override
                           public void onBannerError(View view, Exception exception) {

                               if (exception.getMessage().equals("onAutoRedirect")) {
                                   Banner.this.load();
                               } else if (exception.getMessage().equals("onNoAd")) {
                                   if (Banner.this.listener != null) {
                                       Banner.this.listener.onNoFill(Banner.this.self);
                                   }
                               } else {
                                   Log.e("CrackRetailBanner", "Banner Iterator", exception);
                                   if (Banner.this.iterator.hasNext()) {
                                       Banner.this.iterator.callNextEvent(this);
                                   } else if (Banner.this.listener != null) {
                                       Banner.this.listener.onBannerError(view, exception);
                                   }
                               }

                           }

                           @Override
                           public void onBannerLoaded(View view) {

                               Log.d("CrackRetailBanner", "banner loaded");
                             if(Banner.this.isActivityVisible) {
                                 Banner.this.show(view);
                                 if (Banner.this.listener != null) {
                                     Banner.this.listener.onBannerLoaded(view);

                                 }
                             }

                               //checking for refresh decision
                               if(!Banner.this.isInterstitial)
                               {
                                   Banner.this.decideforRefresh(adResp);
                                  // Toast.makeText(Banner.this.context,"Refreshing",Toast.LENGTH_LONG).show();
                               }
                               else
                               {
                                  // Toast.makeText(Banner.this.context,"NOT ! Refreshing",Toast.LENGTH_LONG).show();
                               }
                           }

                           @Override
                           public void onBannerClosed(View view) {

                               Log.d("CrackRetailBanner", "Banner Closed");
                               Banner.this.self.removeAllViews();
                               if (Banner.this.listener != null) {
                                   Banner.this.listener.onBannerClosed(view);
                               }

                           }

                           @Override
                           public void onBannerFinished() {
                               Log.d("CrackRetailBanner", "banner Finished");
                               if (Banner.this.listener != null) {
                                   Banner.this.listener.onBannerFinished();
                               }
                           }

                           @Override
                           public void onBannerClicked(View view) {

                               Log.d("CrackRetailBanner", "Banner Clicked!");
                               if (Banner.this.listener != null) {
                                   Banner.this.listener.onBannerClicked(view);
                               }
                           }
                       });
                   }


               }

               @Override
               public void onAdLoaded(CrackRetailWebView webView) {
             /*   Banner.this.removeAllViews();
                if (webView != null)
                {
                    Banner.this.addView(webView);
                }
            */

               }

               @Override
               public void onNoAd(CrackRetailWebView webView) {

               }

               @Override
               public void onAutoDirect(CrackRetailWebView webView, String str) {

               }
           };


       }



   }


   protected void show(View banner) {
        this.self.removeAllViews();
        this.self.addView(banner);
    }

    private void make()
    {
       this.getWaterfall();
    }

    private void getWaterfall()
    {
        //Checking if there is existance of JSON at local cache
        if(Utils.read(this.context,"crack-retail-waterfalls-file")!=null)
        {
          //Accessing the JSON from the local Cache
           String waterfalls= Utils.read(this.context,"crack-retail-waterfalls-file");
            this.crackRetailWebView.setWaterfalls(waterfalls);
           this.makeTasks.notifyTaskDone(BannerMakeTasks.Tasks.GET_WATERFALLS);
        }
        else
        {
            CrackRetailRequest request=new CrackRetailRequest(this.context,Constants.WATERFALLS_URL);
            request.setParam("p",this.inv_hash);
            request.get(new AsyncCallback() {
                @Override
                public void onComplete(int code, Object response, Map<String, List<String>> headers) {
                    Log.d("CrackRetailBann_Net","on Complete Done");
                    Banner.this.waterfalls=response.toString();
                    Utils.write(Banner.this.context,"crack-retail-waterfalls-file",Banner.this.waterfalls);
                    Banner.this.makeTasks.notifyTaskDone(BannerMakeTasks.Tasks.GET_WATERFALLS);
                }

                @Override
                public void onError(Exception exception) {
                    Log.e("CrackRetail","on Error",exception);

                }
            });
        }

    }

    private void init()
    {
        getADV_id();//Returning the Advertisement Id
        Load_WebViewUtility(); //Loading the WebView Utility like html files and js files
        getLayout(); //restructuring the input width & height
       // getLocation(initTasks); //getting the location
        this.postDMP(); //dumping the required info to the server
        this.getBundleId(this.context); //getting the package name or Bundle ID

    }


   public void load()
    {

        appearBannerAd();
    /*   new Thread(new Runnable() {
           @Override
           public void run() {

               if(checkConnection.step2hotspotcheck(Banner.this.context))
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



    public void appearBannerAd()
    {
        if(this.self.inv_hash==null||this.self.inv_hash.length()==0)
        {
            Log.d("CrackRetailBanner_Error","Please set Inventory hash");
            if(this.self.listener==null)
            {
                return;
            }
        }

        if(this.self.readyToLoad)
        {
          if(!this.isInterstitial)
          {
            this.crackRetailWebView.setListener(this.webViewListener);
          }
            Banner.this.load(inv_hash);
        }
        else
        {
           init();
        }

    }

    private void load(String hash)
    {
        JSONObject params=this.makeParams();
        if(params!=null)
        {
            loadBanner(params);
        }

    }

    private void loadBanner(JSONObject jsonObj)
    {
        try
        {
            crackRetailWebView.loadAd(jsonObj.toString());
        }
        catch(Exception exception)
        {
            Log.e("CrackRetailBanner","Webview LoadBanner error",exception);
            if(this.listener==null)
                return;

            this.listener.onBannerError(this.self, exception);
        }

    }


    private void postDMP(){

        Calendar calendar=Calendar.getInstance();
        int currentdayofyear=calendar.get(Calendar.DAY_OF_YEAR);
        int currentyear=calendar.get(Calendar.YEAR);

        String marker=currentdayofyear+":"+currentyear;
        SharedPreferences mydumpprefs=Banner.this.context.getSharedPreferences("dumpprefs", Banner.this.context.MODE_PRIVATE);
        String updateflag=mydumpprefs.getString("dumpupdateflag", marker);

        String nextmarker[]=updateflag.split(Pattern.quote(":"));
        int nextupdatedayofyear=Integer.parseInt(nextmarker[0].trim());
        int nextupdateyear=Integer.parseInt(nextmarker[1].trim());

    //  Log.e("DUMP_UPDATE_DATA","Day of year= "+currentdayofyear+"/"+nextupdatedayofyear+" Year="+currentyear+"/"+nextupdateyear);
      if(currentdayofyear>=nextupdatedayofyear) {
          Activity activity = (Activity) context;
          DMPManager dmpManager = new DMPManager(activity, context, crackRetailWebView, o_andadvid, adDoNotTrack);

          dmpManager.postDump();
      }
      else
      {
         if(currentyear!=nextupdateyear)
         {
             Activity activity = (Activity) context;
             DMPManager dmpManager = new DMPManager(activity, context, crackRetailWebView, o_andadvid, adDoNotTrack);

             dmpManager.postDump();
         }
          else
         {
             Log.d("DMP_DATA","Day of year= "+currentdayofyear+"/"+nextupdatedayofyear+" Year="+currentyear+"/"+nextupdateyear);
            // Toast.makeText(Banner.this.context,"Sorry! Today dump has already updated",Toast.LENGTH_LONG).show();
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

    @TargetApi(16)
    private void getLayout()
    {
         if(this.adspace_width>0&&this.adspace_height>0)
         {
             this.initTasks.notifyTaskDone(BannerInitTasks.Tasks.GET_LAYOUT);
             Log.d("CrackRetailBanner", "adspace_width: " + adspace_width + " adspace_height: " + adspace_height);
         }
        else
         {

            final DisplayMetrics displayMetrics=this.context.getResources().getDisplayMetrics();
             try {
                 int width = (int) ((float) this.self.getWidth() / displayMetrics.density);
                 int height = (int) ((float) this.self.getHeight() / displayMetrics.density);

                 if (width > 0 && height > 0)
                 {
                     this.adspace_width=width;
                     this.adspace_height=height;
                     this.initTasks.notifyTaskDone(BannerInitTasks.Tasks.GET_LAYOUT);
                     Log.d("CrackRetailBanner", "adspace_width: " + adspace_width + " adspace_height: " + adspace_height);

                     return;
                 }

             }
             catch(Exception exception)
             {
                    Log.e("CrackRetailBanner","CrackRetailBanner_Error: ",exception);
             }

          /*
               ViewTreeObserver viewTreeObserver=this.self.getViewTreeObserver();
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        Banner.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        int width = (int) ((float) Banner.this.self.adspace_width / displayMetrics.density);
                        int height = (int) ((float) Banner.this.self.adspace_height / displayMetrics.density);

                        if (Banner.this.self.adspace_height > 245 && Banner.this.adspace_height < 255) {
                            Banner.this.self.adspace_height = 250;
                        }

                        (Banner.this).initTasks.notifyTaskDone(BannerInitTasks.Tasks.GET_LAYOUT);
                        Log.d("CrackRetailBanner", "adspace_width: " + adspace_width + " adspace_height: " + adspace_height);

                    }
                });

                */
         }
    }


/*

  //commented for future use of Location Module in the SDK
    protected  void getLocation(final BannerInitTasks initTasks)
    {
        if(Utils.checkPermission(this.context,"ACCESS_FINE_LOCATION"))
        {
            GetLocationTask getLocationTask=new GetLocationTask(this.context, new GetLocationTask.LocationTaskListener() {
                @Override
                public void onLocationReady(Location location) {

                    if (location == null)
                    {
                        Log.d("CrackRetailBannerError","Get location Exception occured");
                        initTasks.notifyTaskDone(BannerInitTasks.Tasks.GET_LOCATION);
                    }
                    else
                    {
                        Log.d("CrackRetailBanner","Latitude: "+location.getLatitude()+" Longitude: "+location.getLongitude());
                        initTasks.notifyTaskDone(BannerInitTasks.Tasks.GET_LOCATION);
                    }

                }


            }){
                @Override
                protected void onPostExecute(Void aVoid) {

                    initTasks.notifyTaskDone(BannerInitTasks.Tasks.GET_LOCATION);
                }
            };

            getLocationTask.execute();
        }
        else
        {
          Banner.this.initLocationDialog();
        }
    }



    public void initLocationDialog()
    {
        ActivityCompat.requestPermissions((Activity) Banner.this.context, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 1);
    }

    */


    private void decideforRefresh(JSONObject responsejson)
    {
        try {
            final String ad_type=responsejson.getJSONObject("ad").getJSONObject("request").getString("ad_type");
            final int cur_freq=Integer.parseInt(responsejson.getJSONObject("ad").getJSONObject("request").getString("cur_freq").trim());
            final int max_freq=Integer.parseInt(responsejson.getJSONObject("ad").getJSONObject("request").getString("max_freq").trim());
            final int refreshtime=Integer.parseInt(responsejson.getJSONObject("ad").getJSONObject("request").getString("refresh").trim());

            current_frequency=cur_freq;
            maximum_frequency=max_freq;

            Log.e("COMPARISON_MADE",cur_freq+"/"+max_freq);
       if(ad_type.equals("banner")) {
           if (cur_freq <= max_freq) {
               handler.postDelayed(new Runnable() {
                   @Override
                   public void run() {

                     Banner.this.load();
                    // Toast.makeText(Banner.this.context,"Loading The banner time",Toast.LENGTH_LONG).show();
                   }
               }, (refreshtime * 1000));
           }
           else {
            //   Toast.makeText(Banner.this.context,"Greater than Max Frequency i.e."+max_freq,Toast.LENGTH_LONG).show();
           }

       }

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    public void getADV_id()
    {
           GetAdvertisingIdTask getAdvertisingIdTask=new GetAdvertisingIdTask(Banner.this.context, new GetIdTask.AdIdCallBack() {
               @Override
               public void onSuccess(String adId, boolean adDoNotTrack) {

                   if(adId!=null)
                   {
                       Banner.this.o_andadvid=adId;
                       Banner.this.adDoNotTrack=adDoNotTrack;
                       Log.d("CrackRetailBanner","andro_AdId: "+adId);
                   }
                   else
                   {
                       Log.e("CrackRetailBannerError","andro_AdId"+new Exception("Andro_Advertisement Id returned null"));
                   }

                   Banner.this.initTasks.notifyTaskDone(BannerInitTasks.Tasks.GET_ADVERTISING_ID);

               }
           });
           getAdvertisingIdTask.execute();
    }

    public void Load_WebViewUtility()
    {
        this.crackRetailWebView=new CrackRetailWebView(Banner.this.context,Banner.this.webViewListener);

     //   this.crackRetailWebView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        this.crackRetailWebView.setLayoutParams(new ViewGroup.LayoutParams(-1,-1));
       // this.crackRetailWebView.setLayoutParams(new LayoutParams((int)Utils.convertDpToPixel(Banner.this.adspace_width,context),(int)Utils.convertDpToPixel(Banner.this.adspace_height,context)));
   //   ViewGroup.LayoutParams param=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
   //    param.width=this.getWidth();
   //    param.height=this.getHeight();
   //   crackRetailWebView.setLayoutParams(param);


    }


    public JSONObject makeParams()
    {
        //wrapping all the needed params to the JSON Object & returning the JSON object
       JSONObject jsonObject=new JSONObject();
       try {
           /*   jsonObject.put("s", this.inv_hash);
           jsonObject.put("o_andadvid", this.o_andadvid);
           jsonObject.put("type", this.type);
           jsonObject.put("adFormat", this.adFormat);
           jsonObject.put("autoplay", this.autoplay);
           jsonObject.put("skip", this.skip ? "true" : "");
           jsonObject.put("debug", this.debug ? "true" : "");
           jsonObject.put("rt", this.rt);
           jsonObject.put("adspace_width", this.adspace_width);
           jsonObject.put("adspace_height", this.adspace_height);
           jsonObject.put("adspace_strict", this.adspace_strict);
           jsonObject.put("sub_bundle_id", this.sub_bundle_id);
           jsonObject.put("auto_pilot", this.auto_pilot);
           jsonObject.put("v", "Core_2.0.4");
           jsonObject.put("secure", this.secure);
           jsonObject.put("start_muted", this.start_muted);


           if (this.v_dur_min > 0) {
               jsonObject.put("v_dur_min", this.v_dur_min);
           }

           if (this.v_dur_max > 0) {
               jsonObject.put("v_dur_max", this.v_dur_max);
           }

           if (this.r_floor > 0)
           {
               jsonObject.put("r_floor",this.r_floor);
           }

           if(this.no_markup.length()>0)
           {
               jsonObject.put("no_markup",this.no_markup);
           }

           if(this.demo_gender.length()>0)
           {
               jsonObject.put("demo_gender",this.demo_gender);
           }

           if(this.demo_age > 0)
           {
               jsonObject.put("demo_age",this.demo_age);
           }

           if(this.demo_keywords.length()>0)
           {
               jsonObject.put("demo_keyword",this.demo_keywords);
           }

           if(this.location!=null)
           {
               jsonObject.put("latitude",this.location.getLatitude());
               jsonObject.put("longitude",this.location.getLongitude());
           }*/

           if(isInterstitial)
           {
               jsonObject.put("ad_type","interstitial");
           }
           else
           {
               jsonObject.put("ad_type","banner");
               int frequency=Banner.this.current_frequency;
               jsonObject.put("ad_req_freq",frequency+"");
           }

           int banner_width=Banner.this.adspace_width;
           int banner_height=Banner.this.adspace_height;

           Log.e("Banner_WIDTH", banner_width + "");
           Log.e("Banner_HEIGHT", banner_height + "");

           jsonObject.put("inv_hash",this.inv_hash);
           jsonObject.put("package_id",this.sub_bundle_id);
           jsonObject.put("ad_width",banner_width+"");
           jsonObject.put("ad_height",banner_height+"");
           jsonObject.put("screen_density",Utils.getDensity(Banner.this.context)+"");
           jsonObject.put("advert_id",this.o_andadvid);
           String sha1hashmac=Utils.SHA1((new NetworkInfo(Banner.this.context)).getWifiMAC());
           jsonObject.put("hash",sha1hashmac);


       }
       catch(Exception e)
       {
           Log.e("CrackRetailError","JSON Build Error",e);
       }


       Log.d("CrackRetailBanner","request params:"+jsonObject.toString());
        return jsonObject;

    }





     //defining basic methods for developers for setting basic vars

    public void setInventoryHash(String inv_h)
    {
        this.inv_hash=inv_h;
    }

    public void setListener(BannerListener listener){
        this.listener=listener;
    }

    public void setType(String type)
    {
        this.type=type;
    }

    public void setAdFormat(String adFormat){
        this.adFormat=adFormat;
    }

    public void setAutoPlay(boolean autoplay){
        this.autoplay=autoplay;
    }

    public void setDemo_gender(String demo_gender){
        this.demo_gender=demo_gender;
    }
    
    public void setDemo_keywords(String demo_keyword){
        this.demo_keywords=demo_keyword;
    }
   
    public void setNo_Markup(String no_markup){
        this.no_markup=no_markup;
    }

    public void setAutoPilot(boolean auto_pilot){
        this.auto_pilot=auto_pilot;
    }

    public void setRefresh(int refresh){
        this.refresh=refresh;
    }

    public void setAdspace_strict(int adspace_strict) {
        this.adspace_strict = adspace_strict;
    }

    public void setV_dur_min(int v_dur_min) {
        this.v_dur_min = v_dur_min;
    }

    public void setV_dur_max(int v_dur_max) {
        this.v_dur_max = v_dur_max;
    }

    public void setR_floor(int r_floor) {
        this.r_floor = r_floor;
    }

    public void setRt(String rt) {
        this.rt = rt;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public void setStart_muted(boolean start_muted) {
        this.start_muted = start_muted;
    }

    public void setDemo_age(int demo_age) {
        this.demo_age = demo_age;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setReadyToLoad(boolean readyToLoad) {
        this.readyToLoad = readyToLoad;
    }

    public void setMakeTasks(BannerMakeTasks makeTasks) {
        this.makeTasks = makeTasks;
    }

    public void setInitTasks(BannerInitTasks initTasks) {
        this.initTasks = initTasks;
    }

    public void setWebViewListener(CrackRetailWebView.Listener webViewListener) {
        this.webViewListener = webViewListener;
    }

    public void onPause()
    {
        Banner.this.isActivityVisible=false;
        if(crackRetailWebView!=null) {
            crackRetailWebView.onPause();
        }
    }

    public void onResume()
    {
        Banner.this.isActivityVisible=true;
        if(crackRetailWebView!=null)
        {
            crackRetailWebView.onResume();
        }
    }


}
