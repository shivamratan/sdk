package com.crackretail.sdk.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by Shivam on 29-Jun-16.
 */
public class Utils
{
    public Utils()
    {

    }

    private static String convertToHex(byte[] data)
    {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

    public static int getDensity(Context context)
    {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenDensity = metrics.densityDpi;
        return screenDensity;
    }

     //FOR CHECKING PERMISSION
    public static boolean checkPermission(Context context,String per)
    {
        String permission="android.permission."+per;
        int res=context.checkCallingOrSelfPermission(permission);
        return res==0;
    }

    //Reading the file stored from cache
    public static String read(Context c,String name)
    {
        FileInputStream fin=null;

        try {
            fin=c.openFileInput(name);
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            Log.e("CrackRetail_UtilsERROR",e.getMessage());
        }

        //for reading the cache file existance
        if(fin==null)
            return null;

        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(fin));
        StringBuffer buff_str=new StringBuffer();
        String ls=System.getProperty("line.separator");

        String line=null;
        String temp=null;
        try {
            while((line=bufferedReader.readLine())!=null)
            {
                buff_str.append(line);
                buff_str.append(ls);
            }

            return buff_str.toString().length()==0?null:buff_str.toString();
        } catch (IOException e) {
            line=null;
            e.printStackTrace();
        }


        return null;
    }

   //storing the file to cache
    public static void write(Context context,String name,String data)
    {
        FileOutputStream fout=null;

        try {
            fout=context.openFileOutput(name,Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(fout));

       try {
           bufferedWriter.write(data);
           bufferedWriter.flush();
           return;
       }
       catch(Exception e)
       {
           Log.d("CrackRetailUtils",e.getMessage());
       }
        finally {
           try {
               bufferedWriter.close();
           } catch (IOException e) {
               Log.d("CrackRetailUtils",e.getMessage());
           }
       }

    }


    public static String getBase(String data, String fileName)
    {
        String tDir=System.getProperty("java.io.tmpdir");
        PrintWriter srcFile=null;

        String str=null;

        try {
            srcFile=new PrintWriter(tDir+"/"+fileName);
            srcFile.write(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            str=e.getMessage();
        }
        finally
        {
            if(srcFile!=null)
            {
                srcFile.close();
            }
        }

        String path="file://"+tDir+"/";
        return path;
    }

    public static String LoadResourceFile(Context context, String fileName) {
        InputStreamReader streamReader = null;

        try {
            streamReader = new InputStreamReader(context.getAssets().open(fileName), "UTF-8");
        } catch (IOException var17) {
            Log.d("CrackRetailUtils", "Unable to read from assets, will look in root");
        }

        if(streamReader == null) {
            String reader1 = internalLoad(context, fileName);
            return reader1;
        } else {
            BufferedReader reader = null;
            StringBuilder content = new StringBuilder();

            Object var6;
            try {
                reader = new BufferedReader(streamReader);

                String e;
                while((e = reader.readLine()) != null) {
                    content.append(e);
                    content.append('\n');
                }

                return content.toString();
            } catch (Exception var18) {
                var6 = null;
            } finally {
                if(reader != null) {
                    try {
                        reader.close();
                    } catch (IOException var16) {
                        Log.d("CrackRetailUtils", "cant close buffer");
                    }
                }

            }

            return (String)var6;
        }
    }

    private static String internalLoad(Context context, String fileName) {
        Log.d("CrackRetailUtils", "internalLoad, fileName: " + fileName);
        InputStream inputStream = context.getClass().getClassLoader().getResourceAsStream(fileName);
        if(inputStream == null) {
            inputStream = context.getClassLoader().getResourceAsStream(fileName);
        }

        InputStreamReader inputreader = null;

        try {
            inputreader = new InputStreamReader(inputStream, "UTF-8");
        } catch (UnsupportedEncodingException var8) {
            var8.printStackTrace();
        }

        BufferedReader buffreader = new BufferedReader(inputreader);
        StringBuilder text = new StringBuilder();

        String line;
        try {
            while((line = buffreader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        } catch (IOException var9) {
            return null;
        }

        return text.toString();
    }


    public static String getIPAddress(boolean useIPv4)
    {
        try
        {
            ArrayList interfaces= Collections.list(NetworkInterface.getNetworkInterfaces());
            Iterator iterator=interfaces.iterator();

           while(iterator.hasNext())
           {
               InetAddress address=(InetAddress)iterator.next();
               if(!address.isLoopbackAddress())
               {
                   String sAddr=address.getHostAddress();
                   boolean isIPv4=sAddr.indexOf(58)<0;
                   if(useIPv4)
                   {
                       if(isIPv4)
                       {
                           return sAddr;
                       }
                   }
                   else if(!isIPv4)
                   {
                       int delim=sAddr.indexOf(37);
                       return delim<0?sAddr.toUpperCase():sAddr.substring(0,delim).toUpperCase();
                   }


               }

           }
        }
        catch(Exception exception)
        {
            Log.d("CrackRetail_Error","Unable to Access IP Address");
        }

        return "";
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / 160.0F);
        return px;
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / 160.0F);
        return dp;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static String getVideoUrl(JSONObject adResp) {
        try {
            JSONObject e = adResp.getJSONArray("vasts").getJSONObject(0).getJSONObject("VAST").getJSONObject("Ad").getJSONObject("InLine").getJSONObject("Creatives").getJSONObject("Creative");
            String url = e.getJSONObject("Linear").getJSONObject("MediaFiles").getJSONObject("MediaFile").getString("__cdata");
            return url;
        } catch (Exception var3) {
            Log.d("CrackRetailBanner", "video url json exception");
            return null;
        }
    }

    public static String getAudioUrl(JSONObject adResp) {
        try {
            JSONObject e = adResp.getJSONArray("vasts").getJSONObject(1).getJSONObject("VAST").getJSONObject("Ad").getJSONObject("InLine").getJSONObject("Creatives").getJSONObject("Creative");
            String url = e.getJSONObject("Linear").getJSONObject("MediaFiles").getJSONObject("MediaFile").getString("__text");
            return url;
        } catch (Exception var3) {
            Log.d("CrackRetailBanner", "video url json exception");
            return null;
        }
    }

    public static boolean videoExists(String videoUrl) {
        try {
            File e = new File(videoUrl);
            return e.exists();
        } catch (Exception var2) {
            Log.d("CrackRetailBanner", "video exists exception");
            return false;
        }
    }

    public static JSONObject replaceToCached(String replace, JSONObject adResp) {
        try {
            adResp.getJSONArray("vasts").getJSONObject(0).getJSONObject("VAST").getJSONObject("Ad").getJSONObject("InLine").getJSONObject("Creatives").getJSONObject("Creative").getJSONObject("Linear").getJSONObject("MediaFiles").getJSONObject("MediaFile").put("__cdata", replace);
            return adResp;
        } catch (Exception var3) {
            Log.d("CrackRetailBanner", "replace with cached exception");
            return null;
        }
    }

    public static JSONObject replaceAudioCached(String replace, JSONObject adResp) {
        try {
            adResp.getJSONArray("vasts").getJSONObject(1).getJSONObject("VAST").getJSONObject("Ad").getJSONObject("InLine").getJSONObject("Creatives").getJSONObject("Creative").getJSONObject("Linear").getJSONObject("MediaFiles").getJSONObject("MediaFile").put("__text", replace);
            return adResp;
        } catch (Exception var3) {
            Log.d("CrackRetailBanner", "replace with cached exception");
            return null;
        }
    }

}
