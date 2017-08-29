This may look like an old topic. Because lots of documents or posts could be found on Google in recent days which are regarding to the handler memory memory leak issue. Even Android lint will warn you immediately if you have Handler as an inner class in your code. 
However, the problem I found with the popular solution is seems has blear understanding to the usage of Java Weak Reference. 
So, I think it still worth to drill deeper into this problem.

First of all, the reason why inner class Handler may leak memory is as the below code clip shows:


```
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
