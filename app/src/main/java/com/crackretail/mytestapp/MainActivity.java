package com.crackretail.mytestapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bannershow);

        Button btn_showBanner=(Button)findViewById(R.id.button_bannerAD);
        Button btn_showNative=(Button)findViewById(R.id.button_NativeAD);
        Button btn_showInterstitial=(Button)findViewById(R.id.button_InterstitialAD);

        btn_showBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MainActivity.this,BannerTypes.class);
                startActivity(intent);

            }
        });


        btn_showNative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MainActivity.this,nativeshow.class);
                startActivity(intent);


            }
        });

        btn_showInterstitial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Interstitialshow.class);
                startActivity(intent);

            }
        });



    }


}