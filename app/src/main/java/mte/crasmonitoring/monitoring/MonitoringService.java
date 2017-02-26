package mte.crasmonitoring.monitoring;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import mte.crasmonitoring.rest.APICallbacks;
import mte.crasmonitoring.rest.APIManager;
import mte.crasmonitoring.ui.activities.MonitoringActivity;
import mte.crasmonitoring.R;
import mte.crasmonitoring.eventbus.Events;
import mte.crasmonitoring.utils.Constants;


public class MonitoringService extends Service {

    private final static int NOTIFICATION_ID = 1234;
    private final static String STOP_SERVICE = MonitoringService.class.getPackage() + ".stop";

    private MonitoringAbilityService mPhoneCallWatcher;
    private MonitoringAbilityService appsCheckerManager;
    private MonitoringAbilityService speedLimitManager;
    private BroadcastReceiver stopServiceReceiver;

    private String supervisorId;

    public static void start(Context context, String supervisorId) {
        Intent intent = new Intent(context, MonitoringService.class);
        intent.putExtra(Constants.MONITOR_SUPERVISOR_ID_KEY,supervisorId);
        context.startService(intent);
    }

    public static void stop(Context context) {
        context.stopService(new Intent(context, MonitoringService.class));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null)
            supervisorId = intent.getStringExtra(Constants.MONITOR_SUPERVISOR_ID_KEY);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerReceivers();
        startMonitoringApps();
        startWatchingCalls();
        startTrackingDrivingSpeed();
        createStickyNotification();
    }

    private void getSupervisorId()
    {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopMonitoringApps();
        removeNotification();
        unregisterReceivers();
        stopTrackingDrivingSpeed();
        mPhoneCallWatcher.stopMonitoring();
        stopSelf();
    }

    private void startMonitoringApps() {
        appsCheckerManager = new AppsCheckerManager(getBaseContext(), new MonitoringBase.MonitoringListener() {
            @Override
            public void didBad() {
                Toast.makeText(getBaseContext(), "You are using an unapproved app.", Toast.LENGTH_SHORT).show();
                Log.v("MonitoringUpdates", "You are using an unapproved app.");
                EventBus.getDefault().post(new Events.AppViolationEvent());
                sendAppViolationEvent();
            }
        });

        appsCheckerManager.startMonitoring();
    }

    private void sendAppViolationEvent()
    {
        APIManager.sendAppViolationEvent(getBaseContext(), supervisorId, new APICallbacks<String>() {
            @Override
            public void successfulResponse(String s) {

            }

            @Override
            public void FailedResponse(String errorMessage) {

            }
        });
    }

    private void stopMonitoringApps() {
        appsCheckerManager.stopMonitoring();
    }

    private void registerReceivers() {
        stopServiceReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                stopSelf();
            }
        };
        registerReceiver(stopServiceReceiver, new IntentFilter(STOP_SERVICE));
    }

    private void unregisterReceivers() {
        unregisterReceiver(stopServiceReceiver);
    }

    private Notification createStickyNotification() {

        Intent intent = new Intent(this, MonitoringActivity.class);
        intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        NotificationManager manager = ((NotificationManager) getSystemService(NOTIFICATION_SERVICE));
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setAutoCancel(false)
                .setContentText(getString(R.string.monitor_notification_title))
                .setContentTitle(getString(R.string.monitor_notification_title))
                .setContentIntent(PendingIntent.getActivity(this, 0, intent,
                        PendingIntent.FLAG_CANCEL_CURRENT))
                .setWhen(0)
                .build();
        manager.notify(NOTIFICATION_ID, notification);
        return notification;
    }

    private void removeNotification() {
        NotificationManager manager = ((NotificationManager) getSystemService(NOTIFICATION_SERVICE));
        manager.cancel(NOTIFICATION_ID);
    }


    private void startTrackingDrivingSpeed()
    {
        speedLimitManager = new SpeedLimitManager(getBaseContext(), new MonitoringBase.MonitoringListener() {
            @Override
            public void didBad() {
                Toast.makeText(getBaseContext(),"You are over the speeding limit",Toast.LENGTH_LONG).show();
                Log.v("MonitoringUpdates", "You are over the speeding limit");
                int drivingSpeed = ( (SpeedLimitManager) speedLimitManager).getDrivingSpeed();
                EventBus.getDefault().post(new Events.DrivingLimitViolationEvent(drivingSpeed));
            }
        });
        speedLimitManager.startMonitoring();
    }

    private void stopTrackingDrivingSpeed()
    {
        speedLimitManager.stopMonitoring();
    }

    private void startWatchingCalls()
    {
        mPhoneCallWatcher = new PhoneCallWatcher(this, new PhoneCallWatcher.OnCallStateListener() {
            @Override
            public void onCallAnswered() {
                //Activate loudspeaker
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.v("Receiver", "Setting speaker ON");
                        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                        audioManager.setMode(AudioManager.MODE_IN_CALL );
                        audioManager.setSpeakerphoneOn(true);
                    }
                },300);

            }

            @Override
            public void onCallEnded() {
                //Activate loudspeaker
                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                audioManager.setMode(AudioManager.MODE_NORMAL);
                audioManager.setSpeakerphoneOn(false);
            }
        });
        mPhoneCallWatcher.startMonitoring();
    }

}
