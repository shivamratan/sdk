package com.crackretail.sdk.networking;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Shivam on 01-Jul-16.
 */
public class CrackRetailRequest
{
    String url;
    Map<String, String> parameters;
    Map<String, String> headers;
    JSONObject data;
    Context context;




    public CrackRetailRequest(Context context,
                              String url)
    {
        this.url=url;
        this.context=context;
        this.parameters=new HashMap<>();
        this.headers=new HashMap<>();

    }

    public CrackRetailRequest setParam(String key,String value)
    {
        parameters.put(key,value);
        return this;

    }

    public CrackRetailRequest setData(JSONObject data)
    {
        this.data=data;
        return this;
    }

    public CrackRetailRequest setHeader(String key,String value)
    {
        this.headers.put(key, value);
        return this;
    }


    private static void _call(String url, final String method, final Object data, final Map<String, String> reqHeaders, final CrackRetailRequest.ResponseFormatter formatter, final AsyncCallback cb)
    {
        boolean leftinitial=true;
        //fullurl=url+query

        //Query creation with use of Map data
     // Log.e("GET_URL","the requested URL is"+url);
        StringBuilder query=new StringBuilder();
     //different from ideal Code
      if(data!=null) {
          if (method.equals("GET")) {
            if(data instanceof JSONObject) {
                JSONObject partialURL = (JSONObject) data;
              //  Log.e("Error Data", partialURL + "");
                Iterator finalURL = partialURL.keys();

                while (finalURL.hasNext()) {
                    String key = (String) finalURL.next();
                    try {
                        if (leftinitial) {
                            query.append(key + "=" + URLEncoder.encode( partialURL.get(key)+"", "UTF-8"));
                            leftinitial = false;
                        } else {
                            query.append("&" + key + "=" + URLEncoder.encode(partialURL.get(key)+"", "UTF-8"));
                        }
                    } catch (Exception exception) {
                        Log.e("CrackRetail_Net_Error", exception.toString());
                    }

                }

            }
            else if(data instanceof Map)
            {
                Map partialURL = (Map) data;
              //  Log.e("Error Data", partialURL + "");
                Iterator finalURL = partialURL.keySet().iterator();

                while (finalURL.hasNext()) {
                    String key = (String) finalURL.next();
                    try {
                        if (leftinitial) {
                            query.append(key + "=" + URLEncoder.encode(partialURL.get(key)+"", "UTF-8"));
                            leftinitial = false;
                        } else {
                            query.append("&" + key + "=" + URLEncoder.encode(partialURL.get(key)+"", "UTF-8"));
                        }
                    } catch (Exception exception) {
                        Log.e("CrackRetail_Net_Error", exception.toString());
                    }

                }
            }

          }
      }

        //Attaching url with Query to create full URL
       String fullURL=url;
        if(query.length()>0){
            if(url.indexOf("?")>0){
                fullURL+="&"+query.toString();
            }
            else {
                fullURL+="?"+query.toString();
            }
        }




       // Log.d("CR_FULL_URL","The request URL is "+fullURL);

        //AsyncTask for creating GET OR POST METHOD TYPE HTTP Connections with server used for uploading data & downloading response from the server

        if(fullURL.indexOf("crackretail.com")==-1) {
            new AsyncTask<String, String, String>() {

                int status;
                Object response;
                Map<String, List<String>> responseHeader;
                Exception exception;

                @Override
                protected String doInBackground(String... params) {
                    HttpURLConnection con = null;
                    BufferedWriter writer = null;

                    OutputStream os;

                    try {
                        URL url = new URL(params[0]);

                        // Log.e("error_found",params[0]+" error found !!");

                        con = (HttpURLConnection) url.openConnection();
                        con.setRequestMethod(method);
                        con.setRequestProperty("User-Agent", System.getProperty("http.agent"));
                        Iterator iterator1 = reqHeaders.keySet().iterator();
                        while (iterator1.hasNext()) {
                            String h = (String) iterator1.next();
                            con.setRequestProperty(h, (String) reqHeaders.get(h));
                        }
                        if (method.equals("POST")) {
                            con.setDoInput(true);
                            con.setDoOutput(true);
                            os = con.getOutputStream();
                            writer = new BufferedWriter(new OutputStreamWriter(os));
                            writer.write(((JSONObject) data).toString());
                            writer.flush();
                            writer.close();
                            os.close();
                        }


                        this.status = con.getResponseCode();
                        this.response = formatter.format(con.getInputStream());
                        this.responseHeader = con.getHeaderFields();

                        return "success";

                    } catch (Exception exception) {
                        this.exception = exception;
                        os = null;
                        Log.e("Banner_Exc", exception.toString());
                        exception.printStackTrace();
                    } finally {
                        try {

                            if (con != null) {
                                con.disconnect();
                            }
                        } catch (Exception exception) {
                            Log.d("CrackRetailErr", exception.toString());
                        }


                    }

                    return os.toString();
                }

                @Override
                protected void onPostExecute(String s) {
                    if (cb != null) {
                        if (s == null) {
                            cb.onError(this.exception);
                        } else {
                            cb.onComplete(status, this.response, this.responseHeader);
                        }
                    }

                }

            }.execute(fullURL);


        }
    }


