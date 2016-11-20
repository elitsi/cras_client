package mte.crasmonitoring.monitoring;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Locks Notification Bar so it can not be dragged down.
 */
public class NotificationsBarLocker {

    private Context mContext;
    private static View mStatusBarView;

    public NotificationsBarLocker(Context context) {
        mContext = context;
    }

    /**
     * Locks notifications bar.
     */
    public void lock() {
        WindowManager manager = ((WindowManager) mContext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        localLayoutParams.gravity = Gravity.TOP;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                // Draws over status bar
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        localLayoutParams.height = (int) (34 * mContext.getResources()
                .getDisplayMetrics().scaledDensity);
        localLayoutParams.format = PixelFormat.TRANSPARENT;
        mStatusBarView = new StatusBarHackView(mContext);
        manager.addView(mStatusBarView, localLayoutParams);
    }

    /**
     * Unlocks notifications bar.
     */
    public void unlock() {
        WindowManager manager = ((WindowManager) mContext.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE));
        if(mStatusBarView != null) {
            manager.removeView(mStatusBarView);
            mStatusBarView = null;
        }
    }

    public boolean isUnlocked()
    {
        return mStatusBarView == null;
    }

    public boolean isLocked()
    {
        return !isUnlocked();
    }

    private static class StatusBarHackView extends ViewGroup {

        public StatusBarHackView(Context context) {
            super(context);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent event) {
            return true;
        }
    }
}
