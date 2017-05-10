package com.crackretail.sdk.deviceinfo;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.crackretail.sdk.utils.checkInfoValidity;

import java.util.Locale;

/**
 * Created by Shivam on 17-Jul-16.
 */
public class GetSimInfo
{
    private TelephonyManager telephonyManager=null;
    Context context;
    public GetSimInfo(Context context) {
        this.context=context;
        telephonyManager=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public String getCountry(){
        String result;
        if(telephonyManager.getSimState()==TelephonyManager.SIM_STATE_READY)
        {
            result=telephonyManager.getSimCountryIso().toLowerCase(Locale.getDefault());
        }
        else{
            Locale locale=Locale.getDefault();
            result=locale.getCountry().toLowerCase(locale);
        }

        return checkInfoValidity.checkValidData(checkInfoValidity.handleIllegalCharacterInResult(result));
    }


    public String getCarrier()
    {
        String result=null;
        if(telephonyManager!=null&&telephonyManager.getPhoneType()!=TelephonyManager.PHONE_TYPE_CDMA)
        {
            result=telephonyManager.getNetworkOperatorName().toLowerCase(Locale.getDefault());
        }

        return checkInfoValidity.checkValidData(checkInfoValidity.handleIllegalCharacterInResult(result));
    }


  /*  public String getIMSI() {
        return checkInfoValidity.checkValidData(telephonyManager.getSubscriberId());

    }*/

    public String getSimSerial() {
        return checkInfoValidity.checkValidData(telephonyManager.getSimSerialNumber());
    }


}
