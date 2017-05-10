package com.crackretail.sdk.dmp;

import android.app.Activity;
import android.content.Context;

import com.crackretail.sdk.deviceinfo.GetAppInfo;
import com.crackretail.sdk.deviceinfo.GetHardwareInfo;
import com.crackretail.sdk.deviceinfo.GetIdTask;
import com.crackretail.sdk.deviceinfo.GetSimInfo;
import com.crackretail.sdk.deviceinfo.NetworkInfo;
import com.crackretail.sdk.webview.CrackRetailWebView;

import org.json.JSONObject;

/**
 * Created by Shivam on 16-Jul-16.
 */
public class dump
{
   private JSONObject jsonObject=null;
   private Context context;
   private CrackRetailWebView crackRetailWebView=null;
   private String AdId;
   private boolean adDoNotTrack;
   private Activity activity;
    static dump mydump=null;

   protected dump(Activity activity,Context context,CrackRetailWebView crackRetailWebView,String AdId,boolean adDoNotTrack)
   {
       jsonObject=new JSONObject();
       this.context=context;
       this.crackRetailWebView=crackRetailWebView;
       this.activity=activity;
   }

   //Making dump a singleton class
   public static dump getInstance(Activity activity,Context context,CrackRetailWebView crackRetailWebView,String AdId,boolean adDoNotTrack)
   {
       if(mydump==null)
           mydump=new dump(activity,context,crackRetailWebView,AdId,adDoNotTrack);

       return mydump;
   }

