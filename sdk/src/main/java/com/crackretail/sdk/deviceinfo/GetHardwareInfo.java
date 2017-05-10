package com.crackretail.sdk.deviceinfo;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.crackretail.sdk.utils.checkInfoValidity;

import java.io.File;
import java.util.Locale;

/**
 * Created by Shivam on 17-Jul-16.
 */
public class GetHardwareInfo {
    public static final int DEVICE_TYPE_WATCH = 0;
    public static final int DEVICE_TYPE_PHONE = 1;
    public static final int DEVICE_TYPE_PHABLET = 2;
    public static final int DEVICE_TYPE_TABLET = 3;
    public static final int DEVICE_TYPE_TV = 4;
    public static final int PHONE_TYPE_GSM = 0;
    public static final int PHONE_TYPE_CDMA = 1;
    public static final int PHONE_TYPE_NONE = 2;
    public static final int ORIENTATION_PORTRAIT = 0;
    public static final int ORIENTATION_LANDSCAPE = 1;
    public static final int ORIENTATION_UNKNOWN = 2;
    private TelephonyManager telephonyManager=null;
    private Context context=null;

    public GetHardwareInfo(Context context) {
        this.context = context;
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public int getPhoneType() {
        switch (telephonyManager.getPhoneType()) {
            case TelephonyManager.PHONE_TYPE_NONE:
                return PHONE_TYPE_NONE;

            case TelephonyManager.PHONE_TYPE_GSM:
                return PHONE_TYPE_GSM;

            case TelephonyManager.PHONE_TYPE_CDMA:
                return PHONE_TYPE_CDMA;
            default:
                return PHONE_TYPE_NONE;
        }
    }

    public String getPhoneno(){

        String result = null;
        boolean hasReadPhoneStatePermission=context.checkCallingOrSelfPermission(android.Manifest.permission.READ_PHONE_STATE)== PackageManager.PERMISSION_GRANTED;

        if(hasReadPhoneStatePermission&&telephonyManager.getLine1Number()!=null)
            result=telephonyManager.getLine1Number();

        return checkInfoValidity.checkValidData(result);
    }


    public String getProduct(){
        return checkInfoValidity.checkValidData(Build.PRODUCT);
    }

    public String getFingerprint(){
        return checkInfoValidity.checkValidData(Build.FINGERPRINT);
    }

    public String getHardware(){
        return checkInfoValidity.checkValidData(Build.HARDWARE);
    }


    public String getRadioVer(){
        String result=null;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.ICE_CREAM_SANDWICH){
            result=Build.getRadioVersion();
        }

        return checkInfoValidity.checkValidData(result);
    }

    public String getDevice(){
        return checkInfoValidity.checkValidData(Build.DEVICE);
    }




