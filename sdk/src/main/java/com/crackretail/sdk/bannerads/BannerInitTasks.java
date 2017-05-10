package com.crackretail.sdk.bannerads;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shivam on 29-Jun-16.
 */
public class BannerInitTasks
{
    boolean done=false;
    BannerInitTasks.DoneCallback callback;
    Map<BannerInitTasks.Tasks,Boolean> tasks=new HashMap<>();
   // Map<BannerInitTasks.Tasks,Boolean> tasks=new EnumMap(BannerInitTasks.class);

    /*setting all the value of Advertising ,layout getting,getting location &loading JS files to Webview to false
     *we will set to true when all this completed
     */
    public BannerInitTasks(BannerInitTasks.DoneCallback callback)
    {
        tasks.put(Tasks.GET_ADVERTISING_ID,Boolean.valueOf(false));
        tasks.put(Tasks.GET_LAYOUT,Boolean.valueOf(false));
        tasks.put(Tasks.GET_LOCATION,Boolean.valueOf(false));
        tasks.put(Tasks.LOAD_JS,Boolean.valueOf(false));
        this.callback=callback;
    }

    public void notifyTaskDone(BannerInitTasks.Tasks task)
    {
      synchronized (this)
      {
          if(this.done)
          {
              return;
          }
      }

     this.tasks.put(task,Boolean.valueOf(true));
     boolean allDone=true;

      if(allDone)
      {
          this.done=true;
          //calling onDone() method defined in setup while definding the definition of the interface
          this.callback.onDone();
      }

    }



    public static enum Tasks
    {
        LOAD_JS,
        GET_LAYOUT,
        GET_ADVERTISING_ID,
        GET_LOCATION;

        private Tasks()
        {

        }
    }

    public interface DoneCallback
    {
        void onDone();
    }
}
