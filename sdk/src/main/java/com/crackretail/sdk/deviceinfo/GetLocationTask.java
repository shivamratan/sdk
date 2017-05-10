package com.crackretail.sdk.deviceinfo;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.crackretail.sdk.utils.Utils;

/**
 * Created by Shivam on 30-Jun-16.
 */

public class GetLocationTask extends AsyncTask<Void,Void,Void>
{
  /*  Context context;
    GetLocationTask.LocationTaskListener locationTaskListener;

    public GetLocationTask(Context context,GetLocationTask.LocationTaskListener locationTaskListener)
    {
        this.context=context;
        this.locationTaskListener=locationTaskListener;
    }*/

    @Override
    protected Void doInBackground(Void... params)
    {
/*
        final LocationManager locationManager=(LocationManager)this.context.getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled=locationManager.isProviderEnabled("gps");
        //Accessing location
        if(isGPSEnabled)
        {
            Criteria criteria=new Criteria();
            final String bestProvider=String.valueOf(locationManager.getBestProvider(criteria,true));
           Location location = null;

            try {
              if(Utils.checkPermission(context,"ACCESS_FINE_LOCATION")) {
                  location= locationManager.getLastKnownLocation(bestProvider);
              }
            }
            catch (Exception exception) {
                Log.e("CrackRetailBanner","SecurityException ",exception);
            }

            if(location!=null)
            {
                this.locationTaskListener.onLocationReady(location);
                return null;
            }

            //location listener
        final LocationListener locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                try{
                    if(Utils.checkPermission(context,"ACCESS_FINE_LOCATION")) {
                        locationManager.removeUpdates(this);
                    }
                }
                catch (Exception exception) {
                Log.e("CrackRetailBanner","SecurityException",exception);
                }

                if(location!=null)
                {
                    GetLocationTask.this.locationTaskListener.onLocationReady(location);

                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras)
            {
                Log.d("CrackRetailBanner","on Status Changed");
            }

            @Override
            public void onProviderEnabled(String provider)
            {
                Log.d("CrackRetailBanner","on Provider enabled");
            }

            @Override
            public void onProviderDisabled(String provider)
            {
                Log.d("CrackRetailBanner","on Provider disabled");
            }
        };

            //register for location update
            (new Handler(this.context.getMainLooper())).post(new Runnable() {
                @Override
                public void run() {

                  try {
                      if(Utils.checkPermission(context,"ACCESS_FINE_LOCATION")) {
                          locationManager.requestLocationUpdates(bestProvider, 1000L, 0.0F, locationListener);
                      }
                  }
                  catch(Exception exception)
                  {
                    Log.e("CrackRetail","Security Exception",exception);
                  }

                }
            });

        }
        else
        {
            this.locationTaskListener.onLocationReady((Location)null);
        }
*/
        return null;
    }

    public interface LocationTaskListener
    {
        void onLocationReady(Location location);
    }
}
