package com.crackretail.sdk.nativeads;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.crackretail.sdk.customevents.CustomEventNativeListener;
import com.crackretail.sdk.networking.AsyncCallback;
import com.crackretail.sdk.networking.AsyncCallbackBitmap;
import com.crackretail.sdk.networking.CrackRetailRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Shivam on 21-Jul-16.
 */
public class NativeAd
{

    private NativeAd self = this;
    private String iconUrl;
    private int iconWidth;
    private int iconHeight;
    private Bitmap icon;
    private String mainUrl;
    private int mainWidth;
    private int mainHeight;
    private Bitmap main;
    private String headline;
    private String description;
    private String cta;
    private String rating;
    private String advertiser;
    private List<Tracker> trackerList;
    private String clickUrl;
    private int imagesLoaded = 0;
    private int imagedFailed = 0;
    private NativeAd.ImagesLoadedListener listener;

    private Context context=null;
    private CustomEventNativeListener customEventNativeListener=null;
    private NativeEvent nativeEvent=null;


    public NativeAd()
    {


    }

    private void checkImagesDownloaded()
    {
        if(this.imagesLoaded+this.imagedFailed==2)
        {
               customEventNativeListener.onNativeReady(nativeEvent,NativeAd.this);
        }
    }

    void loadImages(Context context,CustomEventNativeListener customEventNativeListener,NativeEvent nativeEvent)
    {
        this.context=context;
        this.customEventNativeListener=customEventNativeListener;
        this.nativeEvent=nativeEvent;

        CrackRetailRequest iconRequest=new CrackRetailRequest(context,this.iconUrl);
        iconRequest.getBitmap(new AsyncCallbackBitmap() {
            @Override
            public void onComplete(int code, Bitmap bitmap, Map<String, List<String>> headers) {

                NativeAd.this.icon=bitmap;
                ++NativeAd.this.imagesLoaded;
                NativeAd.this.checkImagesDownloaded();
            }

            @Override
            public void onError(Exception exception) {
                Log.e("CR_Native_err","Icon Loading Failed",exception);
                ++NativeAd.this.imagedFailed;
                NativeAd.this.checkImagesDownloaded();
            }
        });


        CrackRetailRequest mainRequest=new CrackRetailRequest(context,this.mainUrl);
        mainRequest.getBitmap(new AsyncCallbackBitmap() {
            @Override
            public void onComplete(int code, Bitmap bitmap, Map<String, List<String>> headers) {
                NativeAd.this.main=bitmap;
                ++NativeAd.this.imagesLoaded;
                NativeAd.this.checkImagesDownloaded();
            }

            @Override
            public void onError(Exception exception) {
                Log.e("CR_Native_err","Icon Loading Failed",exception);
                ++NativeAd.this.imagedFailed;
                NativeAd.this.checkImagesDownloaded();
            }
        });
    }

    private void fireTrackers(Context context,final NativeAd.FireTrackersCallback callback){
        final CountDownLatch trackersLeft=new CountDownLatch(this.trackerList.size());
        Iterator iterator=this.trackerList.iterator();

        while(iterator.hasNext())
        {
            final Tracker tracker=(Tracker)iterator.next();
            CrackRetailRequest request=new CrackRetailRequest(context,tracker.getUrl());
            request.get(new AsyncCallback() {
                @Override
                public void onComplete(int code, Object response, Map<String, List<String>> headers) {
                Log.d("CR_Native_tracker","fired Tracker "+tracker.getUrl());
                trackersLeft.countDown();
                if(callback!=null&&trackersLeft.getCount()==0L)
                {
                    callback.onComplete();
                }

                }

                @Override
                public void onError(Exception exception)
                {
                    //trying another tracker if any error occured while requesting any particular tracker
                    trackersLeft.countDown();
                    if(callback!=null&&trackersLeft.getCount()==0L)
                    {
                        callback.onComplete();
                    }
                }
            });

        }


    }


    public void fireTracker(Context context)
    {
        this.fireTrackers(context,(NativeAd.FireTrackersCallback)null);
    }




    public static NativeAd parse(JSONObject obj)
    {
        if(obj == null) {
            return null;
        } else {
            try {
                NativeAd e = new NativeAd();
                e.setIconUrl(obj.getJSONObject("imageassets").getJSONObject("icon").getString("url"));
                e.setIconWidth(obj.getJSONObject("imageassets").getJSONObject("icon").getInt("width"));
                e.setIconHeight(obj.getJSONObject("imageassets").getJSONObject("icon").getInt("height"));
                e.setMainUrl(obj.getJSONObject("imageassets").getJSONObject("main").getString("url"));
                e.setMainWidth(obj.getJSONObject("imageassets").getJSONObject("main").getInt("width"));
                e.setMainHeight(obj.getJSONObject("imageassets").getJSONObject("main").getInt("height"));
                e.setHeadline(obj.getJSONObject("textassets").getString("headline"));
                e.setDescription(obj.getJSONObject("textassets").getString("description"));
                e.setCta(obj.getJSONObject("textassets").getString("cta"));
                e.setRating(obj.getJSONObject("textassets").getString("rating"));
                e.setAdvertiser(obj.getJSONObject("textassets").getString("advertiser"));
                JSONArray ar = obj.getJSONArray("trackers");
                ArrayList trackerList = new ArrayList();

                for(int i = 0; i < ar.length(); ++i) {
                    JSONObject trackerJson = ar.getJSONObject(i);
                    Tracker tracker = new Tracker(trackerJson.getString("type"), trackerJson.getString("url"));
                    trackerList.add(tracker);
                }

                e.setTrackerList(trackerList);
                e.setClickUrl(obj.getString("click_url"));
                return e;
            }
            catch (JSONException exception)
            {
                Log.e("CR_Nativead_ERROR","JSON ERROR",exception);
                exception.printStackTrace();
                return null;
            }
        }
    }


    public String getIconUrl() {
        return this.iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getIconWidth() {
        return this.iconWidth;
    }

    public void setIconWidth(int iconWidth) {
        this.iconWidth = iconWidth;
    }

    public int getIconHeight() {
        return this.iconHeight;
    }

    public void setIconHeight(int iconHeight) {
        this.iconHeight = iconHeight;
    }

    public Bitmap getIcon() {
        return this.icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public String getMainUrl() {
        return this.mainUrl;
    }

    public void setMainUrl(String mainUrl) {
        this.mainUrl = mainUrl;
    }

    public int getMainWidth() {
        return this.mainWidth;
    }

    public void setMainWidth(int mainWidth) {
        this.mainWidth = mainWidth;
    }

    public int getMainHeight() {
        return this.mainHeight;
    }

    public void setMainHeight(int mainHeight) {
        this.mainHeight = mainHeight;
    }

    public Bitmap getMain() {
        return this.main;
    }

    public void setMain(Bitmap main) {
        this.main = main;
    }

    public String getHeadline() {
        return this.headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCta() {
        return this.cta;
    }

    public void setCta(String cta) {
        this.cta = cta;
    }

    public String getRating() {
        return this.rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getAdvertiser() {
        return this.advertiser;
    }

    public void setAdvertiser(String advertiser) {
        this.advertiser = advertiser;
    }

    public List<Tracker> getTrackerList() {
        return this.trackerList;
    }

    public void setTrackerList(List<Tracker> trackerList) {
        this.trackerList = trackerList;
    }

    public String getClickUrl() {
        return this.clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    public interface FireTrackersCallback {
        void onComplete();
    }

    public interface ImagesLoadedListener {
        void onImagesLoaded(NativeAd var1);
    }
}
