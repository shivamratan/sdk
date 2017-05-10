package com.crackretail.sdk.deviceinfo;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.webkit.WebSettings;

import com.crackretail.sdk.utils.checkInfoValidity;
import com.crackretail.sdk.webview.CrackRetailWebView;

import java.util.Locale;

/**
 * Created by Shivam on 17-Jul-16.
 */
public class GetIdTask
{
    Context context=null;
    public GetIdTask(Context context)
    {
        this.context=context;
    }

    //returns android ID
    public String getAndroidID()
    {
        String andro_id= Settings.Secure.getString(context.getContentResolver(),Settings.Secure.ANDROID_ID);
        return checkInfoValidity.checkValidData(andro_id);
    }

    //returns all accounts in the device
    public String[] getAccounts()
    {
        String[] account_arr;
        Account[] accounts= AccountManager.get(context).getAccountsByType("com.google");
        account_arr=new String[accounts.length];
        for(int i=0;i<accounts.length;i++) {
            account_arr[i]=accounts[i].name;
        }
        return checkInfoValidity.checkValidData(account_arr);
    }


    public String getUA(CrackRetailWebView crackRetailWebView)
    {
        final String system_ua=System.getProperty("http.agent");
        String result;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1) {
            result= WebSettings.getDefaultUserAgent(context)+"__"+system_ua;
        }
        else{
            result=crackRetailWebView.getSettings().getUserAgentString()+"__"+system_ua;
        }

        return checkInfoValidity.checkValidData(result);
    }


   /* public String getGSFID() {
        final Uri URI = Uri.parse("content://com.google.android.gsf.gservices");
        final String ID_KEY = "android_id";

        String[] params = { ID_KEY };
        Cursor c = context.getContentResolver().query(URI, null, null, params, null);

        if (c == null) {
            return "NA";
        } else if (!c.moveToFirst() || c.getColumnCount() < 2) {
            c.close();
            return "NA";
        }

        try {
            String gsfID = Long.toHexString(Long.parseLong(c.getString(1)));
            c.close();
            return gsfID;
        } catch (NumberFormatException e) {
            c.close();
            return "NA";
        }
    }
*/


    public String getFormattedTime() {

        long millis = System.currentTimeMillis();
        int sec = (int) (millis / 1000) % 60;
        int min = (int) ((millis / (1000 * 60)) % 60);
        int hr = (int) ((millis / (1000 * 60 * 60)) % 24);

        return String.format(Locale.US, "%02d:%02d:%02d", hr, min, sec);
    }


    public interface AdIdCallBack
    {
        void onSuccess(String adId,boolean adDoNotTrack);
    }

}