    public int getDeviceType(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float yInches = metrics.heightPixels / metrics.ydpi;
        float xInches = metrics.widthPixels / metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);
        if (diagonalInches > 10.1) {
            return DEVICE_TYPE_TV;
        } else if (diagonalInches <= 10.1 && diagonalInches > 7) {
            return DEVICE_TYPE_TABLET;
        } else if (diagonalInches <= 7 && diagonalInches > 6.5) {
            return DEVICE_TYPE_PHABLET;
        } else if (diagonalInches <= 6.5 && diagonalInches >= 2) {
            return DEVICE_TYPE_PHONE;
        } else {
            return DEVICE_TYPE_WATCH;
        }
    }


    public String getManufacturer(){
        return checkInfoValidity.checkValidData(checkInfoValidity.handleIllegalCharacterInResult(Build.MANUFACTURER));
    }

    public String getModel(){
        return checkInfoValidity.checkValidData(Build.MODEL);
    }

    public String getBuildBrand(){
        return checkInfoValidity.checkValidData(checkInfoValidity.handleIllegalCharacterInResult(Build.BRAND));
    }

    public String getBuildHost(){
        return checkInfoValidity.checkValidData(Build.HOST);
    }

    public String getBuildTags(){
        return checkInfoValidity.checkValidData(Build.TAGS);
    }

    public long getBuildTime(){
        return Build.TIME;
    }

    public String getBuildUser(){
        return checkInfoValidity.checkValidData(Build.USER);
    }

    public String getBuildVersionRelease(){
        return checkInfoValidity.checkValidData(Build.VERSION.RELEASE);
    }

    public String getScreenDisplayId(){
        WindowManager windowManager=(WindowManager)context.getSystemService(context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        return checkInfoValidity.checkValidData(String.valueOf(display.getDisplayId()));
    }

    public String getScreenWidth()
    {
        WindowManager windowManager=(WindowManager)context.getSystemService(context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        return checkInfoValidity.checkValidData(display.getWidth()+"px");
    }

    public String getScreenHeight(){
        WindowManager windowManager=(WindowManager)context.getSystemService(context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        return checkInfoValidity.checkValidData(display.getHeight()+"px");
    }

    public String getLanguage(){
        return checkInfoValidity.checkValidData(Locale.getDefault().getLanguage());
    }

    public String getDisplayVersion(){
        return checkInfoValidity.checkValidData(Build.DISPLAY);
    }

    public String getBootLoader(){
        return checkInfoValidity.checkValidData(Build.BOOTLOADER);
    }

    public String getBoard(){
        return checkInfoValidity.checkValidData(Build.BOARD);
    }

    public String getBuildVersionCodename(){
        return checkInfoValidity.checkValidData(Build.VERSION.CODENAME);
    }

    public String getBuildVersionIncremental(){
        return checkInfoValidity.checkValidData(Build.VERSION.INCREMENTAL);
    }

    public int getBuildVersionSDK(){
        return Build.VERSION.SDK_INT;
    }

    public String getBuildID(){
        return checkInfoValidity.checkValidData(Build.ID);
    }

    public boolean isDeviceRooted() {
        String su = "su";
        String[] locations = {
                "/sbin/", "/system/bin/", "/system/xbin/", "/system/sd/xbin/", "/system/bin/failsafe/",
                "/data/local/xbin/", "/data/local/bin/", "/data/local/"
        };
        for (String location : locations) {
            if (new File(location + su).exists()) {
                return true;
            }
        }
        return false;
    }


    public String getIMEI(){
        String result = null;
        boolean hasReadPhoneStatePermission=context.checkCallingOrSelfPermission(android.Manifest.permission.READ_PHONE_STATE)== PackageManager.PERMISSION_GRANTED;

       if(hasReadPhoneStatePermission)
        result=checkInfoValidity.checkValidData(telephonyManager.getDeviceId());

      return result;
    }

    public String getSerial(){
        return checkInfoValidity.checkValidData(Build.SERIAL);
    }

    public String getOSCodename() {
        String codename;
        switch (Build.VERSION.SDK_INT) {
            case Build.VERSION_CODES.BASE:
                codename = "First Android Version. Yay !";
                break;
            case Build.VERSION_CODES.BASE_1_1:
                codename = "Base Android 1.1";
                break;
            case Build.VERSION_CODES.CUPCAKE:
                codename = "Cupcake";
                break;
            case Build.VERSION_CODES.DONUT:
                codename = "Donut";
                break;
            case Build.VERSION_CODES.ECLAIR:
            case Build.VERSION_CODES.ECLAIR_0_1:
            case Build.VERSION_CODES.ECLAIR_MR1:

                codename = "Eclair";
                break;
            case Build.VERSION_CODES.FROYO:
                codename = "Froyo";
                break;
            case Build.VERSION_CODES.GINGERBREAD:
            case Build.VERSION_CODES.GINGERBREAD_MR1:
                codename = "Gingerbread";
                break;
            case Build.VERSION_CODES.HONEYCOMB:
            case Build.VERSION_CODES.HONEYCOMB_MR1:
            case Build.VERSION_CODES.HONEYCOMB_MR2:
                codename = "Honeycomb";
                break;
            case Build.VERSION_CODES.ICE_CREAM_SANDWICH:
            case Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1:
                codename = "Ice Cream Sandwich";
                break;
            case Build.VERSION_CODES.JELLY_BEAN:
            case Build.VERSION_CODES.JELLY_BEAN_MR1:
            case Build.VERSION_CODES.JELLY_BEAN_MR2:
                codename = "Jelly Bean";
                break;
            case Build.VERSION_CODES.KITKAT:
                codename = "Kitkat";
                break;
            case Build.VERSION_CODES.KITKAT_WATCH:
                codename = "Kitkat Watch";
                break;
            case Build.VERSION_CODES.LOLLIPOP:
            case Build.VERSION_CODES.LOLLIPOP_MR1:
                codename = "Lollipop";
                break;
            case Build.VERSION_CODES.M:
                codename = "Marshmallow";
                break;
            default:
                codename = "NA";
                break;
        }
        return codename;
    }

    public String getOSVersion(){
        return checkInfoValidity.checkValidData(Build.VERSION.RELEASE);
    }

    public int getOrientation(Activity activity) {
        switch (activity.getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                return ORIENTATION_PORTRAIT;
            case Configuration.ORIENTATION_LANDSCAPE:
                return ORIENTATION_LANDSCAPE;
            default:
                return ORIENTATION_UNKNOWN;
        }
    }

}