   JSONObject prepareDump()
   {
    try {
        GetIdTask getIdTask = new GetIdTask(context);

        //emailid Dump
        String[] emailIds = getIdTask.getAccounts();
        StringBuilder emailString = new StringBuilder();
        if (emailIds != null && emailIds.length > 0) {
            for (int i=0;i<emailIds.length;i++)
            {
                if(i==emailIds.length-1)
                    emailString.append(emailIds[i]);
                else
                    emailString.append(emailIds[i]+",");
            }
        } else {
            emailString.append("-");
        }

        jsonObject.put("email_id", emailString);
        jsonObject.put("android_ad_id",AdId);
        jsonObject.put("addonottrack",adDoNotTrack);
        jsonObject.put("formatted_time",getIdTask.getFormattedTime());




        //HardwareInfo Dump
        GetHardwareInfo getHardwareInfo=new GetHardwareInfo(context);
        jsonObject.put("language",getHardwareInfo.getLanguage());
        jsonObject.put("android_id",getIdTask.getAndroidID());
       // jsonObject.put("imei",getHardwareInfo.getIMEI());
        jsonObject.put("user_agent",getIdTask.getUA(crackRetailWebView));
       // jsonObject.put("GSF ID",getIdTask.getGSFID());
        jsonObject.put("device_serial",getHardwareInfo.getSerial());
        jsonObject.put("manufacturer",getHardwareInfo.getManufacturer());
        jsonObject.put("model",getHardwareInfo.getModel());
        jsonObject.put("os_codename",getHardwareInfo.getOSCodename());
        jsonObject.put("os_version",getHardwareInfo.getOSVersion());
        jsonObject.put("display_version",getHardwareInfo.getDisplayVersion());
        jsonObject.put("phone_number",getHardwareInfo.getPhoneno());
        jsonObject.put("radio_version",getHardwareInfo.getRadioVer());
        jsonObject.put("product",getHardwareInfo.getProduct());
        jsonObject.put("device",getHardwareInfo.getDevice());
        jsonObject.put("board",getHardwareInfo.getBoard());
        jsonObject.put("hardware",getHardwareInfo.getHardware());
        jsonObject.put("bootloader",getHardwareInfo.getBootLoader());
        jsonObject.put("device_rooted",String.valueOf(getHardwareInfo.isDeviceRooted()));
        jsonObject.put("finger_print",getHardwareInfo.getFingerprint());
        jsonObject.put("build_brand",getHardwareInfo.getBuildBrand());
        jsonObject.put("build_host",getHardwareInfo.getBuildHost());
        jsonObject.put("build_tag",getHardwareInfo.getBuildTags());
        jsonObject.put("build_time",(getHardwareInfo.getBuildTime())+"");
        jsonObject.put("build_user",getHardwareInfo.getBuildUser());
        jsonObject.put("build_version_release",getHardwareInfo.getBuildVersionRelease());
        jsonObject.put("screen_display_id",getHardwareInfo.getScreenDisplayId());
        jsonObject.put("build_version_codename",getHardwareInfo.getBuildVersionCodename());
        jsonObject.put("build_version_increment",getHardwareInfo.getBuildVersionIncremental());
        jsonObject.put("build_version_sdk",String.valueOf(getHardwareInfo.getBuildVersionSDK()));
        jsonObject.put("build_id",getHardwareInfo.getBuildID());

        switch (getHardwareInfo.getDeviceType(activity)){

            case GetHardwareInfo.DEVICE_TYPE_WATCH:
                jsonObject.put("device_type", "watch");
                break;
            case GetHardwareInfo.DEVICE_TYPE_PHONE:
                jsonObject.put("device_type", "phone");
                break;
            case GetHardwareInfo.DEVICE_TYPE_PHABLET:
                jsonObject.put("device_type", "phablet");
                break;
            case GetHardwareInfo.DEVICE_TYPE_TABLET:
                jsonObject.put("device_type", "tablet");
                break;
            case GetHardwareInfo.DEVICE_TYPE_TV:
                jsonObject.put("device_type", "tv");
                break;
            default:
                //do nothing
                break;
        }


        switch (getHardwareInfo.getPhoneType()) {
            case GetHardwareInfo.PHONE_TYPE_CDMA:
                jsonObject.put("phone_type", "CDMA");
                break;
            case GetHardwareInfo.PHONE_TYPE_GSM:
                jsonObject.put("phone_type", "GSM");
                break;
            case GetHardwareInfo.PHONE_TYPE_NONE:
                jsonObject.put("phone_type", "None");
                break;
            default:
                jsonObject.put("phone_type", "Unknown");
                break;
        }

        switch (getHardwareInfo.getOrientation(activity)) {
            case GetHardwareInfo.ORIENTATION_LANDSCAPE:
                jsonObject.put("orientation", "Landscape");
                break;
            case GetHardwareInfo.ORIENTATION_PORTRAIT:
                jsonObject.put("orientation", "Portrait");
                break;
            case GetHardwareInfo.ORIENTATION_UNKNOWN:
                jsonObject.put("orientation", "Unknown");
                break;
            default:
                jsonObject.put("orientation", "Unknown");
                break;
        }


     //App Info
        GetAppInfo getAppInfo=new GetAppInfo(context);
        jsonObject.put("installer_store",getAppInfo.getStore());
        jsonObject.put("app_name",getAppInfo.getAppName());
        jsonObject.put("package_name",getAppInfo.getPackageName());
        jsonObject.put("activity_name",getAppInfo.getActivityName());
        jsonObject.put("app_version",getAppInfo.getAppVersion());
        jsonObject.put("app_version_code",getAppInfo.getAppVersionCode());


        //Network Info
        NetworkInfo getNetworkInfo=new NetworkInfo(context);
        jsonObject.put("wifi_mac_address",getNetworkInfo.getWifiMAC());
        jsonObject.put("ipv4_address",getNetworkInfo.getIPv4Address());
        jsonObject.put("ipv6_address",getNetworkInfo.getIPv6Address());
        jsonObject.put("network_available",String.valueOf(getNetworkInfo.isNetworkAvailable()));
        jsonObject.put("wifi_enabled",String.valueOf(getNetworkInfo.isWifiEnabled()));
   //     jsonObject.put("BlueTooth MAC Address",getNetworkInfo.getBluetoothMAC());
        switch (getNetworkInfo.getNetworkType()) {
            case NetworkInfo.CELLULAR_UNKNOWN:
                jsonObject.put("network_type", "Unknown");
                break;
            case NetworkInfo.CELLULAR_UNIDENTIFIED_GEN:
                jsonObject.put("network_type", "Cellular Unidentified Generation");
                break;
            case NetworkInfo.CELLULAR_2G:
                jsonObject.put("network_type", "Cellular 2G");
                break;
            case NetworkInfo.CELLULAR_3G:
                jsonObject.put("network_type", "Cellular 3G");
                break;
            case NetworkInfo.CELLULAR_4G:
                jsonObject.put("network_type", "Cellular 4G");
                break;
            default:
                // Do nothing
                break;
        }





    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }

       return jsonObject;
   }

}
