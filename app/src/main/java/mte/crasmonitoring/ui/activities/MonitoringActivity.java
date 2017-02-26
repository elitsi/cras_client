package mte.crasmonitoring.ui.activities;

import android.app.ActivityManager;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import net.danlew.android.joda.DateUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.joda.time.DateTime;

import mte.crasmonitoring.R;
import mte.crasmonitoring.eventbus.Events;
import mte.crasmonitoring.monitoring.MonitoringService;
import mte.crasmonitoring.rest.APICallbacks;
import mte.crasmonitoring.rest.APIManager;
import mte.crasmonitoring.utils.Constants;

public class MonitoringActivity extends AppCompatActivity {
    TextView monitoringLogTv;
    private String supervisorId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);

        int flags = getIntent().getFlags();
        boolean isAppLoadedFromHistory = (flags & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) != 0;
        if (isAppLoadedFromHistory)
        {
            if(!isMonitoringServiceRunning())
            {
                //App is started from history && monitor service is no running ==> there's no monitoring going on ==> move user to main activity.
                startActivity(new Intent(this, ShowUserListsActivity.class));
                finish();
            }
        }
        else
        {
            //App is not from history ==> it's from firebase notification or local monitoring notification
            Intent intent = getIntent();
            if(savedInstanceState == null && TextUtils.equals(intent.getStringExtra(Constants.MONITOR_OPEN_ACTIVITY_TYPE_KEY),Constants.MONITOR_OPEN_ACTIVITY_TYPE_ADDED_SUPERVISOR_VALUE))
            {
                supervisorId = intent.getStringExtra(Constants.MONITOR_SUPERVISOR_ID_KEY);
                APIManager.acceptMonitorRequest(getBaseContext(), supervisorId, new APICallbacks<String>() {
                    @Override
                    public void successfulResponse(String s)
                    {
                        MonitoringService.start(MonitoringActivity.this, supervisorId);
                    }
                    @Override
                    public void FailedResponse(String errorMessage) {}
                });
            }
        }

        monitoringLogTv = (TextView) findViewById(R.id.tv_monitoring_log);



        (findViewById(R.id.btn_open_waze)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openApp(getString(R.string.waze_package_name));
            }
        });

        (findViewById(R.id.btn_open_google_maps)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openApp(getString(R.string.google_maps_package_name));
            }
        });

        (findViewById(R.id.btn_stop_monitoring)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MonitoringService.stop(getBaseContext());
                finish();
            }
        });

        EventBus.getDefault().register(this);

    }

    private boolean isMonitoringServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (MonitoringService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public boolean openApp(String packageName) {
        PackageManager manager = getPackageManager();
        try {
            Intent i = manager.getLaunchIntentForPackage(packageName);
            if (i == null) {
                return false;
                //throw new PackageManager.NameNotFoundException();
            }
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            startActivity(i);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onDrivingSpeedViolationEvent(Events.DrivingLimitViolationEvent drivingLimitViolationEvent)
    {

        StringBuilder stringBuilder = new StringBuilder().append(monitoringLogTv.getText().toString())
                .append("\n")
                .append("Driving limit event - speed - ")
                .append(drivingLimitViolationEvent.getDrivingSpeed())
                .append(", at - ")
                .append(DateUtils.formatDateTime(this, DateTime.now(), DateUtils.FORMAT_SHOW_TIME));

        monitoringLogTv.setText(stringBuilder.toString());
    }

    @Subscribe
    public void onAppViloationEvent(Events.AppViolationEvent appViolationEvent)
    {
        StringBuilder stringBuilder = new StringBuilder().append(monitoringLogTv.getText().toString())
                .append("\n")
                .append("App event ")
                .append("at - ")
                .append(DateUtils.formatDateTime(this, DateTime.now(), DateUtils.FORMAT_SHOW_TIME));

        monitoringLogTv.setText(stringBuilder.toString());
    }

}
