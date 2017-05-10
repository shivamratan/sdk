package com.crackretail.mytestapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crackretail.sdk.customevents.CustomEventNative;
import com.crackretail.sdk.nativeads.Native;
import com.crackretail.sdk.nativeads.NativeAd;
import com.crackretail.sdk.nativeads.NativeListener;

public class nativeshow extends AppCompatActivity {

    private RelativeLayout layout=null;
    private ImageView iconimage=null;
    private ImageView mainimage=null;
    private TextView tv_heading=null;
    private TextView tv_description=null;
    private RatingBar ratingBar=null;
    private Button btn_callaction=null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nativeshow);

        layout=(RelativeLayout)findViewById(R.id.myadLayout);
        iconimage=(ImageView)findViewById(R.id.icon_imageView);
        mainimage=(ImageView)findViewById(R.id.imageView3);
        tv_heading=(TextView)findViewById(R.id.textView_Heading);
        tv_description=(TextView)findViewById(R.id.textView3);
        ratingBar=(RatingBar)findViewById(R.id.ratingBar);
        btn_callaction=(Button)findViewById(R.id.button_install);



        Native mynative=new Native(this);
        mynative.setListener(new NativeListener() {
            @Override
            public void onNativeReady(Native nativevar, CustomEventNative customEventNative, final NativeAd nativeAd) {
                customEventNative.registerViewForInteraction(layout);

                nativeAd.fireTracker(nativeshow.this);

                iconimage.setImageBitmap(nativeAd.getIcon());
                mainimage.setImageBitmap(nativeAd.getMain());
                tv_heading.setText(nativeAd.getHeadline());
                tv_description.setText(nativeAd.getDescription());
                ratingBar.setRating(Float.parseFloat(nativeAd.getRating().trim()));
                btn_callaction.setText(nativeAd.getCta());
                btn_callaction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse(nativeAd.getClickUrl());
                        Intent intent=new Intent(Intent.ACTION_VIEW);
                        intent.setData(uri);
                        nativeshow.this.startActivity(intent);

                    }
                });

                layout.setVisibility(View.VISIBLE);
                Toast.makeText(nativeshow.this,"Native Ad loaded!",Toast.LENGTH_LONG).show();


            }

            @Override
            public void onNativeError(Exception var1)
            {
                Toast.makeText(nativeshow.this,"Error Loading Ad!",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNativeClick(NativeAd var1)
            {
                Toast.makeText(nativeshow.this,"Native Ad Clicked!",Toast.LENGTH_LONG).show();
            }
        });

        mynative.setInventoryHash("83c40645d785d32bb312a667d06428ab");
        mynative.load();






    }
}
