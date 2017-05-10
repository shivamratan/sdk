package com.crackretail.sdk.deviceinfo;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.crackretail.sdk.utils.checkInfoValidity;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * Created by Shivam on 17-Jul-16.
 */
public class NetworkInfo
{
    private Context context;
    public static final int UNKNOWN = 0;
    public static final int WIFI_WIFIMAX = 1;
    public static final int CELLULAR_UNKNOWN = 2;
    public static final int CELLULAR_2G = 3;
    public static final int CELLULAR_3G = 4;
    public static final int CELLULAR_4G = 5;
    public static final int CELLULAR_UNIDENTIFIED_GEN = 6;

    public NetworkInfo(Context context)
    {
        this.context=context;
    }


    public boolean isWifiEnabled() {
        boolean wifiState = false;

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            wifiState = wifiManager.isWifiEnabled();
        }
        return wifiState;
    }

    public boolean isNetworkAvailable(){
        ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }


    public String getIPv4Address(){
        String result=null;

        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for(NetworkInterface networkInterface:interfaces){
                List<InetAddress> address=Collections.list(networkInterface.getInetAddresses());
                for(InetAddress addr: address){
                    if(!addr.isLoopbackAddress()){
                        String sAddr=addr.getHostAddress().toUpperCase();
                        boolean isIPv4=addr instanceof Inet4Address;

                        if(isIPv4)
                            result=sAddr;
                    }
                }

            }

        }catch (Exception exception){
            exception.printStackTrace();
        }

        return checkInfoValidity.checkValidData(result);
    }



    public String getIPv6Address(){
        String result=null;

        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for(NetworkInterface networkInterface:interfaces){
                List<InetAddress> address=Collections.list(networkInterface.getInetAddresses());
                for(InetAddress addr: address){
                    if(!addr.isLoopbackAddress()){
                        String sAddr=addr.getHostAddress().toUpperCase();
                        boolean isIPv4=addr instanceof Inet4Address;

                        if (!isIPv4) {
                            int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                            result = delim < 0 ? sAddr : sAddr.substring(0, delim);
                        }
                    }
                }

            }

        }catch (Exception exception){
            exception.printStackTrace();
        }

        return checkInfoValidity.checkValidData(result);

    }

    public int getNetworkType() {
        int networkStatePermission =
                context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE);

        int result = UNKNOWN;

        if (networkStatePermission == PackageManager.PERMISSION_GRANTED) {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            android.net.NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork == null) {
                result = UNKNOWN;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI
                    || activeNetwork.getType() == ConnectivityManager.TYPE_WIMAX) {
                result = WIFI_WIFIMAX;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                TelephonyManager manager =
                        (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                if (manager.getSimState() == TelephonyManager.SIM_STATE_READY) {
                    switch (manager.getNetworkType()) {

                        // Unknown
                        case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                            result = CELLULAR_UNKNOWN;
                            break;
                        // Cellular Data–2G
                        case TelephonyManager.NETWORK_TYPE_EDGE:
                        case TelephonyManager.NETWORK_TYPE_GPRS:
                        case TelephonyManager.NETWORK_TYPE_CDMA:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                            result = CELLULAR_2G;
                            break;
                        // Cellular Data–3G
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                            result = CELLULAR_3G;
                            break;
                        // Cellular Data–4G
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            result = CELLULAR_4G;
                            break;
                        // Cellular Data–Unknown Generation
                        default:
                            result = CELLULAR_UNIDENTIFIED_GEN;
                            break;
                    }
                }
            }
        }
        return result;
    }


    public String getWifiMAC() {
        String result = null;
        if (context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_WIFI_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            result = wm.getConnectionInfo().getMacAddress();
        }
        return checkInfoValidity.checkValidData(result);
    }

    /*
    public String getBluetoothMAC() {
        String result = null;
        if (context.checkCallingOrSelfPermission(Manifest.permission.BLUETOOTH)
                == PackageManager.PERMISSION_GRANTED) {
            BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();
            result = bta.getAddress();
        }
        return checkInfoValidity.checkValidData(result);
    }
*/

}
