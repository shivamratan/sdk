package com.crackretail.mytestapp;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.crackretail.sdk.Interstitial;
import com.crackretail.sdk.interstitialads.InterstitialAdListener;

public class Interstitialshow extends AppCompatActivity {

    Interstitial interstitialad=null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitialshow);


        interstitialad = new Interstitial(this);

        final Activity self = this;
        InterstitialAdListener listener = new InterstitialAdListener() {
            @Override
            public void onInterstitialLoaded(Interstitial interstitial) {
              //  Toast.makeText(self, "loaded", Toast.LENGTH_SHORT).show();
                //call show() to display the interstitial when its finished loading
                interstitial.show();
            }
            @Override
            public void onInterstitialFailed(Interstitial interstitial, Exception e) {
                //Toast.makeText(self, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onInterstitialClosed(Interstitial interstitial) {
                //Toast.makeText(self, "closed", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onInterstitialFinished() {
               // Toast.makeText(self, "finished", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onInterstitialClicked(Interstitial interstitial) {
                Toast.makeText(self, "clicked", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onInterstitialShown(Interstitial interstitial) {
                //Toast.makeText(self, "shown", Toast.LENGTH_SHORT).show();
            }
        };
        interstitialad.setListener(listener);
        interstitialad.setInventoryHash("83c40645d785d32bb312a667d06428ab");
        interstitialad.load();


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onPause() {
        super.onPause();
        interstitialad.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
        interstitialad.onResume();
    }



}
