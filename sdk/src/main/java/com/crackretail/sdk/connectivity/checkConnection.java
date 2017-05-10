package com.crackretail.sdk.connectivity;

import android.content.Context;
import android.net.ConnectivityManager;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Shivam on 15-Jul-16.
 */
public class checkConnection {



   public static boolean  step2hotspotcheck(Context context)
    {

        if (isConnected(context))
        {
            try
            {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(28000); //choose your own timeframe
                urlc.setReadTimeout(30000); //choose your own timeframe
                urlc.connect();
                //networkcode2 = urlc.getResponseCode();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e)
            {
                return (false);  //connectivity exists, but no internet.
            }
        } else
        {
            return false;  //no connectivity
        }
    }

    private static boolean isConnected(Context context)
    {
        ConnectivityManager connec=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //NetworkInfo activeNetworkinfo=connectivitymanager.getActiveNetworkInfo();
        //networkinfo=activeNetworkinfo.toString();

        if(connec.getNetworkInfo(0).getState()==android.net.NetworkInfo.State.CONNECTED||
                connec.getNetworkInfo(0).getState()==android.net.NetworkInfo.State.CONNECTING||
                connec.getNetworkInfo(1).getState()==android.net.NetworkInfo.State.CONNECTED||
                connec.getNetworkInfo(1).getState()==android.net.NetworkInfo.State.CONNECTING)
        {
            return true;
        }

        else if(connec.getNetworkInfo(0).getState()==android.net.NetworkInfo.State.DISCONNECTED||
                connec.getNetworkInfo(0).getState()==android.net.NetworkInfo.State.DISCONNECTING||
                connec.getNetworkInfo(1).getState()==android.net.NetworkInfo.State.DISCONNECTED||
                connec.getNetworkInfo(1).getState()==android.net.NetworkInfo.State.DISCONNECTING)
        {
            return false;
        }
        else
        {
            return false;
        }

    }
}
