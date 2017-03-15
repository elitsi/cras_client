package mte.crasmonitoring.monitoring;

import android.content.Context;
import android.os.Handler;
import android.text.format.DateUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mickael on 11/17/2016.
 */

public abstract class MonitoringBase implements MonitoringAbilityService{

    public interface MonitoringListener {
        void didBad();
    }

    private boolean needsToMonitor = true;
    private static final int CHECKING_TIMEOUT = 30; // (int) (DateUtils.MINUTE_IN_MILLIS/2);//(int) (DateUtils.MINUTE_IN_MILLIS*5);
    private boolean monitoringTimeoutPassed = true;
    private MonitoringListener monitoringListener;
    private ScheduledExecutorService timeoutExecutor;

    MonitoringBase(MonitoringListener monitoringListener)
    {
        timeoutExecutor = Executors.newSingleThreadScheduledExecutor();
        this.monitoringListener = monitoringListener;
    }

    private Runnable checkTimeoutRunnable = new Runnable() {
        @Override
        public void run() {
            monitoringTimeoutPassed = true;
        }
    };


    void didGood()
    {
        needsToMonitor = true;
    }

    void didBad()
    {
        if (needsToMonitor && monitoringTimeoutPassed) {
            if(monitoringListener != null)
                monitoringListener.didBad();
            needsToMonitor = false;
            monitoringTimeoutPassed = false;
            timeoutExecutor.schedule(checkTimeoutRunnable, CHECKING_TIMEOUT, TimeUnit.SECONDS);
        }
    }
}
