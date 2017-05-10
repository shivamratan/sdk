package com.crackretail.mytestapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BannerTypes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_types);


        Button button13=(Button)findViewById(R.id.buttononethree);
        Button button54=(Button)findViewById(R.id.buttonfivefour);
        Button button2=(Button)findViewById(R.id.buttontwo);

        button13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(BannerTypes.this,bannershow.class);
                startActivity(intent);

            }
        });

        button54.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(BannerTypes.this,listBannershow.class);
                startActivity(intent);

            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(BannerTypes.this,bannertype3.class);
                startActivity(intent);

            }
        });
    }
}
