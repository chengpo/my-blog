The TouchDelegate is an useful helper class to expand touch area of small image or text view.
The view whose touch area be changed is called the delegate view.
To change the touch area, a TouchDelegate instance should be set to the delegate view's direct parent view.

Setting TouchDelegate is as simple as :

``` java
final View parentView = (View) delegateView.getParent();
parentView.post(new Runnable() {
    @Override
    public void run() {
        final Rect rect = new Rect();
        delegateView.getHitRect(rect);
        
        // expand touch area
        rect.top -= 100;
        rect.left -= 100;
        rect.right += 100;
        rect.bottom += 100;
        
        parentView.setTouchDelegate(new TouchDelegate(rect, delegateView));
    }
});
```

A limitation of the setTouchDelegate() API is one view can only have one TouchDelegate. 
What if we need to expand multiple smaller views in one container view?

Thanks to 'Decorator Design Pattern', it is easy to add new behavior to TouchDelegate class.
A CompositeTouchDelegate can forward touch event to a secondary TouchDelegate like :

``` java
public class CompositeTouchDelegate extends TouchDelegate {
    private final TouchDelegate secondaryDelegate;
    
    public CompositeTouchDelegate(Rect bounds, View delegateView, TouchDelegate secondaryDelegate) {
        super(bounds, delegateView);
        this.secondaryDelegate = secondaryDelegate;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (secondaryDelegate == null) {
            return super.onTouchEvent(event);
        }
        
        final float x = event.getX();
        final float y = event.getY();
        
        final result = super.onTouchEvent(event);
        
        // since super.onTouchEvent() transform event coordinates into the delegate view bound,
        // we need to restore the original event coordinate for the secondary touch delegate.
        event.setLocation(x, y);
        return result || secondaryDelegate.onTouchEvent(event);
    }
}
```

Then it is easy to chain multiple TouchDelegate like :

``` java
final View parentView = (View) delegateView.getParent();
parentView.post(new Runnable() {
    @Override
    public void run() {
        final Rect rect = new Rect();
        delegateView.getHitRect(rect);
        
        // expand touch area
        rect.top -= 100;
        rect.left -= 100;
        rect.right += 100;
        rect.bottom += 100;
        
        // get existing touch delegate
        final TouchDelegate existingDelegate = parentView.getTouchDelegate();
        
        // make existing delegate as secondary touch delegate
        final TouchDelegate compositeDelegate = new CompositeTouchDelegate(rect, delegateView, existingDelegate);
        
        parentView.setTouchDelegate(compositeDelegate);
    }
});
```

The sample code has a small issue. I don't consider checking overlapping delegate bounds.
A better solution may automatically resolve the conflicting bounds. 
<!--eof-->