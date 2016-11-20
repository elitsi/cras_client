package mte.crasmonitoring.monitoring;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

/**
 * Watches for navigation button clicks (e.g. home button, recent apps button) and notifies attached listener.
 */
public class NavigationButtonsWatcher implements MonitoringAbilityService{

    public static final int BUTTON_HOME = 0;
    public static final int BUTTON_RECENT_APPS = 1;
    public static final int BUTTON_SEARCH = 2;

    private Context mContext;
    private OnNavigationButtonClickListener mListener;
    private ActionCloseSystemDialogsReceiver mReceiver;
    private boolean mIsWatching;

    public NavigationButtonsWatcher(Context context, OnNavigationButtonClickListener listener) {
        mContext = context;
        mListener = listener;
    }

    public boolean isMonitoring()
    {
        return mIsWatching;
    }

    /**
     * Start watching for navigation buttons clicks
     */
    @Override
    public void startMonitoring() {
        if (mIsWatching) {
            return;
        }

        if (mReceiver == null) {
            mReceiver = new ActionCloseSystemDialogsReceiver();
        }
        mContext.registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        mIsWatching = true;
    }

    /**
     * Stop watching for navigation buttons clicks
     */
    @Override
    public void stopMonitoring() {
        if (mIsWatching && mReceiver != null) {
            mContext.unregisterReceiver(mReceiver);
        }
        mIsWatching = false;
    }

    public interface OnNavigationButtonClickListener {
        void onNavigationButtonClick(int which);
    }

    private class ActionCloseSystemDialogsReceiver extends BroadcastReceiver {
        final String SYSTEM_DIALOG_REASON_KEY = "reason";
        final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
        final String SYSTEM_DIALOG_REASON_SEARCH = "search";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (!TextUtils.isEmpty(reason)) {
                    checkReason(reason);
                }
            }
        }

        private void checkReason(String reason) {
            if (mListener == null) {
                return;
            }

            switch (reason) {
                case SYSTEM_DIALOG_REASON_HOME_KEY:
                    mListener.onNavigationButtonClick(BUTTON_HOME);
                    break;
                case SYSTEM_DIALOG_REASON_RECENT_APPS:
                    mListener.onNavigationButtonClick(BUTTON_RECENT_APPS);
                    break;
                case SYSTEM_DIALOG_REASON_SEARCH:
                    mListener.onNavigationButtonClick(BUTTON_SEARCH);
                    break;
            }
        }
    }
}
