package com.crackretail.sdk.bannerads;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shivam on 29-Jun-16.
 */
public class BannerMakeTasks
{
    boolean done=false;
    BannerMakeTasks.DoneCallback callback;
//    Map<BannerMakeTasks.Tasks,Boolean> tasks=new EnumMap(BannerMakeTasks.class);
      Map<BannerMakeTasks.Tasks,Boolean> tasks=new HashMap<>();

    /*setting all the value of Advertising ,layout getting,getting location &loading JS files to Webview to false
     *we will set to true when all this completed
     */
    public BannerMakeTasks(BannerMakeTasks.DoneCallback callback)
    {
        tasks.put(Tasks.GET_WATERFALLS,Boolean.valueOf(false));
        this.callback=callback;
    }

    public void notifyTaskDone(BannerMakeTasks.Tasks task)
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
        GET_WATERFALLS;

        private Tasks()
        {

        }
    }

    public interface DoneCallback
    {
        void onDone();
    }

}
