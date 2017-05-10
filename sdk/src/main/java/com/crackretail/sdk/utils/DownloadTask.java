package com.crackretail.sdk.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Shivam on 29-Jun-16.
 */

/*
DownloadTask is AsyncTask which fetches the content from server to cache directory
* */
public class DownloadTask extends AsyncTask<String,Void,String>
{
    Context context;
    String filepath;

    public DownloadTask(Context context)
    {
        this.context=context;
    }

    @Override
    protected String doInBackground(String... params)
    {
        InputStream input=null;
        FileOutputStream output=null;
        HttpURLConnection connection=null;

        try {
            URL url=new URL(params[0]);
            connection=(HttpURLConnection)url.openConnection();
            connection.connect();
            String filename;

            if(connection.getResponseCode()!=200)
            {
                Log.e("CrackRetailNetwork","Bad Connection Request");
                filename=null;
            }
            else
            {
                input=connection.getInputStream();
                filename=params[0].substring(params[0].lastIndexOf("/"));
                this.filepath=this.context.getCacheDir()+"/"+filename;
                output=new FileOutputStream(this.filepath);
                byte[] data=new byte[4096];

                int count;
                while((count=input.read(data))!=-1)
                {
                    output.write(data,0,count);
                }


            }


        } catch (Exception e)
        {
            Log.e("CrackRetailNetwork","DownloadTaskError",e);
        }
        finally
        {
          try {
              if (input != null) {
                  input.close();
              }

              if(output!=null)
              {
                  output.close();
              }


          }
          catch(Exception e)
          {

          }

            if(connection!=null)
            {
                connection.disconnect();
            }

        }
        return this.filepath;
    }
}
