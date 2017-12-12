This may look like an old topic, as lots of documents or posts could be found on Google in recent days regards to the Handler Memory Leak issue. Even Android Lint will warn you immediately if you have Handler as an inner class in your code. 
However, the problem I found with the popular solution is most of them seem has blear understanding to the usage of Java Weak Reference. 
So, I think it still worth to drill deeper into this typical coding mistake.

First of all, the typical pattern of an inner Handler class may leak memory is as the below code clip shows:


``` java
public class LeakedActivity extends Activity {
    private final Handler mLeakyHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // process the message
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // do something
            }
        }, 1000 * 60 * 10);  // post a message and delay its execution for 10 minutes
    }
}
```

Inner Handler class itself has no problem. The potential memory leaking is caused by the delayed Runnable object.
Since the runnable object has the strong reference to it outer class LeakedActivity object, unless the Runnable object be released from the Main Thread, the LeakedActivity object won't be able to be recycled by GC even it has been destroyed. Literally, it may look like it's a small chance that the Activity be destroyed before the delayed Runnable be executed and unattached from Main Thread. However, please keep in mind an Activity could be destroyed by Android OS at anytime when configuration changed. Configuration change may caused by screen rotation, screen size change, device language change, etc. Although one activity instance be leaked few seconds may not a big issue, imaging every configuration change leaks 1 more activity object, accumulatively the leaked activity objects will eventually exhaust the Android VM heap and cause Out-Of-Memory exception. 

What we could do to avoid leaking the destroyed activity object? Apparently, the first thought could be remove the strong reference of the LeakedActivity from the Runnable object. This is how the most popular solution proposes. So, we need to convert the inner Handler class to nested Handler class. Furthermore, the nested Handler class hold a weak reference to the Activity. Typically, the code may look like below:


``` java
public class SampleActivity extends Activity {
    private static class MyHandler extends Handler {
        final WeakReference<SampleActivity> mActivityRef;
        MyHandler(SampleActivity activity) {
            mActivityRef = new WeakReference<>(activity); 
        }

        @Override
        public void handleMessage(Message msg) {
           SampleActivity activity = mActivityRef.get();
           if (activity != null) {
                // do something and call back to activity
           }
        }
    }
    
    private MyHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new MyHandler(this);
        // post a message and delay for 10 minutes
        mHandler.sendEmptyMessageDelayed(0, 1000 * 60 * 10);    
    }
}

```

Now since SampleActivity object is weak referenced by MyHandler object, SampleActivity object could be recycled when it is be destroyed even at that time MyHandler still has pending message in the Main Thread message queue.
Everything looks fine, except one issue that MyHandler won't be able to send callback to SampleActivity if the weak reference returns NULL.

It may look like not an issue at the first time as we may assume weak reference returns NULL when the target object already be recycled by GC.
However, Android document does not specify when the weak reference returns NULL. Base on my unit test, the behavior varies for different Android version. The weak reference may even returns NULL when SampleActivity object still alive in memory. It is because even the weak reference itself could be recycled when GC happens.


So, what's the better solution if we must guarantee the SampleActivity always get the callback from the MyHandler object?
After revisit the first solution, I'd like to ask what the good for keeping pending Message for MyHandler when the SampleActivity already be destroyed. Fortunately, Handler class does have the API to cancel the pending Message object.
Let's move on to the #2 solution:


``` java
public class SampleActivity extends Activity {
    private static class MyHandler extends Handler {
        private SampleActivity mActivity;
        MyHandler(SampleActivity activity) {
            mActivity = activity; 
        }

        private void cancel() {
            mActivity = null;
            removeCallbacksAndMessages(null);
        }

        @Override
        public void handleMessage(Message msg) {
           if (mActivity != null) {
                // do somethin and call back to activity
           }
        }
    }
    
    private MyHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new MyHandler(this);
        // post a message and delay for 10 minutes
        mHandler.sendEmptyMessageDelayed(0, 1000 * 60 * 10);    
    }

    @Override
    proteced void onDestroy() {
        mHandler.cancel();  
    }
}

```

The difference of the #2 solution is it immediately cancel the pending message and reset the SampleActivity strong reference for MyHandler object. No SampleActivity object could be leaked since its reference has been explicitly reset from the Handler. On the other hand, since MyHandler has the strong reference to SampleActivity, it guarantees MyHandler can always call back to SampleActivity until SampleActivity be destroyed.


Alright, it is already a long post. In the end, lesson learnt from this research:

- Weak reference is only good for optional communication. Whether reference to the target object still alive or not create no harm to the system.
- Anything be put into a pending queue has to be canceled immediately when they are no longer needed.

That's it. Bye for now.

<!--eof-->