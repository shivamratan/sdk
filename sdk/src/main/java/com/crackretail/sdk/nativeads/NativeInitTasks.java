package com.crackretail.sdk.nativeads;

import com.crackretail.sdk.bannerads.BannerInitTasks;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shivam on 22-Jul-16.
 */
public class NativeInitTasks
{
    boolean done=false;
    NativeInitTasks.DoneCallback callback=null;
    Map<NativeInitTasks.Tasks,Boolean> tasks=new HashMap<>();


    public NativeInitTasks(NativeInitTasks.DoneCallback callback)
    {
        tasks.put(Tasks.GET_ADVERTISING_ID,false);
        tasks.put(Tasks.GET_LAYOUT,false);
        tasks.put(Tasks.GET_ADVERTISING_ID,false);
        tasks.put(Tasks.GET_LOCATION,false);

        this.callback=callback;

    }

    public void notifyTaskDone(NativeInitTasks.Tasks task)
    {
        synchronized (this)
        {
            if(this.done)
            {
                return;
            }
        }

        this.tasks.put(task,Boolean.valueOf(true));
        this.done=true;
        this.callback.onDone();
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
