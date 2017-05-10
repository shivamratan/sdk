package com.crackretail.sdk.interstitialads;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.crackretail.sdk.R;
import com.crackretail.sdk.utils.Utils;
import com.crackretail.sdk.utils.btnback;
import com.crackretail.sdk.webview.CrackRetailWebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Shivam on 18-Jul-16.
 */
public class InterstitialActivity extends Activity
{
    CrackRetailWebView webView;
    CrackRetailWebView.Listener webviewListener;
    String adRespString;
    JSONObject adResp;
    Activity self;
    String jsonObjMainkey[]={"clickurl","htmlString"};
    String jsongarbageKey[]={"_type","scale","skippreflight","urltype","clicktype","refresh"};
    FrameLayout frameLayout=null;
    LinearLayout linearLayout=null;

    public InterstitialActivity()
    {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(1);
        this.self=this;

        frameLayout=new FrameLayout(InterstitialActivity.this);
        ViewGroup.LayoutParams framelayoutparams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        frameLayout.setLayoutParams(framelayoutparams);

        int orientation;
        try {
            orientation = getResources().getConfiguration().orientation;
            if(orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT || orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
               this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

            if(orientation == ActivityInfo.SCREEN_ORIENTATION_USER) {
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        } catch (Exception var4) {
            Log.e("CR_interstitial", "interstitial activity lock orientation exception", var4);
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }


        Intent intent = this.getIntent();
        this.adRespString = intent.getStringExtra("adResp");

        try {
            this.adResp = new JSONObject(this.adRespString);
        } catch (Exception var4) {
            Log.e("MobFoxInterstitial", "activity json exception", var4);
        }


        this.webviewListener=new CrackRetailWebView.Listener() {
            @Override
            public void onReady(CrackRetailWebView webView) {
              Log.d("CR_Interstitial","activity webview Ready");
              InterstitialActivity.this.useWebView();
            }

            @Override
            public void onError(CrackRetailWebView webView, Exception exception) {
             Log.e("CR_Interstitial","on Webview error",exception);
             InterstitialActivity.this.useWebView();

                JSONObject data=new JSONObject();
              try{
                  data.put("data",exception.toString());
              }catch(JSONException e){
                  e.printStackTrace();
              }
                InterstitialActivity.this.sendMessage("OnERROR",data.toString());
            }

            @Override
            public void onAdClick(CrackRetailWebView webView, String clickURL)
            {
                Log.d("CR_Interstitial","onAdClicked");
                try
                {
                    Uri uri = Uri.parse(clickURL);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(uri);
                    InterstitialActivity.this.startActivity(intent);
                }
                catch(Exception exception)
                {
                   this.onError(webView,exception);
                }

                JSONObject data=new JSONObject();
                try {
                    data.put("data",clickURL);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                InterstitialActivity.this.sendMessage("onAdClick",data.toString());

            }

            @Override
            public void onVideoAdFinished(CrackRetailWebView webView) {
                InterstitialActivity.this.sendMessage("onVideoAdFinished", "");
            }

            @Override
            public void onAdClosed(CrackRetailWebView webView) {
                InterstitialActivity.this.sendMessage("onAdClosed", "");
                InterstitialActivity.this.finish();
            }

            @Override
            public void onAdResponse(CrackRetailWebView webView, JSONObject jsonObj) {

            }

            @Override
            public void onAdLoaded(CrackRetailWebView webView) {

            }

            @Override
            public void onNoAd(CrackRetailWebView webView) {

            }

            @Override
            public void onAutoDirect(CrackRetailWebView webView, String str) {

                InterstitialActivity.this.sendMessage("onAutoRedirect",str);
                InterstitialActivity.this.finish();
            }
        };

        this.webView=new CrackRetailWebView(this,this.webviewListener);
        this.webView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.webView.setBackgroundColor(0xFF000000);
        this.frameLayout.addView(this.webView);
        this.addbtnToRoot();
        this.readJSON(adResp);


    }

    private void addbtnToRoot()
    {
        LinearLayout coverlinearLayout=new LinearLayout(InterstitialActivity.this);
        coverlinearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams coverlinearparams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        coverlinearLayout.setLayoutParams(coverlinearparams);

        linearLayout=new LinearLayout(InterstitialActivity.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        //linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        linearLayout.setPadding(13, 13, 13, 13);
        LinearLayout.LayoutParams linearparams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearparams.gravity=Gravity.RIGHT;
        linearLayout.setLayoutParams(linearparams);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
          Button closeButton=new Button(InterstitialActivity.this);
        LinearLayout.LayoutParams imgbtnparams=new LinearLayout.LayoutParams((int)Utils.convertDpToPixel(36, this),(int)Utils.convertDpToPixel(36, this));
            closeButton.setBackground(new btnback());
        imgbtnparams.topMargin=40;
        closeButton.setLayoutParams(imgbtnparams);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InterstitialActivity.this.webviewListener.onAdClosed(InterstitialActivity.this.webView);
            }
        });
          linearLayout.addView(closeButton);
        }

        coverlinearLayout.addView(linearLayout);
        frameLayout.addView(coverlinearLayout);
    }

    protected void sendMessage(String message){
        this.sendMessage(message, (String) null);
    }

    protected void sendMessage(String message,String data)
    {
        Log.d("CR_Interstitial", "inter-Activity>>> Broadcasting Message: " + message);
        Intent interstitialEventIntent=new Intent("interstitialEvent");
        interstitialEventIntent.putExtra("message", message);
        if(data!=null&&data.length()>0){
            interstitialEventIntent.putExtra("data",data);
        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(interstitialEventIntent);
    }

    protected void useWebView() {
        this.webView.renderAd(this.adResp);
        this.setContentView(this.frameLayout);
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
                    this.webView.loadDataWithBaseURL("",clickURL,"text/html","UTF-8","");
                    this.webviewListener.onReady(this.webView);
                    //    Log.e("clickURL",clickURL);
                }

              //  stringBuilder.append(jsonObjMainkey[i]+" "+clickURL+"\n");
            }

        /*    for(int j=0;j<jsongarbageKey.length;j++)
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


    protected void onPause()
    {
        super.onPause();
        this.sendMessage("onAdClosed", "");
    }

    public void finish()
    {
        super.finish();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

}
