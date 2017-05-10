package com.crackretail.sdk.deviceinfo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.crackretail.sdk.utils.checkInfoValidity;

/**
 * Created by Shivam on 17-Jul-16.
 */
public class GetAppInfo
{
    Context context=null;
    public GetAppInfo(Context context){
        this.context=context;
    }

    public String getActivityName(){
        return checkInfoValidity.checkValidData(context.getClass().getSimpleName());
    }

    public String getPackageName(){
        return checkInfoValidity.checkValidData(context.getPackageName());
    }

    public String getStore(){
        String result = null;
        if (Build.VERSION.SDK_INT >= 3) {
            result = context.getPackageManager().getInstallerPackageName(context.getPackageName());
        }
        return result;
    }

    public String getAppName(){
        String result=null;
        final PackageManager packageManager=context.getPackageManager();
        ApplicationInfo applicationInfo=null;

        try {
            applicationInfo=packageManager.getApplicationInfo(context.getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        result=(applicationInfo!=null)?(String)packageManager.getApplicationLabel(applicationInfo):null;
        return checkInfoValidity.checkValidData(result);
    }

    public String getAppVersion(){
        String result = null;
        try {
            result = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return checkInfoValidity.checkValidData(result);
    }


    public String getAppVersionCode(){

        String result = null;
        try {
            result = String.valueOf(
                    context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return checkInfoValidity.checkValidData(result);
    }

}
