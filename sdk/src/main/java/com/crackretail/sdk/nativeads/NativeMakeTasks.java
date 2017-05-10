package com.crackretail.sdk.nativeads;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shivam on 22-Jul-16.
 */
public class NativeMakeTasks
{
    boolean done=false;
    NativeMakeTasks.DoneCallback callback=null;
    Map<NativeMakeTasks.Tasks,Boolean> tasks=new HashMap<>();


    public NativeMakeTasks(NativeMakeTasks.DoneCallback callback)
    {
        tasks.put(Tasks.GET_WATERFALLS,false);
        this.callback=callback;
    }

    public void notifyTaskDone(NativeMakeTasks.Tasks task)
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
