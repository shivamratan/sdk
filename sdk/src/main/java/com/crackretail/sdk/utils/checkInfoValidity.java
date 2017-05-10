package com.crackretail.sdk.utils;

/**
 * Created by Shivam on 17-Jul-16.
 */
public class checkInfoValidity
{
    public static String handleIllegalCharacterInResult(String result) {
        String tempResult = result;
        if (tempResult!=null && tempResult.indexOf(" ") > 0) {
            tempResult = tempResult.replaceAll(" ", "_");
        }
        return tempResult;
    }


    public static String checkValidData(String data) {
        String tempData = data;
        if (tempData == null || tempData.length() == 0) {
            tempData = "NA";
        }
        return tempData;
    }


    public static String[] checkValidData(String[] data) {
        String[] tempData = data;
        if (tempData == null || tempData.length == 0) {
            tempData = new String[] { "-" };
        }
        return tempData;
    }
}
