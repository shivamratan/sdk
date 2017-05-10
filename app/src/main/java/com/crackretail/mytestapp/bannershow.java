package com.crackretail.mytestapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.crackretail.sdk.Banner;
import com.crackretail.sdk.bannerads.BannerListener;
import com.crackretail.sdk.utils.Utils;

public class bannershow extends AppCompatActivity {

    Banner banner = null;
    TextView tv_text=null;
    Banner littile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tv_text=(TextView)findViewById(R.id.mytxt);
      banner = (Banner) findViewById(R.id.mybanner);
        banner.setListener(new BannerListener() {
            @Override
            public void onBannerError(View view, Exception exception) {

                Toast.makeText(bannershow.this, "" + exception.toString(), Toast.LENGTH_LONG).show();
                exception.toString();
            }

            @Override
            public void onBannerLoaded(View view) {

                Toast.makeText(bannershow.this, "Banner Loaded!", Toast.LENGTH_LONG).show();

                banner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onBannerClosed(View view) {
                Toast.makeText(bannershow.this, "Banner Closed!", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onBannerFinished() {
                Toast.makeText(bannershow.this, "Banner Finished", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onBannerClicked(View view) {
                Toast.makeText(bannershow.this, "Banner Clicked!", Toast.LENGTH_LONG).show();
                WebView webView = (WebView) view;

                // Log.d("Dimension of WebView","Banner_Width="+banner.getWidth()+" Banner_Height="+banner.getHeight()+"   webview_Width="+webView.getWidth()+"  Webview_Height="+webView.getHeight());
                tv_text.setText("Banner_Width=" + banner.getWidth() + "\n Banner_Height=" + banner.getHeight() + "\n   webview_Width=" + Utils.convertPixelsToDp(webView.getWidth(), bannershow.this) + "\n  Webview_Height=" + Utils.convertPixelsToDp(webView.getHeight(), bannershow.this));
            }

            @Override
            public void onNoFill(View view) {
                Toast.makeText(bannershow.this, "Banner No fill!", Toast.LENGTH_LONG).show();
            }
        });

        banner.setInventoryHash("83c40645d785d32bb312a667d06428ab");
        banner.load();


        littile=(Banner)findViewById(R.id.littlebanner);
        littile.setInventoryHash("83c40645d785d32bb312a667d06428ab");
        littile.load();

        //bannershow


    }

    @Override
    protected void onPause() {
        super.onPause();
        banner.onPause();
        littile.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        banner.onResume();
        littile.onResume();
    }
}