    //for Parsing JSON response
    public void getJSON(final AsyncCallbackJSON cb)
    {
        this.get(new AsyncCallback() {
            @Override
            public void onComplete(int code, Object response, Map<String, List<String>> headers) {

            String jsonstr=(String)response;
                if(jsonstr!=null&&jsonstr.length()!=0)
                {
                    try {
                        JSONObject jsonObject=new JSONObject(jsonstr);
                        cb.onComplete(code,jsonObject,headers);
                    } catch (JSONException e) {
                        Log.e("CrackRetailError","Error Parsing JSON response"+response);
                        cb.onError(e);
                    }
                }

            }

            @Override
            public void onError(Exception exception) {
                cb.onError(exception);
            }
        });
    }

 //for Parsing the Bitmap Type Response from the server
    public void getBitmap(final AsyncCallbackBitmap cb)
    {
        _call(this.url, "GET", this.parameters, this.headers, new ResponseFormatter() {
            @Override
            public Object format(InputStream response)
            {
                return BitmapFactory.decodeStream(response);
            }
        }, new AsyncCallback() {
            @Override
            public void onComplete(int code, Object response, Map<String, List<String>> headers) {
             cb.onComplete(code,(Bitmap)response,headers);
            }

            @Override
            public void onError(Exception exception)
            {
                cb.onError(exception);

            }
        });
    }

    public void get(AsyncCallback cb)
    {
        _call(this.url,"GET",this.data,this.headers,(new DefaultResponseFormatter()),cb);
    }

    public void post(AsyncCallback cb)
    {
        _call(this.url,"POST",this.data,this.headers,(new DefaultResponseFormatter()),cb);
    }


    private class DefaultResponseFormatter implements CrackRetailRequest.ResponseFormatter
    {

        public DefaultResponseFormatter()
        {

        }

        //for parsing of String type response & returning it
        @Override
        public Object format(InputStream response)
        {
            StringBuilder sb=new StringBuilder();
            BufferedReader reader=new BufferedReader(new InputStreamReader(response));

            try
            {
                String line;
                while((line=reader.readLine())!=null)
                {
                   sb.append(line+"\n");
                }
            }
            catch (Exception exception)
            {
                Log.e("CrackRetailNetwork","Error Reading the Response",exception);
            }
            finally {
                if(reader!=null)
                {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e("CrackRetailNetwork","Error Closing the Reader",e);
                    }
                }

            }

            return sb.toString();
        }
    }

    private interface ResponseFormatter
    {
        Object format(InputStream var1);
    }


}
