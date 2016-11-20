package mte.crasmonitoring.monitoring;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;

import com.rvalerio.fgchecker.AppChecker;

import java.util.ArrayList;
import java.util.List;

import mte.crasmonitoring.R;

/**
 * Created by Mickael on 11/16/2016.
 */

public class AppsCheckerManager extends MonitoringBase {
    private static final int INTERVAL_TIME = (int) (DateUtils.SECOND_IN_MILLIS*5);
    private AppChecker appChecker;
    private Context context;
    private List<String> allowedPackages;
    private NotificationsBarLocker notificationsBarLocker;
    private NavigationButtonsWatcher navigationButtonsWatcher;
    public AppsCheckerManager(Context context, @Nullable MonitoringListener appCheckerListener)
    {
        super(appCheckerListener);
        this.context = context;
        notificationsBarLocker = new NotificationsBarLocker(context);
        notificationsBarLocker.lock();

        navigationButtonsWatcher = new NavigationButtonsWatcher(context, new NavigationButtonsWatcher.OnNavigationButtonClickListener() {
            @Override
            public void onNavigationButtonClick(int which) {
                userIsUsingUnapprovedApp();
            }
        });
        navigationButtonsWatcher.startMonitoring();

    }

    @Override
    public void startMonitoring()
    {
        allowedPackages = getAllowedPackages();
        appChecker = new AppChecker();
        appChecker
                .other(new AppChecker.Listener() {
                    @Override
                    public void onForeground(String packageName) {
                        if (!allowedPackages.contains(packageName))
                            userIsUsingUnapprovedApp();
                         else
                            userIsUsingApprovedApp();
                    }
                })
                .timeout(INTERVAL_TIME)
                .start(context);
    }

    private synchronized void userIsUsingUnapprovedApp()
    {
        didBad();

        if(notificationsBarLocker.isLocked())
            notificationsBarLocker.unlock();

        if(navigationButtonsWatcher.isMonitoring())
            navigationButtonsWatcher.stopMonitoring();
    }

    private void userIsUsingApprovedApp()
    {
        didGood();

        if(notificationsBarLocker.isUnlocked())
            notificationsBarLocker.lock();

        if(!navigationButtonsWatcher.isMonitoring())
            navigationButtonsWatcher.startMonitoring();

    }

    private List<String> getAllowedPackages() {
        PackageManager pm = context.getPackageManager();
        ArrayList<String> acceptedPackagesList = new ArrayList<>();
        acceptedPackagesList.add(context.getPackageName());
        acceptedPackagesList.add(context.getString(R.string.google_maps_package_name));
        acceptedPackagesList.add(context.getString(R.string.waze_package_name));

        //Get dialer's package name
        Intent i = new Intent();
        i.setAction(Intent.ACTION_DIAL);
        List<ResolveInfo> actionActivities = pm.queryIntentActivities(i, 0);
        for (ResolveInfo info : actionActivities) {
            acceptedPackagesList.add(info.activityInfo.packageName);
        }

        return acceptedPackagesList;

    }

    @Override
    public void stopMonitoring()
    {
        appChecker.stop();

        if(notificationsBarLocker.isLocked())
            notificationsBarLocker.unlock();

        if(navigationButtonsWatcher.isMonitoring())
            navigationButtonsWatcher.stopMonitoring();
    }

}
