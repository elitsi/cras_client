package mte.crasmonitoring.ui.activities;

import android.app.usage.UsageEvents;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import net.danlew.android.joda.DateUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.joda.time.DateTime;

import mte.crasmonitoring.R;
import mte.crasmonitoring.eventbus.Events;
import mte.crasmonitoring.monitoring.MonitoringService;

public class MonitoringActivity extends AppCompatActivity {
    TextView monitoringLogTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);

        monitoringLogTv = (TextView) findViewById(R.id.tv_monitoring_log);

        MonitoringService.start(getBaseContext());


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
