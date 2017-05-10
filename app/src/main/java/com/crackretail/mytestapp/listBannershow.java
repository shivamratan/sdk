package com.crackretail.mytestapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.crackretail.sdk.Banner;

import java.util.ArrayList;
import java.util.Arrays;

public class listBannershow extends AppCompatActivity {

    private Banner banner;
    private Banner topbanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bannershow);


        ListView listView=(ListView)findViewById(R.id.listView);
        final ArrayList<String> arrayList=new ArrayList<>();
        for(int i=1;i<=50;i++)
        {
            arrayList.add("List Item "+i);
        }

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);

        topbanner=(Banner)findViewById(R.id.listtopbanner);
        topbanner.setInventoryHash("4567uhjdhffjsdkjsjfsdkfksfieu4e9934249");
        topbanner.load();


        banner=(Banner)findViewById(R.id.listbottombanner);
        banner.setInventoryHash("4567uhjdhffjsdkjsjfsdkfksfieu4e9934249");
        banner.load();


    }

    @Override
    protected void onPause() {
        super.onPause();
        banner.onPause();
        topbanner.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        banner.onResume();
        topbanner.onResume();
    }
}
