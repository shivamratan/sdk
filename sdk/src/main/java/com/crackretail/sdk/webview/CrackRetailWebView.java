package com.crackretail.sdk.webview;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.crackretail.sdk.constants.Constants;
import com.crackretail.sdk.networking.AsyncCallbackJSON;
import com.crackretail.sdk.networking.CrackRetailRequest;
import com.crackretail.sdk.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by Shivam on 29-Jun-16.
 */
public class CrackRetailWebView extends  WebView{
    Context context;
    Listener listener;
    String cached = "";
    Handler handler;
    boolean ready = false;
    boolean userInteraction = false;
    String inventory_hash;
    String jsonObjMainkey[]={"clickurl","htmlString"};
    //String jsongarbageKey[]={"_type","scale","skippreflight","urltype","clicktype","refresh"};

    public CrackRetailWebView(Context context, CrackRetailWebView.Listener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
        init();
    }

    private void init() {
        Log.d("CrackRetailWebView", "CrackRetail webview init");
        //Adding basic functionality to the webview of the CrackRetailWebview
        this.setVerticalScrollBarEnabled(false);
        this.setHorizontalScrollBarEnabled(false);
        WebSettings settings = this.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= 16) {
            settings.setAllowFileAccessFromFileURLs(true);
            settings.setAllowUniversalAccessFromFileURLs(true);
        }

        settings.setCacheMode(2);
        this.setHapticFeedbackEnabled(false);
        this.setSoundEffectsEnabled(false);


        //finding the setMediaPlaybackRequiresUserGesture Method whose has one parameter of boolean type & then setting or invoking its value to false
        if (Build.VERSION.SDK_INT >= 17) {
            try {
                Method e = WebSettings.class.getMethod("setMediaPlaybackRequiresUserGesture", new Class[]{Boolean.TYPE});
                e.invoke(settings, new Object[]{Boolean.valueOf(false)});
            } catch (Exception e) {
                this.listener.onError(this, e);
            }

        }

      /*
      * These are the handler which are integrated with the webview
      * which can be called from js files with the help of
      * default handler in specific
      * particular events on webview
      * */


        //The alternative

   /*     this.registerHandler("click", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                if (CrackRetailWebView.this.listener != null) {
                    CrackRetailWebView.this.listener.onAdClick(CrackRetailWebView.this, data);
                }
            }
        });


        this.registerHandler("close", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                CrackRetailWebView.this.removeAllViews();
                if (CrackRetailWebView.this.listener != null) {
                    CrackRetailWebView.this.listener.onAdClosed(CrackRetailWebView.this);
                }
            }
        });

        this.registerHandler("finished", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {

                if (CrackRetailWebView.this.listener != null) {
                    CrackRetailWebView.this.listener.onVideoAdFinished(CrackRetailWebView.this);
                }
            }
        });

        this.registerHandler("error", new BridgeHandler() {
            @Override
            public void handler(String error, CallBackFunction function) {

                if (CrackRetailWebView.this.listener != null) {
                    CrackRetailWebView.this.listener.onError(CrackRetailWebView.this, new Exception(error));
                }
            }
        });
*/

        this.setWebViewClient(new CrackRetailWebViewClient(this, new CrackRetailWebViewClient.Listener() {
            @Override
            public void onReady() {

                if (!CrackRetailWebView.this.ready) {
                    CrackRetailWebView.this.ready = true;
                    if (CrackRetailWebView.this.listener != null) {
                        CrackRetailWebView.this.listener.onReady(CrackRetailWebView.this);
                    }
                }
            }

            @Override
            public void onClick(final String str) {
                CrackRetailWebView.this.post(new Runnable() {
                    @Override
                    public void run() {

                        if (CrackRetailWebView.this.listener != null) {
                            CrackRetailWebView.this.listener.onAdClick(CrackRetailWebView.this, str);
                        }
                    }
                });
            }

            @Override
            public void onError(final Exception exception) {
                CrackRetailWebView.this.post(new Runnable() {
                    @Override
                    public void run() {
                        if (CrackRetailWebView.this.listener != null) {
                            CrackRetailWebView.this.listener.onError(CrackRetailWebView.this, exception);
                        }
                    }
                });
            }

            @Override
            public void onAutoRedirect(WebView webView, final String str) {
                CrackRetailWebView.this.post(new Runnable() {
                    @Override
                    public void run() {

                        if (CrackRetailWebView.this.listener != null) {
                            CrackRetailWebView.this.listener.onAutoDirect(CrackRetailWebView.this, str);
                        }
                    }
                });
            }

        }));


        this.setWebChromeClient(new CrackRetailWebChromeClient(new CrackRetailWebChromeClient.ReadyCallback() {
            @Override
            public void onReady() {

                if(!CrackRetailWebView.this.ready)
                {
                    CrackRetailWebView.this.ready=true;
                    if(CrackRetailWebView.this.listener!=null)
                    {
                        CrackRetailWebView.this.listener.onReady(CrackRetailWebView.this);
                    }
                }
            }
        }));


        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                CrackRetailWebView.this.userInteraction = true;
                return false;
            }
        });


    /*        try {
            CrackRetailWebView.this.loadSDK();
        } catch (Exception e) {
            Log.e("CrackRetailWebView", "Load sdk Error", e);
            if (this.listener == null) {
                return;
            }
            this.listener.onError(this, e);
        }
      */
    }




    public void loadAd(final String jsonstr) {
        this.userInteraction = false;
        this.removeAllViews();

        //getting the json from server
        CrackRetailRequest request=new CrackRetailRequest(this.context, Constants.CRACKRETAIL_AD_JSON_URL);
        try {
            request.setData(new JSONObject(jsonstr));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.getJSON(new AsyncCallbackJSON() {
            @Override
            public void onComplete(int code, JSONObject jsonObject, Map<String, List<String>> headers)
            {
                Log.e("reponseJSON",jsonObject.toString());
                JSONObject complete_json=generateJSON(jsonObject,jsonstr);
               // Log.d("Crack_RetailJSON", complete_json.toString());
               readJSON(complete_json);
              // listener.onAdLoaded(CrackRetailWebView.this);
               listener.onAdResponse(CrackRetailWebView.this,complete_json);
            }

            @Override
            public void onError(Exception exception)
            {
                    listener.onError(CrackRetailWebView.this,exception);
                    Log.e("CrackRetail_Error",exception.toString());
            }
        });

        //Adding loadAdResponse bridgehandler which would be called from the js script of the webview
        //when ad will loaded
      /*  this.registerHandler("loadAdResponse", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {

                Log.d("CrackRetailWebView", "DATA:>>>>>>>> " + data);

                try {
                    JSONObject jsonobj = new JSONObject(data);
                    if (jsonobj.has("adLoaded")) {
                        if (CrackRetailWebView.this.listener != null) {
                            CrackRetailWebView.this.listener.onAdLoaded(CrackRetailWebView.this);
                        }

                        return;
                    }

                    if (jsonobj.has("vast") || jsonobj.has("customEvents")) {

                        if (CrackRetailWebView.this.listener != null)
                        {
                           // Log.d("Checking the","reaching to the destination bro!!!");
                            CrackRetailWebView.this.listener.onAdResponse(CrackRetailWebView.this, jsonobj);
                        }

                        return;
                    }


                    Object except_ion = jsonobj.get("error");
                    if (except_ion != null && except_ion instanceof JSONObject) {
                        JSONObject errorObj = (JSONObject) except_ion;
                        if (errorObj.has("noAd")) {
                            if (CrackRetailWebView.this.listener != null) {
                                CrackRetailWebView.this.listener.onNoAd(CrackRetailWebView.this);
                            }
                        }
                    }

                    if (CrackRetailWebView.this.listener != null) {
                        CrackRetailWebView.this.listener.onError(CrackRetailWebView.this, new Exception(except_ion.toString()));
                    }


                } catch (Exception e) {
                    if (CrackRetailWebView.this.listener != null) {
                        CrackRetailWebView.this.listener.onError(CrackRetailWebView.this, e);
                    }
                }


            }
        });*/

        //Calling to load the final JSON from the server to load the ad
        //this.callHandler("loadAd", jsonstr, (CallBackFunction) null);
    }

    private JSONObject generateJSON(JSONObject respJson,String paramstr)
    {
        JSONObject jsonObject=new JSONObject();

        try {



            JSONObject adJSON=new JSONObject();
            adJSON.put("encoded","true");

            JSONObject paramJSON=new JSONObject(paramstr);
            adJSON.put("params",paramJSON);

            JSONObject jsoncustobj=respJson.getJSONObject("request");
            adJSON.put("request",jsoncustobj);
            jsonObject.put("ad",adJSON);
            JSONArray respcustarr=respJson.getJSONArray("customEvents");

            jsonObject.put("customEvents",respcustarr);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    void readJSON(JSONObject json)
    {
       // StringBuilder stringBuilder=new StringBuilder();
        //Log.d("json-info", jsonstr);
        try {
         //   JSONObject json=new JSONObject(jsonstr);

            JSONObject jsonObject1=json.getJSONObject("ad").getJSONObject("request");


            for(int i=0;i<jsonObjMainkey.length;i++) {
                JSONObject jsonObject2=jsonObject1.getJSONObject(jsonObjMainkey[i]);
                String clickURL = jsonObject2.getString("__cdata");
                if(i==1)
                {
                    this.loadDataWithBaseURL("",clickURL,"text/html","UTF-8","");
                //    Log.e("clickURL",clickURL);
                }

              //  stringBuilder.append(jsonObjMainkey[i]+" "+clickURL+"\n");
            }

          /*  for(int j=0;j<jsongarbageKey.length;j++)
            {
                String value=jsonObject1.getString(jsongarbageKey[j]);
                stringBuilder.append(jsongarbageKey[j]+" "+value+"\n");
            }*/
           // return stringBuilder.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
       // return "";
    }




    @Override
    public void onPause() {
        super.onPause();
        //Video view must be paused
    }


    @Override
    public void onResume() {
        super.onResume();

        //Video view must be resumed
    }

    //Basic methods which is used externally to change the local Variables
    public void setCached(String cached) {
        this.cached = cached;
    }

    public CrackRetailWebView.Listener getListener() {
        return this.listener;
    }

    public void setUserInteraction(boolean interaction) {
        this.userInteraction = interaction;
    }

    public void setRefresh(int refresh) {
     //   this.callHandler("refresh", String.valueOf(refresh), (CallBackFunction) null);
    }

    public void setWaterfalls(String waterfalls)
    {
       // this.callHandler("setWaterfallJson",waterfalls,(CallBackFunction)null);
    }

    @Override
    public void destroy() {
        if (this.listener != null) {
            this.listener = null;
        }

   /*     this.registerHandler("click", (BridgeHandler) null);
        this.registerHandler("close", (BridgeHandler) null);
        this.registerHandler("finished", (BridgeHandler) null);
        this.registerHandler("error", (BridgeHandler) null);
        this.registerHandler("loadAdResponse", (BridgeHandler) null);*/
        super.destroy();
    }


    public void setListener(CrackRetailWebView.Listener listener)
    {
        this.listener=listener;
    }
    public interface Listener
    {
        void onReady(CrackRetailWebView webView);
        void onError(CrackRetailWebView webView, Exception exception);
        void onAdClick(CrackRetailWebView webView, String str);
        void onVideoAdFinished(CrackRetailWebView webView);
        void onAdClosed(CrackRetailWebView webView);
        void onAdResponse(CrackRetailWebView webView, JSONObject jsonObj);
        void onAdLoaded(CrackRetailWebView webView);
        void onNoAd(CrackRetailWebView webView);
        void onAutoDirect(CrackRetailWebView webView, String str);
    }


  public void renderAd(JSONObject adResp)
    {
        this.userInteraction=false;

        /*
        * Try Catch statement missing for the video ads
        * */
     /* this.callHandler("renderAd", adResp.toString(), new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                if(data==null)
                {
                    data="";
                }

                Log.d("CrackRetailBanner","render response: "+data);
            }
        });*/

    }





}
